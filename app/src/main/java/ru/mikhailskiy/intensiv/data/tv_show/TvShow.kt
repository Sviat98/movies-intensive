package ru.mikhailskiy.intensiv.data.tv_show

import com.google.gson.annotations.SerializedName

data class TvShow(
    @SerializedName(value = "name")
    var title: String,
    @SerializedName(value = "vote_average")
    var voteAverage: Double,
    @SerializedName(value = "poster_path")
    var posterPath : String
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}