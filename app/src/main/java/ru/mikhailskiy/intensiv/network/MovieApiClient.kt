package ru.mikhailskiy.intensiv.network

import io.reactivex.internal.schedulers.RxThreadFactory
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object MovieApiClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private var client : OkHttpClient = OkHttpClient.Builder().build();

    val apiClient : MovieApiInterface by lazy {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(
            GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()

        return@lazy retrofit.create(MovieApiInterface::class.java)
    }

}
