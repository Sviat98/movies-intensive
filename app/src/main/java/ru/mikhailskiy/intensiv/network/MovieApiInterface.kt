package ru.mikhailskiy.intensiv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mikhailskiy.intensiv.data.MovieResponse
import ru.mikhailskiy.intensiv.data.TvShowResponse


interface MovieApiInterface {
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey : String ,@Query("language") language: String):Call<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey : String ,@Query("language") language: String):Call<MovieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("api_key") apiKey : String ,@Query("language") language: String):Call<MovieResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("api_key") apiKey : String ,@Query("language") language: String):Call<MovieResponse>

    @GET("tv/popular")
    fun getAllTvShows(@Query("api_key") apiKey : String ,@Query("language")language: String):Call<TvShowResponse>

}
