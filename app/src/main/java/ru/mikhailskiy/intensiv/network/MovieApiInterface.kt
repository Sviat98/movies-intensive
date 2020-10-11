package ru.mikhailskiy.intensiv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.mikhailskiy.intensiv.data.movie.CreditsResponse
import ru.mikhailskiy.intensiv.data.movie.Movie
import ru.mikhailskiy.intensiv.data.movie.MovieResponse
import ru.mikhailskiy.intensiv.data.tv_show.TvShowResponse


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

    @GET("movie/{id}")
    fun getMovieDetails(@Path("id") id : Int, @Query("api_key") apiKey : String, @Query("language")language: String):Call<Movie>

    @GET("movie/{id}/credits")
    fun getMovieCredits(@Path("id") id : Int, @Query("api_key") apiKey : String, @Query("language")language: String) :Call<CreditsResponse>

}
