package com.example.fran.ipau.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.example.fran.ipau.R
import com.example.fran.ipau.models.Problematica1
import com.example.fran.ipau.viewmodels.PantallaPrincipalViewModel
import com.example.fran.ipau.viewmodels.ProblemasPrincipalesViewModel
import kotlinx.android.synthetic.main.activity_pantalla_ini.*
import kotlinx.android.synthetic.main.view_alert_dialog_error_conn.view.*
import retrofit2.Retrofit
import kotlin.system.exitProcess

class PantallaIniActivity : AppCompatActivity() {

    lateinit var problematicas1List: ArrayList<Problematica1>
    lateinit var viewModel: PantallaPrincipalViewModel
    lateinit var alertDialogErrorConnection: AlertDialog
    lateinit var viewAlerDialogErrorConn: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_ini)
        viewAlerDialogErrorConn = LayoutInflater.from(this).inflate(R.layout.view_alert_dialog_error_conn,null)

        alertDialogErrorConnection = AlertDialog.Builder(this)
                .setTitle("Error al Iniciar")
                .setCancelable(false)
                .setIcon(R.drawable.ic_error_24dp)
                .setPositiveButton("Reintentar", DialogInterface.OnClickListener{_,_ ->
                    reintentarConexion()
                })
                .setNegativeButton("Salir", DialogInterface.OnClickListener { _, _ ->
                    salir()
                })
                .create()
        viewModel = ViewModelProviders.of(this)[PantallaPrincipalViewModel::class.java]
    }

    /**
     *Este metodod obtiene el menu de la base de datos
     */
    fun getMenu(){
        viewModel.getAllProblematicas1()
        viewModel.getData().observe(this, Observer<List<Problematica1>>{menuProb ->
            var ban: Boolean = true
            if (menuProb != null) {
                menuProb.forEach {
                    if(it.descripcion == null)
                        ban = false
                }
                if(menuProb[0].descripcion != null) {
                    viewModel.menuProblematicas = menuProb
                    var intent = Intent(this, ProblemasPrincipalesActivity2::class.java)
                    var bundle = Bundle()
                    bundle.putParcelableArrayList("menuProblematicas", menuProb as java.util.ArrayList<out Parcelable>)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressBar.visibility = View.GONE
                    alertDialogErrorConnection.show()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        getMenu()
    }

    fun reintentarConexion(){
        progressBar.visibility = View.VISIBLE
        alertDialogErrorConnection.cancel()
        getMenu()
    }

    fun salir(){
        alertDialogErrorConnection.cancel()
        finish()
        exitProcess(0)
    }
}
