package ru.mikhailskiy.intensiv.data.movie

import com.google.gson.annotations.SerializedName

data class Studio(
    var id: Int,
    @SerializedName(value = "logo_path")
    var logoPath : String,
    var name: String,
    @SerializedName(value = "origin_country")
    var originCountry : String
)