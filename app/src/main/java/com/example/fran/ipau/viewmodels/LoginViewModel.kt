package com.example.fran.ipau.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fran.ipau.models.Login
import com.example.fran.ipau.repositories.LoginRepository

class LoginViewModel : ViewModel(){

    lateinit var loginRepository: LoginRepository
    private var data: MutableLiveData<Login> = MutableLiveData()

    fun login(username: String, password: String){
        loginRepository = LoginRepository
        data = loginRepository.login(username, password)
    }

    fun getData(): MutableLiveData<Login> {
        return data
    }

}