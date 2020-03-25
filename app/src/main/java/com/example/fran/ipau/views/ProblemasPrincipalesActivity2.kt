package com.example.fran.ipau.views

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.example.fran.ipau.R
import com.example.fran.ipau.models.Problematica1
import com.example.fran.ipau.models.Problematica2
import com.example.fran.ipau.viewmodels.ProblemasPrincipalesViewModel
import kotlinx.android.synthetic.main.activity_problemas_principales2.*
import java.util.ArrayList

class ProblemasPrincipalesActivity2 : AppCompatActivity() {

    lateinit var problematicas1: List<Problematica1>
    lateinit var viewModel: ProblemasPrincipalesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problemas_principales2)
        var intent: Intent = intent
        var bundle = intent.extras
        problematicas1 = bundle.getParcelableArrayList("menuProblematicas")
        viewModel = ViewModelProviders.of(this)[ProblemasPrincipalesViewModel::class.java]
        viewModel.menuProblematicas = problematicas1
        prob1Menu.setOnClickListener {
            Log.d("onclick","prob 1 "+problematicas1[0].descripcion)
            genMenu(problematicas1[0].subProblematicas2,problematicas1[0].descripcion)
        }
        prob2Menu.setOnClickListener{
            genMenu(problematicas1[1].subProblematicas2,problematicas1[1].descripcion)
        }
        prob3Menu.setOnClickListener{
            genMenu(problematicas1[2].subProblematicas2,problematicas1[2].descripcion)
        }
        prob4Menu.setOnClickListener{
            genMenu(problematicas1[3].subProblematicas2,problematicas1[3].descripcion)
        }
        prob5Menu.setOnClickListener{
            genMenu(problematicas1[4].subProblematicas2,problematicas1[4].descripcion)
        }
        prob6Menu.setOnClickListener{
            genMenu(problematicas1[5].subProblematicas2,problematicas1[5].descripcion)
        }
    }

    fun genMenu(subMenu: List<Problematica2>,title: String){
        var intent: Intent = Intent(this, SubProblematicasActivity::class.java)
        var bundle: Bundle = Bundle()
        bundle.putParcelableArrayList("problematicas2", subMenu as ArrayList<out Parcelable>)
        bundle.putString("problematica1", title)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
