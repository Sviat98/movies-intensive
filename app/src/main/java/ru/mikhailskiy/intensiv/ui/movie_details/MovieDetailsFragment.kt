package ru.mikhailskiy.intensiv.ui.movie_details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.movie_details_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie.CreditsResponse
import ru.mikhailskiy.intensiv.data.movie.Movie
import ru.mikhailskiy.intensiv.network.MovieApiClient
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MovieDetailsFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.movie_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = requireArguments().getInt("id")


        val movieDetails = MovieApiClient.apiClient.getMovieDetails(movieId, API_KEY,"ru")

        movieDetails.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {it->Picasso.get()
                .load("https://image.tmdb.org/t/p/w500${it.posterPath}")
                .into(movie_details_image)

                movie_details_title.text = it.title

                movie_details_rating.rating = it.rating

                movie_details_description.text = it.description


                val studiosList = it.studios

                movie_details_studio.text = "Производство      ${studiosList.map { it -> it.name }.toList().joinToString()}"

                val genresList = it.genres

                movie_details_genre.text = "Жанр    ${genresList.map { it->it.name }.toList().joinToString()}"



                //Log.d("date", SimpleDateFormat("yyyy").format(response.body()!!.releaseDate).toString())

                movie_details_year.text = "Год      ${SimpleDateFormat("yyyy").format(it.releaseDate)}"
            },
            {error -> Timber.e(error)}
        )

        val movieCredits = MovieApiClient.apiClient.getMovieCredits(movieId, API_KEY,"ru")

        movieCredits.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {it->
                val actorItems = it.actors.map { it->ActorItem(it) }.toList()


                actors_recycler_view.adapter = GroupAdapter<GroupieViewHolder>().apply { addAll(actorItems) }},
            {error -> Timber.e(error)}
        )

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API

    }
}