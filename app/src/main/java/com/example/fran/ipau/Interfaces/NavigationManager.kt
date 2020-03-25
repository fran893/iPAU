package com.example.fran.ipau.Interfaces

import android.support.v4.app.Fragment

interface NavigationManager {

    fun showFragment(title: String)

    fun getFragmentByTag(tag: String): Fragment

}
