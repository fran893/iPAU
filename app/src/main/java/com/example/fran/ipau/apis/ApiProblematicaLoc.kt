package com.example.fran.ipau.apis

import com.example.fran.ipau.models.ProblematicaLocation
import io.reactivex.Single

import retrofit2.Call
import retrofit2.http.*

interface ApiProblematicaLoc {

    @get:GET("v1/locationsProb")
    val allProblematicasLoc: Single<List<ProblematicaLocation>>

    @GET("v1/locationsProb3/{idProb3}")
    fun getProblematicasLocProb3(@Path("idProb3") idProb3: Int): Single<List<ProblematicaLocation>>

    @PUT("v1/addProblematicaLocation/")
    fun saveLocation(@Body problematicaLocation: ProblematicaLocation): Single<List<ProblematicaLocation>>

}
