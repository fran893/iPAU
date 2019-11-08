package com.example.fran.ipau.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fran.ipau.models.Problematica1
import com.example.fran.ipau.models.Problematica2
import com.example.fran.ipau.repositories.ProblematicasPrincipalesRepository

class ProblemasPrincipalesViewModel : ViewModel(){

    lateinit var problematica1Select : Problematica1
    lateinit var menuProblematicas : List<Problematica1>
    lateinit var problematica1Repository : ProblematicasPrincipalesRepository
    private var data: MutableLiveData<List<Problematica1>> = MutableLiveData()

    fun getData(): LiveData<List<Problematica1>> {
        return data
    }

    fun selectProblematicaOption(idProblematica1: Int): List<Problematica2>{
        problematica1Select = menuProblematicas[idProblematica1]
        return menuProblematicas[idProblematica1].subProblematicas2
    }

}