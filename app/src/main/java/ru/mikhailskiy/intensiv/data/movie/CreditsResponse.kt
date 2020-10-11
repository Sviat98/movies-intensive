package ru.mikhailskiy.intensiv.data.movie

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    var id : Int,
    @SerializedName(value = "cast")
    var actors : List<Actor>
)