package com.example.fran.ipau.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class BasicAuthInterceptor(user: String, password: String): Interceptor {

    private var credentials: String = Credentials.basic(user, password)


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var authenticatedRequest: Request = request.newBuilder()
                .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

}