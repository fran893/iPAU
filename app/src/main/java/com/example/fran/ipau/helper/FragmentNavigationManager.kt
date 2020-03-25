package com.example.fran.ipau.helper

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log

import com.example.fran.ipau.BuildConfig
import com.example.fran.ipau.Interfaces.NavigationManager
import com.example.fran.ipau.views.ProblematicasMapaFragment
import com.example.fran.ipau.R
import com.example.fran.ipau.views.SubProblematicasActivity

class FragmentNavigationManager : NavigationManager {


    lateinit var mFragmentManger: FragmentManager
    lateinit var subProblematicaActivity: SubProblematicasActivity
    lateinit var mapaFragment: ProblematicasMapaFragment

    /*object getInstance{

        lateinit var mInstance : FragmentNavigationManager

        fun getmInstance(subProblematicaActivity: SubProblematicasActivity) : FragmentNavigationManager {
            if(mInstance == null)
                mInstance = FragmentNavigationManager()
            mInstance.configure(subProblematicaActivity)
            return mInstance
        }
    }*/

    companion object{

        fun newInstance(subProblematicaActivity: SubProblematicasActivity): FragmentNavigationManager{
            var mInstance = FragmentNavigationManager()
            mInstance.configure(subProblematicaActivity)
            return mInstance
        }

    }

    fun configure(subProblematicaActivity: SubProblematicasActivity) {
        var subProblematicaActivity2 = subProblematicaActivity
        subProblematicaActivity2 = subProblematicaActivity2
        mFragmentManger = subProblematicaActivity2.supportFragmentManager
    }

    override fun getFragmentByTag(tag: String): Fragment {
        return mapaFragment
    }

    override fun showFragment(title: String) {
        //mapaFragment = ProblematicasMapaFragment.newInstance(title)
        mapaFragment = ProblematicasMapaFragment.myInstace.newInstance("title")
        Log.d("fragmet","fragment "+mapaFragment.KEY_TITLE)
        showFragment(mapaFragment, false)
    }

    fun showFragment(fragment : Fragment,b : Boolean) {
        var fm : FragmentManager = mFragmentManger
        var ft : FragmentTransaction = fm.beginTransaction().add(R.id.container, fragment)
        if(b || !BuildConfig.DEBUG)
            ft.commitAllowingStateLoss()
        else
            ft.commit();
        fm.executePendingTransactions()
    }
}
