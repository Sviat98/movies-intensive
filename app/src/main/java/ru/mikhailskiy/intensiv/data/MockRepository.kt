package ru.mikhailskiy.intensiv.data

import ru.mikhailskiy.intensiv.data.movie.Genre
import ru.mikhailskiy.intensiv.data.movie.Movie
import ru.mikhailskiy.intensiv.data.movie.Studio
import java.util.*

object MockRepository {

    fun getMovies(): List<Movie> {

        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x,
                posterPath = "/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhYjQtOTQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg",
                id =1,
                description  = "descr",
            studios = listOf(Studio(1,"studio","studio","country")),
                genres = listOf(Genre(1,"genre")),
            releaseDate  = Date(2020)
            )
            moviesList.add(movie)
        }

        return moviesList
    }

}