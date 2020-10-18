package ru.mikhailskiy.intensiv.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.MockRepository
import ru.mikhailskiy.intensiv.data.movie.Movie
import ru.mikhailskiy.intensiv.data.movie.MovieResponse
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.afterTextChanged
import ru.mikhailskiy.intensiv.ui.feed.MainCardContainer
import ru.mikhailskiy.intensiv.ui.feed.MovieItem
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val adapter by lazy {
        GroupAdapter<com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchTerm = requireArguments().getString("search")
        search_toolbar.setText(searchTerm)

        val searchSource = Observable.create(ObservableOnSubscribe<String> { e ->
            search_toolbar.search_edit_text.afterTextChanged {
                e.onNext(search_toolbar.search_edit_text.text.toString())
            }
            e.setCancellable {
                e.onComplete()
            }
        })

        searchSource.map { text-> text.trim() }.filter { text -> text.length>3 }.debounce(500,
            TimeUnit.MILLISECONDS).
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                    it ->
                searchMovies(it.toString())
                //Timber.tag("Searchterm").d(it.toString())
            }
        )

    }

    override fun onStop() {
        super.onStop()
        //adapter.clear()
    }

    private fun searchMovies(searchTerm: String){
        val searchMovies = MovieApiClient.apiClient.searchMovies(API_KEY,"ru",searchTerm)

        searchMovies.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
        doOnSubscribe { search_movie_progress_bar.visibility = View.VISIBLE }.doFinally { search_movie_progress_bar.visibility = View.GONE }
            .onErrorReturnItem(MovieResponse(1,1,1,MockRepository.getMovies())).subscribe(
            {it->val searchMoviesList =
                it.results.map {
                    MovieItem(it) { movie ->
                        openMovieDetails(movie)
                    }
                }.toList()

                search_movies_recycler_view.adapter =  adapter.apply { update(searchMoviesList) }
                Log.d("movie",it.results.map { it-> it.title }.toList().joinToString())
            },
            {error -> Timber.e(error)}
        )
    }

    private fun openMovieDetails(movie: Movie) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putInt("id", movie.id)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
    }
}