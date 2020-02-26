package com.example.fran.ipau.views

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.fran.ipau.R
import com.example.fran.ipau.models.Problematica1
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){

    private lateinit var problematicas1: List<Problematica1>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener {
            val email: String = input_email.text.toString()
            val password: String = input_password.text.toString()
            if(email.equals("") || password.equals("")){
                Toast.makeText(this, "Debe ingresar email y contraseña", Toast.LENGTH_LONG).show()
            }
        }
        link_guest_sign.setOnClickListener {
            goToMenu()
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

}