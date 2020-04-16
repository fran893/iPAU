package com.example.fran.ipau.views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import com.example.fran.ipau.R
import com.example.fran.ipau.models.Problematica1
import com.example.fran.ipau.utils.Utilidades
import com.synnapps.carouselview.CarouselView
import kotlinx.android.synthetic.main.activity_intro_carousel.*

class IntroCarouselActivity : AppCompatActivity() {

    private val imagesCarousel = arrayOf (
            R.drawable.carousel1,
            R.drawable.carousel2,
            R.drawable.carousel3,
            R.drawable.carousel4
    )

    private val imageTitle = arrayOf (
            "Batman", "Nada", "Lampara", "Darth Vader"
    )

    private var lastImage: Boolean = false
    private lateinit var problematicas1: List<Problematica1>
    private var positionCarousel: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_carousel)
        var introCarousel: CarouselView = introCarousel
        introCarousel.pageCount = imagesCarousel.size
        introCarousel.setImageListener { position, imageView ->
            imageView.setImageResource(imagesCarousel[position])
        }
        introCarousel.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                positionCarousel = position
                if(position === imagesCarousel.size - 1) {
                    btnNext.text = "Empezemos!"
                    lastImage = true
                }else {
                    btnNext.text = "Siguiente"
                    lastImage = false
                }
            }

            override fun onPageSelected(position: Int) {
            }
        })

        btnNext.setOnClickListener {
            if(lastImage){
                goToLogin()
            }else{
                introCarousel.setCurrentItem(positionCarousel + 1,true)
            }
        }

        btnSkip.setOnClickListener {
            goToLogin()
        }
    }

    fun goToLogin(){
        var intent: Intent = intent
        var bundle = intent.extras
        problematicas1 = bundle.getParcelableArrayList("menuProblematicas")
        //Log.d("token", Utilidades.getToken(this).access_token)
        if(Utilidades.getToken(this).access_token == "") {
            intent = Intent(this, InitialActivity::class.java)
        }else {
            intent = Intent(this, ProblemasPrincipalesActivity2::class.java)
        }
        bundle.putParcelableArrayList("menuProblematicas", problematicas1 as java.util.ArrayList<out Parcelable>)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}
