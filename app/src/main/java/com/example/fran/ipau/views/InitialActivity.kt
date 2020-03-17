package com.example.fran.ipau.views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import com.example.fran.ipau.R
import com.example.fran.ipau.models.Problematica1
import kotlinx.android.synthetic.main.activity_initial.*

class InitialActivity : AppCompatActivity() {

    private lateinit var problematicas1: List<Problematica1>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        btn_ingresar.setOnClickListener {
            goToMenu()
        }
        btn_nextLogin.setOnClickListener {
            goToLogin()
        }
    }

    fun goToMenu(){
        var intent: Intent = intent
        var bundle = intent.extras
        problematicas1 = bundle.getParcelableArrayList("menuProblematicas")
        intent = Intent(this, ProblemasPrincipalesActivity2::class.java)
        bundle.putParcelableArrayList("menuProblematicas", problematicas1 as java.util.ArrayList<out Parcelable>)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun goToLogin() {
        var intent: Intent = intent
        var bundle = intent.extras
        problematicas1 = bundle.getParcelableArrayList("menuProblematicas")
        intent = Intent(this, LoginActivity::class.java)
        bundle.putParcelableArrayList("menuProblematicas", problematicas1 as java.util.ArrayList<out Parcelable>)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
