package ru.mikhailskiy.intensiv.data.movie

import com.google.gson.annotations.SerializedName
import ru.mikhailskiy.intensiv.data.movie.Movie

data class MovieResponse(
    var page: Int,
    @SerializedName(value = "total_results")
    var totalResults: Int,
    @SerializedName(value = "total_pages")
    var totalPages: Int,
    var results : List<Movie>
)