package com.example.fran.ipau.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.fran.ipau.R
import com.example.fran.ipau.models.Login
import com.example.fran.ipau.models.Problematica1
import com.example.fran.ipau.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.loading_login_modal.view.*

class LoginActivity : AppCompatActivity(){

    private lateinit var problematicas1: List<Problematica1>
    private lateinit var viewModel: LoginViewModel
    private lateinit var progress: AlertDialog
    private lateinit var loadingAlertView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        loadingAlertView = LayoutInflater.from(this).inflate(R.layout.loading_login_modal, null)
        loadingAlertView.loadingBar.indeterminateDrawable.setColorFilter(Color.WHITE,PorterDuff.Mode.SRC_IN)
        progress = AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
                //.setTitle("Ingresando...")
                //.setMessage("Por favor espere")
                .setView(loadingAlertView)
                .setCancelable(false)
                .create()
        btn_login.setOnClickListener {
            val email: String = input_email.text.toString()
            val password: String = input_password.text.toString()
            if(email.equals("") || password.equals("")){
                Toast.makeText(this, "Debe ingresar email y contraseña", Toast.LENGTH_LONG).show()
            }else{
                progress.show()
                //progress.window.setLayout(600,400)
                viewModel.login(email,password)
                viewModel.getData().observe(this, Observer<Login>{ loginSuccess ->
                    Log.d("TOKEN","TOKEN "+loginSuccess?.access_token)
                    val sharedPref = this?.getSharedPreferences("login",Context.MODE_PRIVATE)
                    var editor = sharedPref.edit()
                    editor.putString("token",loginSuccess?.access_token)
                    editor.putString("nombre_user",loginSuccess?.nombre)
                    editor.putString("apellido_user",loginSuccess?.apellido)
                    editor.putString("correo_user",loginSuccess?.correo)
                    editor.commit()
                    progress.dismiss()
                    goToMenu()

                })
            }
        }
        link_register.setOnClickListener {

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