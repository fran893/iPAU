package com.example.fran.ipau.apis

import com.example.fran.ipau.models.Problematica1
import io.reactivex.Single

import retrofit2.Call
import retrofit2.http.GET

interface ApiProblematica1 {

    //@get:GET("listarProblematicas")
    @get:GET("api/problematicas/listarProblematicas")
    val allProblematicas1: Single<List<Problematica1>>

}
