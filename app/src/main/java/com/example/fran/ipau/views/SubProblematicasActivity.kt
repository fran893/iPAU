package com.example.fran.ipau.views

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v4.view.GravityCompat
import com.example.fran.ipau.models.Problematica2
import com.example.fran.ipau.Interfaces.NavigationManager
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.example.fran.ipau.R
import com.example.fran.ipau.adapters.CustomExpandableListAdapter
import com.example.fran.ipau.helper.FragmentNavigationManager
import com.example.fran.ipau.utils.Utilidades
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class SubProblematicasActivity : AppCompatActivity(){
    lateinit var mDrawerLayout: DrawerLayout

    lateinit var mDraweToggle: ActionBarDrawerToggle
    lateinit var mActivityTitle: String
    lateinit var items: ArrayList<String>
    lateinit var expandableListView: ExpandableListView
    lateinit var adapter: ExpandableListAdapter
    lateinit var lstTitle: ArrayList<String>
    lateinit var lstChild: LinkedHashMap<String, List<String>>
    lateinit var navigationManager: NavigationManager
    lateinit var problematicas2: List<Problematica2>
    //lateinit var intent: Intent
    lateinit var bundle: Bundle
    lateinit var problematica1Descripcion: String
    lateinit var txtProb1: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_problematica2)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mActivityTitle = title.toString()
        expandableListView = findViewById(R.id.navList)
        navigationManager = FragmentNavigationManager.newInstance(this)

        var listHeaderView: View = layoutInflater.inflate(R.layout.nav_header,null,false)
        expandableListView.addHeaderView(listHeaderView)

        var intent: Intent = intent
        bundle = intent.extras
        problematicas2 = bundle.getParcelableArrayList("problematicas2")
        problematica1Descripcion = bundle.getString("problematica1")

        txtProb1 = listHeaderView.findViewById(R.id.name)
        txtProb1.text = problematica1Descripcion

        initItems()
        genData()
        addDrawesItem()
        setUpDrawer()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)
        navigationManager.showFragment(problematica1Descripcion)
        mDrawerLayout.openDrawer(GravityCompat.START)

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDraweToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mDraweToggle.onConfigurationChanged(newConfig)
    }

    fun selectFirstItemAsDefault(){
        var firstItem: String = lstTitle[0]
        navigationManager.showFragment(firstItem)
        supportActionBar?.title = firstItem.toString()
    }

    private fun setUpDrawer() {
        mDraweToggle = object : ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                //getSupportActionBar().setTitle("EDMTDev");
                invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu()
            }
        }

        mDraweToggle.isDrawerIndicatorEnabled = true
        mDrawerLayout.setDrawerListener(mDraweToggle)
    }

    fun setDrawerLocked(shouldLock: Boolean) {
        if(shouldLock)
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        else
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    fun addDrawesItem(){
        adapter = CustomExpandableListAdapter(this,lstTitle,lstChild)
        expandableListView.setAdapter(adapter)
        expandableListView.setOnGroupExpandListener { groupPosition ->
            supportActionBar!!.title = lstTitle[groupPosition] // set title for toolbar when open
        }

        expandableListView.setOnGroupCollapseListener { supportActionBar!!.setTitle(com.example.fran.ipau.R.string.app_name) }

        expandableListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            //change fragment when click on item
            setDrawerLocked(true)
            var prob2 = problematicas2[groupPosition]
            var prob3 = prob2.subProblematicas3[childPosition]
            supportActionBar?.title = prob3.descripcion
            var mapaFragment = navigationManager.getFragmentByTag("fragmentMap") as ProblematicasMapaFragment
            mapaFragment.setProblematicas(prob2, prob3)
            setDrawerLocked(false)
            false
        }
    }

    fun genData(){
        lstChild = LinkedHashMap()
        problematicas2.forEach{ prob2 ->
            var childItem: ArrayList<String> = ArrayList<String>()
            prob2.subProblematicas3.forEach{prob3 ->
                childItem.add(prob3.descripcion)
            }
            lstChild[prob2.descripcion] = childItem
        }
        lstTitle = ArrayList(lstChild.keys)
    }

    fun initItems(){
        items = ArrayList<String>()
        problematicas2.forEach {
            items.add(it.descripcion)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean{
        var id: Int = item.itemId
        if(mDraweToggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

}
