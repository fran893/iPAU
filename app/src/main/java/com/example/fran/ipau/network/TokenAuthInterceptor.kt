package com.example.fran.ipau.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenAuthInterceptor (token: String): Interceptor {

    private var token = token

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build()
        return chain.proceed(request)
    }

}