package com.example.fran.ipau.repositories

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.fran.ipau.apis.ApiLogin
import com.example.fran.ipau.models.Login
import com.example.fran.ipau.network.RetrofitInstanceProblematicaLoc
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

object LoginRepository {

    fun init(){}

    fun login(username: String, password: String): MutableLiveData<Login> {
        Log.d("LoginK repository","En login repository")
        var login = MutableLiveData<Login>()
        var retrofit = RetrofitInstanceProblematicaLoc.createApiLogin()
        var service = retrofit.create(ApiLogin::class.java)
        service.login(username, password, "password")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (

                        onSuccess = { loginSuccess ->
                            login.postValue(loginSuccess)
                        },
                        onError = {error->
                            Log.d("ERROR *** ","Error: "+error.message)
                        }
                )
        return login
    }

}