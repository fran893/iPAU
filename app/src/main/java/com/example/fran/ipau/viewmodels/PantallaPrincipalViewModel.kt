package com.example.fran.ipau.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fran.ipau.models.Problematica1
import com.example.fran.ipau.repositories.ProblematicasPrincipalesRepository

class PantallaPrincipalViewModel : ViewModel(){

    lateinit var problematica1Repository : ProblematicasPrincipalesRepository
    private var data: MutableLiveData<List<Problematica1>> = MutableLiveData()
    lateinit var menuProblematicas : List<Problematica1>

    fun getAllProblematicas1(){
        problematica1Repository = ProblematicasPrincipalesRepository
        //menuProblematicas = problematica1Repository.getProblematicas1().value!!
        data = problematica1Repository.getProblematicas1()

    }

    fun getData(): LiveData<List<Problematica1>> {
        return data
    }

}