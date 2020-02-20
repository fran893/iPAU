package com.example.fran.ipau.apis

import com.example.fran.ipau.models.ProblematicaLocation
import io.reactivex.Single

import retrofit2.Call
import retrofit2.http.*

interface ApiProblematicaLoc {

    @get:GET("problematicasLocation")
    val allProblematicasLoc: Single<List<ProblematicaLocation>>

    @GET("locationsProb3/{idProb3}")
    fun getProblematicasLocProb3(@Path("idProb3") idProb3: Int): Single<List<ProblematicaLocation>>

    @POST("addProblematicaLocation")
    fun saveLocation(@Body problematicaLocation: ProblematicaLocation): Single<ProblematicaLocation>

    @GET("getLocation/{lat}/{long}")
    fun getProblematicaLocation(@Path("lat") lat: Double, @Path("long") long: Double): Single<ProblematicaLocation>

    @PUT("updateCountMarker/{idProbLocation}")
    fun updateCountMarkerProbLocation(@Path("idProbLocation") idProbLocation: Long): Single<ProblematicaLocation>

}
