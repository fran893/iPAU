package com.example.fran.ipau.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import com.example.fran.ipau.R
import com.example.fran.ipau.models.Login
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object Utilidades{

    fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor{
        var canvas = Canvas()
        var bitmap: Bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight);
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun getToken(activity: Activity): Login {
        var login = Login()
        val sharedPref = activity?.getSharedPreferences("login",Context.MODE_PRIVATE)
        login.access_token = sharedPref!!.getString("token","")
        login.nombre = sharedPref!!.getString("nombre_user","")
        login.apellido = sharedPref!!.getString("apellido_user","")
        login.correo = sharedPref!!.getString("correo_user","")
        login.id = sharedPref!!.getString("id_user","")
        return login
    }

}