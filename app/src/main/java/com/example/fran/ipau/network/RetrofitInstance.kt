package com.example.fran.ipau.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    //private val BASE_URL = "http://192.168.1.103:8080/ApiRestFull/" desa
    private val BASE_URL = "https://ipau-rest-full.herokuapp.com/" //test
    //private val BASE_URL = "https://ipau1-rest-full.herokuapp.com/" //timeout test
    private val okHttpClient = OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120,TimeUnit.SECONDS)
            .build()

    fun createApi(): Retrofit{
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

}
