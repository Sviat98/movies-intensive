package ru.mikhailskiy.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie.Movie
import ru.mikhailskiy.intensiv.data.movie.MovieResponse
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.afterTextChanged
import timber.log.Timber

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

        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > 3) {
                openSearch(it.toString())
            }
        }

        val topRatedMovies = MovieApiClient.apiClient.getTopRatedMovies(API_KEY,"ru")

        topRatedMovies.enqueue(object : Callback<MovieResponse>{
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val recommendedMoviesList = listOf(
                    MainCardContainer(
                        R.string.recommended,
                        response.body()!!.results.map {
                            MovieItem(it) { movie ->
                                openMovieDetails(
                                    movie
                                )
                            }
                        }.toList()
                    )
                )

                movies_recycler_view.adapter = adapter.apply { addAll(recommendedMoviesList) }

            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Timber.e(t.toString())
            }
        })

        val upcomingMovies = MovieApiClient.apiClient.getUpcomingMovies(API_KEY,"ru")

        upcomingMovies.enqueue(object : Callback<MovieResponse>{
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val newMoviesList = listOf(
                    MainCardContainer(
                        R.string.upcoming,
                        response.body()!!.results.map {
                            MovieItem(it) { movie ->
                                openMovieDetails(movie)
                            }
                        }.toList()
                    )
                )

                movies_recycler_view.adapter =  adapter.apply { addAll(newMoviesList) }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Timber.e(t.toString())
            }
        })

        val popularMovies = MovieApiClient.apiClient.getPopularMovies(API_KEY,"ru")

        popularMovies.enqueue(object : Callback<MovieResponse>{
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val popularMoviesList = listOf(
                    MainCardContainer(
                        R.string.popular,
                        response.body()!!.results.map {
                            MovieItem(it) { movie ->
                                openMovieDetails(movie)
                            }
                        }.toList()
                    )
                )

                movies_recycler_view.adapter =  adapter.apply { addAll(popularMoviesList) }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Timber.e(t.toString())
            }
        })

        val nowPlayingMovies = MovieApiClient.apiClient.getNowPlayingMovies(API_KEY,"ru")

        nowPlayingMovies.enqueue(object : Callback<MovieResponse>{
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val nowPlayingMoviesList = listOf(
                    MainCardContainer(
                        R.string.now_playing,
                        response.body()!!.results.map {
                            MovieItem(it) { movie ->
                                openMovieDetails(movie)
                            }
                        }.toList()
                    )
                )

                movies_recycler_view.adapter =  adapter.apply { addAll(nowPlayingMoviesList) }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Timber.e(t.toString())
            }
        })

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
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
    }
}