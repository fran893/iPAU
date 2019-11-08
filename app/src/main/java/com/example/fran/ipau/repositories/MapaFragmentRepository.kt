package com.example.fran.ipau.repositories

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.fran.ipau.apis.ApiProblematicaLoc
import com.example.fran.ipau.models.ProblematicaLocation
import com.example.fran.ipau.network.RetrofitInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

object MapaFragmentRepository{

    fun init(){}

    fun getAllProblematicas3(idProb3: Int) : MutableLiveData<List<ProblematicaLocation>>{
        val initProblematicaLocation = MutableLiveData<List<ProblematicaLocation>>()
        val retrofit = RetrofitInstance.createApi()
        val service = retrofit.create(ApiProblematicaLoc::class.java)
        service.getProblematicasLocProb3(idProb3)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                    onSuccess = {
                        initProblematicaLocation.postValue(it)
                    },
                    onError = { error ->
                        Log.d("Error menu",error.message)
                    }
                )
        return initProblematicaLocation
    }

    fun guardarProblematicaLocation(location: ProblematicaLocation): MutableLiveData<ProblematicaLocation>{
        val saveLocation = MutableLiveData<ProblematicaLocation>()
        val retrofit = RetrofitInstance.createApi()
        val service = retrofit.create(ApiProblematicaLoc::class.java)
        service.saveLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                        onSuccess = {
                            saveLocation.postValue(it[0])
                        },
                        onError = { error ->
                            Log.d("Error","Error al guardar locacion "+error.message)
                        }
                )
        return saveLocation
    }

}