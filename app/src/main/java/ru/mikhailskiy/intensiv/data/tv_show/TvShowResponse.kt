package ru.mikhailskiy.intensiv.data.tv_show

import com.google.gson.annotations.SerializedName
import ru.mikhailskiy.intensiv.data.tv_show.TvShow

data class TvShowResponse(
    var page: Int,
    @SerializedName(value = "total_results")
    var totalResults: Int,
    @SerializedName(value = "total_pages")
    var totalPages: Int,
    var results : List<TvShow>
)