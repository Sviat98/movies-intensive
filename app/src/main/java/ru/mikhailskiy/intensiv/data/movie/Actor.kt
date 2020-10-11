package ru.mikhailskiy.intensiv.data.movie

import com.google.gson.annotations.SerializedName

data class Actor(
    var id : Int,
    var name : String,
    @SerializedName(value = "profile_path")
    var profilePath : String
)