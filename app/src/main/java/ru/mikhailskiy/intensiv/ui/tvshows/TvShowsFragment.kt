package ru.mikhailskiy.intensiv.ui.tvshows

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.MockRepository
import ru.mikhailskiy.intensiv.data.tv_show.TvShowResponse
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.feed.MovieItem
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private val adapter by lazy {
    GroupAdapter<GroupieViewHolder>()
}

class TvShowsFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        return inflater.inflate(R.layout.tv_shows_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val popularTvShows= MovieApiClient.apiClient.getAllTvShows(API_KEY,"ru")

        popularTvShows.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
        doOnSubscribe { tv_show_progress_bar.visibility = View.VISIBLE }.doFinally { tv_show_progress_bar.visibility=  View.GONE }.subscribe(
            {it->
                val popularTvShowsItems = it.results.map { it -> TvShowItem(it) }.toList()

                tv_shows_recycler_view.adapter = adapter.apply { addAll(popularTvShowsItems) }
            },
            { error->Timber.e(error)}
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TvShowsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
    }
}