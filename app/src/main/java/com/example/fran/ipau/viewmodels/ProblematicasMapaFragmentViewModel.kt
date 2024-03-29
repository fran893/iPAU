package com.example.fran.ipau.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fran.ipau.models.ProblematicaLocation
import com.example.fran.ipau.repositories.MapaFragmentRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ProblematicasMapaFragmentViewModel : ViewModel() {

    private var initMarkers: MutableLiveData<List<MarkerOptions>> = MutableLiveData()
    private var initProblematicaLocation: MutableLiveData<List<ProblematicaLocation>> = MutableLiveData()
    private var saveLocationLiveData: MutableLiveData<ProblematicaLocation> = MutableLiveData()
    lateinit var repositoryMapaFragmet: MapaFragmentRepository

    fun addMarkersInit(problematicasLocations: List<ProblematicaLocation>) : ArrayList<MarkerOptions>{
        var addsMarker : ArrayList<MarkerOptions> = arrayListOf()
        problematicasLocations.forEach { problematicaLoc ->
            var latLng: LatLng = LatLng(problematicaLoc.latitud, problematicaLoc.longitud)
            var markerOptions: MarkerOptions = MarkerOptions()
            markerOptions.position(latLng)
            if(problematicaLoc.descripcion != "")
                markerOptions.title(problematicaLoc.descripcion)
            else
                markerOptions.title("Ubicación de Problemática")
            addsMarker.add(markerOptions)
        }
        return addsMarker
    }

    fun getInitProblematicasLocation(idProb3 : Int) {
        repositoryMapaFragmet = MapaFragmentRepository
        initProblematicaLocation = repositoryMapaFragmet.getAllProblematicas3(idProb3)
    }

    fun getInitProblematicaLocation(): MutableLiveData<List<ProblematicaLocation>>{
        return initProblematicaLocation
    }

    fun getInitMarkers(): MutableLiveData<List<MarkerOptions>>{
        return initMarkers
    }

    fun guardarProblematicaLocation(location: ProblematicaLocation){
        saveLocationLiveData = repositoryMapaFragmet.guardarProblematicaLocation(location)
    }

    fun getSaveLocationLiveData(): MutableLiveData<ProblematicaLocation>{
        return saveLocationLiveData
    }
}