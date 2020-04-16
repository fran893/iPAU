package com.example.fran.ipau.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstanceProblematicaLoc {

    private val BASE_URL_DESA = "http://192.168.1.106:8090/"
    private val BASE_URL = "http://ec2-3-14-28-90.us-east-2.compute.amazonaws.com:8090/" //test
    private var retrofitInstanceAuth: Retrofit? = null
    private var httpClientAuth: OkHttpClient? = null

    private val okHttpClient = OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build()

    private val httpClientLogin = OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(BasicAuthInterceptor("ipau_app","IpauApp.Android2019"))
            .build()



    fun createApi(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL_DESA)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }
    
    fun createApiLogin(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL_DESA)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClientLogin)
                .build()
    }

    fun getRetrofitInstanceAuth(token: String): Retrofit {
        if(retrofitInstanceAuth == null) {
            httpClientAuth = OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(TokenAuthInterceptor(token))
                    .build()
            retrofitInstanceAuth = Retrofit.Builder()
                    .baseUrl(BASE_URL_DESA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClientAuth)
                    .build()
        }
        return retrofitInstanceAuth!!
    }

}

