package ru.mikhailskiy.intensiv.data

import com.google.gson.annotations.SerializedName

data class Movie(
    var title: String,
    @SerializedName(value = "vote_average")
    var voteAverage: Double,
    @SerializedName(value = "poster_path")
    var posterPath : String
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
/*
"popularity": 55.472,
"vote_count": 13049,
"video": false,
"poster_path": "/gZUc6DbAirZGWJL1685jsOd90Sf.jpg",
"id": 238,
"adult": false,
"backdrop_path": "/rSPw7tgCH9c6NqICZef4kZjFOQ5.jpg",
"original_language": "en",
"original_title": "The Godfather",
"genre_ids": [
80,
18
],
"title": "Крестный отец",
"vote_average": 8.7,
"overview": "Криминальная сага, повествующая о нью-йоркской сицилийской мафиозной семье Корлеоне. Фильм охватывает период 1945-1955 годов. Глава семьи, Дон Вито Корлеоне, выдаёт замуж свою дочь. В это время со Второй мировой войны возвращается его любимый сын Майкл. Майкл, герой войны, гордость семьи, не выражает желания заняться жестоким семейным бизнесом. Дон Корлеоне ведёт дела по старым правилам, но наступают иные времена, и появляются люди, желающие изменить сложившиеся порядки. На Дона Корлеоне совершается покушение.",
"release_date": "1972-03-14"
},

 */
