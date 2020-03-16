package com.example.fran.ipau.apis

import com.example.fran.ipau.models.Login
import com.example.fran.ipau.models.LoginK
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiLogin {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("api/security/oauth/token")
    fun login(@Field("username") username: String,
              @Field("password") password: String,
              @Field("grant_type") grant_type: String): Single<Login>



}