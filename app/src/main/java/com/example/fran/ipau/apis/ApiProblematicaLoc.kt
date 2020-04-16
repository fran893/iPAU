package com.example.fran.ipau.apis

import com.example.fran.ipau.models.ProblematicaLocation
import io.reactivex.Single

import retrofit2.Call
import retrofit2.http.*

interface ApiProblematicaLoc {

    @get:GET("api/probLocation/problematicasLocation")
    val allProblematicasLoc: Single<List<ProblematicaLocation>>

    @GET("api/probLocation/locationsProb3/{idProb3}")
    fun getProblematicasLocProb3(@Path("idProb3") idProb3: Int): Single<List<ProblematicaLocation>>

    @POST("api/probLocation/addProblematicaLocation")
    fun saveLocation(@Body problematicaLocation: ProblematicaLocation): Single<ProblematicaLocation>

    @GET("api/probLocation/getLocation/{lat}/{long}")
    fun getProblematicaLocation(@Path("lat") lat: Double, @Path("long") long: Double): Single<ProblematicaLocation>

    @PUT("api/probLocation/updateCountMarker/{idProbLocation}")
    fun updateCountMarkerProbLocation(@Path("idProbLocation") idProbLocation: Long): Single<ProblematicaLocation>

    @GET("api/probLocation/problematicasLocationPerPrivacy/{isPrivacy}/{idProb3}")
    fun getProblematicaLocByPrivacy(@Path("isPrivacy") isPrivacy: Boolean, @Path("idProb3") idProb3: Int): Single<List<ProblematicaLocation>>

    @PUT("api/probLocation/updateProbLocation/{idProbLocation}")
    fun updateProbLocation(@Path("idProbLocation") idProbLocation: Long, @Body problematicaLocation: ProblematicaLocation): Single<ProblematicaLocation>

}
