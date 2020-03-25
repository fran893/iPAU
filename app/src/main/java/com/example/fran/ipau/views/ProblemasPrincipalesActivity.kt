package com.example.fran.ipau.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.fran.ipau.R
import com.example.fran.ipau.models.Problematica1
import com.example.fran.ipau.models.Problematica2
import com.example.fran.ipau.viewmodels.ProblemasPrincipalesViewModel
import kotlinx.android.synthetic.main.activity_problemas_principales.*
import java.util.ArrayList


class ProblemasPrincipalesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var navView : NavigationView
    lateinit var problematicas1: List<Problematica1>
    var problematica1Select: Problematica1? = null
    lateinit var menu: Menu
    lateinit var viewModel: ProblemasPrincipalesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var intent: Intent = intent
        var bundle = intent.extras
        problematicas1 = bundle.getParcelableArrayList("menuProblematicas")
        getMenu(problematicas1)
        viewModel = ViewModelProviders.of(this)[ProblemasPrincipalesViewModel::class.java]
        viewModel.menuProblematicas = problematicas1
    }

    /*override fun onStart() {
        super.onStart()
        viewModel.getData().observe(this, Observer<List<Problematica1>>{menuProb ->
            if (menuProb != null) {
                viewModel.menuProblematicas = menuProb
                getMenu(menuProb)
            }
        })
    }*/

    fun getMenu(menuProblematicas : List<Problematica1> ){
        setContentView(R.layout.activity_problemas_principales)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navView = navigationView
        navView.setNavigationItemSelectedListener(this)
        navView.itemIconTintList = null
        var orderMenu : Int = 1
        menu = navView.menu
        menuProblematicas.forEach {
            when (it.idProblematica1) {
                1 -> {
                    menu.add(0, it.idProblematica1, orderMenu, it.descripcion).setIcon(R.drawable.ic_prob_1_saneamiento_24dp)
                }//                        //menu.add(0, prob1.getIdProblematica1(), prob1.getIdProblematica1(), prob1.getDescripcion());
                //MenuItem item = menu.getItem(0);
                //item.text
                2 -> {
                    menu.add(0, it.idProblematica1, orderMenu, it.descripcion).setIcon(R.drawable.ic_prob_2_contaminacion_24dp)
                }
                3 -> {
                    menu.add(0, it.idProblematica1, orderMenu, it.descripcion).setIcon(R.drawable.ic_prob_3_zoonosis_24dp)
                }
                4 -> {
                    menu.add(0, it.idProblematica1, orderMenu, it.descripcion).setIcon(R.drawable.ic_prob_4_espverdes_24dp)
                }
                5 -> {
                    menu.add(0, it.idProblematica1, orderMenu, it.descripcion).setIcon(R.drawable.ic_prob_5_insfrpublica_24dp)
                }
                6 -> {
                    menu.add(0, it.idProblematica1, orderMenu, it.descripcion).setIcon(R.drawable.ic_prob_6_otros_24dp)
                }
                else -> {
                }
            }
            orderMenu ++;
        }
    }
    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.problemas_principales, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId - 1
        var problematicas2: List<Problematica2>
        problematicas2 = viewModel.selectProblematicaOption(id)
        problematica1Select = viewModel.problematica1Select
        var intent: Intent = Intent(this, SubProblematicasActivity::class.java)
        var bundle: Bundle = Bundle()
        bundle.putParcelableArrayList("problematicas2", problematicas2 as ArrayList<out Parcelable>)
        bundle.putString("problematica1", problematica1Select!!.descripcion)
        intent.putExtras(bundle)
        startActivity(intent)
        return true
    }
}
