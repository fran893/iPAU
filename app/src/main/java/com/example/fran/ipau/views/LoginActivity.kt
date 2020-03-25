package com.example.fran.ipau.views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.fran.ipau.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener {
            var intent = Intent(this, CargaMenuActivity::class.java)
            startActivity(intent)
        }
    }

}