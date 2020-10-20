package ru.mikhailskiy.intensiv.ui.feed

import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.MockRepository
import ru.mikhailskiy.intensiv.data.movie.Movie
import ru.mikhailskiy.intensiv.data.movie.MovieResponse
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.afterTextChanged
import timber.log.Timber
import java.util.*
import java.util.Collections.addAll
import java.util.concurrent.TimeUnit

class FeedFragment : Fragment() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Добавляем recyclerView
        movies_recycler_view.layoutManager = LinearLayoutManager(context)
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }


        search_toolbar.onTextChangedPublishSubject.map { text-> text.trim() }.filter { text -> text.length>3 }.debounce(500,TimeUnit.MILLISECONDS).
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                    it ->
                openSearch(it.toString())
                    //Timber.tag("Searchterm").d(it.toString())
            }
        )
        

        val topRatedMovies = MovieApiClient.apiClient.getTopRatedMovies(API_KEY,"ru")

        val upcomingMovies = MovieApiClient.apiClient.getUpcomingMovies(API_KEY,"ru")

        val popularMovies = MovieApiClient.apiClient.getPopularMovies(API_KEY,"ru")

        val nowPlayingMovies = MovieApiClient.apiClient.getNowPlayingMovies(API_KEY,"ru")


        Single.zip(topRatedMovies,popularMovies,nowPlayingMovies,upcomingMovies,
            Function4<MovieResponse,MovieResponse,MovieResponse,MovieResponse,List<MainCardContainer>> { response1, response2, response3, response4->
                listOf(
                    MainCardContainer(
                    R.string.recommended,
                    response1.results.map {
                        MovieItem(it) { movie ->
                            openMovieDetails(
                                movie
                            )
                        }
                    }.toList()
                ),MainCardContainer(
                    R.string.popular,
                    response2.results.map {
                        MovieItem(it) { movie ->
                            openMovieDetails(
                                movie
                            )
                        }
                    }.toList()
                ),MainCardContainer(
                    R.string.now_playing,
                    response3.results.map {
                        MovieItem(it) { movie ->
                            openMovieDetails(
                                movie
                            )
                        }
                    }.toList()
                ),MainCardContainer(
                    R.string.upcoming,
                    response4.results.map {
                        MovieItem(it) { movie ->
                            openMovieDetails(
                                movie
                            )
                        }
                    }.toList()
                ))
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe { movie_item_progress_bar.visibility = View.VISIBLE }
            .doFinally { movie_item_progress_bar.visibility = View.GONE }.onErrorReturnItem(
                listOf(
                    MainCardContainer(
                        R.string.recommended,
                        MockRepository.getMovies().map {
                            MovieItem(it) { movie ->
                            openMovieDetails(
                                movie
                            ) }
                        }.toList()
                    ),MainCardContainer(
                        R.string.popular,
                        MockRepository.getMovies().map {
                            MovieItem(it) { movie ->
                                openMovieDetails(
                                    movie
                                ) }
                        }.toList()
                    ),MainCardContainer(
                        R.string.now_playing,
                        MockRepository.getMovies().map {
                            MovieItem(it) { movie ->
                                openMovieDetails(
                                    movie
                                ) }
                        }.toList()
                    ),MainCardContainer(
                        R.string.upcoming,
                        MockRepository.getMovies().map {
                            MovieItem(it) { movie ->
                                openMovieDetails(
                                    movie
                                ) }
                        }.toList()
                    ))
            )
            .subscribe(
            {allMovies-> movies_recycler_view.adapter = adapter.apply { addAll(allMovies)}},
            { error-> Timber.e(error)}
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

    private fun openSearch(searchText: String) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putString("search", searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
        adapter.clear()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
    }
}