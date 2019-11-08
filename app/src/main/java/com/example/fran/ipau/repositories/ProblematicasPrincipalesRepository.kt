package com.example.fran.ipau.repositories

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.fran.ipau.apis.ApiProblematica1
import com.example.fran.ipau.models.Problematica1
import com.example.fran.ipau.network.RetrofitInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

object ProblematicasPrincipalesRepository{

    fun init(){}

    fun getProblematicas1(): MutableLiveData<List<Problematica1>>{
        //var menuProblematicas : List<Problematica1> = arrayListOf()
        val menu = MutableLiveData<List<Problematica1>>()
        val retrofit = RetrofitInstance.createApi()
        val service = retrofit.create(ApiProblematica1::class.java)
        service.allProblematicas1
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { resultMenu ->
                            Log.d("success","entra a success de llamada a ws.. ")
                            menu.postValue(resultMenu)
                        },
                        onError = { error ->
                            Log.d("Error menu",error.message)
                            menu.postValue(arrayListOf(Problematica1()))
                        }
                )

        return menu
    }

}