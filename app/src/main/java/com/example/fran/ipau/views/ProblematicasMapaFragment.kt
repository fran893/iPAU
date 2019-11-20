package com.example.fran.ipau.views

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import com.example.fran.ipau.R
import com.example.fran.ipau.models.Problematica2
import com.example.fran.ipau.models.Problematica3
import com.example.fran.ipau.models.ProblematicaLocation
import com.example.fran.ipau.utils.Parametros
import com.example.fran.ipau.utils.Utilidades
import com.example.fran.ipau.viewmodels.ProblematicasMapaFragmentViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_problematicas_mapa.*
import kotlinx.android.synthetic.main.view_alert_dialog_add_problematica.view.descripcionProblematica
import com.google.android.gms.maps.model.MarkerOptions as MarkerOptions1

class ProblematicasMapaFragment : Fragment(), OnMapReadyCallback {

    val KEY_TITLE: String = "Titulo"
    lateinit var mMap: GoogleMap
    lateinit var marcador: Marker
    var lat: Double = 0.0
    var lng: Double = 0.0
    lateinit var mensaje: String
    lateinit var direccion: String
    val PETICION_PERMISO_LOCALIZACION: Int = 101
    lateinit var problematica2: Problematica2
    var problematica3: Problematica3? = null
    lateinit var mMapView: MapView
    lateinit var mView: View
    lateinit var viewModel: ProblematicasMapaFragmentViewModel
    lateinit var problematicaDialogAlert: AlertDialog
    lateinit var errorProblematicaDialogAlert: AlertDialog
    lateinit var viewAlertDialog: View
    lateinit var viewAlertDialogErrorProblematica : View
    var latLngMarker: LatLng? = null //longitud y latitud que se obtienen cuando el usuario toca en el mapa
    lateinit var hashMapMarker: HashMap<String, Marker>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.activity_problematicas_mapa, container, false)
        viewModel = ViewModelProviders.of(this)[ProblematicasMapaFragmentViewModel::class.java]
        viewAlertDialog = LayoutInflater.from(this.activity).inflate(R.layout.view_alert_dialog_add_problematica, null)
        problematicaDialogAlert = AlertDialog.Builder(this.activity!!)
                .setCancelable(true)
                .setTitle("Agregar Problemática")
                .setView(viewAlertDialog)
                .setPositiveButton("Si", DialogInterface.OnClickListener {_, _ ->
                    guardarMarcadorProblematica()
                })
                .create()
        viewAlertDialogErrorProblematica = LayoutInflater.from(this.activity).inflate(R.layout.view_alert_dialog_error_problematica,null)
        errorProblematicaDialogAlert = AlertDialog.Builder(this.activity!!)
                .setCancelable(true)
                .setTitle("Error")
                .setIcon(R.drawable.ic_error_24dp)
                .setView(R.layout.view_alert_dialog_error_problematica)
                .create()


        return mView
    }

    override fun onResume() {
        mView.requestFocus()
        super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = mView.findViewById<View>(R.id.map) as MapView
        mMapView.onCreate(null)
        mMapView.onResume()
        hashMapMarker = HashMap()
        location.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                //var geocoder = Geocoder(activity)
                //var addressList: List<Address>? = null

                var loc: String = location.query.toString()
                Log.d("queryString ",loc)
                Log.d("queryString parameter ",query )
                var addressList: List<Address>? = null
                if(loc != ""){
                    var geocoder = Geocoder(activity)
                    addressList = geocoder.getFromLocationName(loc,1)
                    if(addressList.isNotEmpty()) {
                        var address = addressList[0]
                        var latLng = LatLng(address.latitude, address.longitude)
                        var searchPlace = com.google.android.gms.maps.model.MarkerOptions()
                        searchPlace.title(loc)
                        searchPlace.position(latLng)
                        var searchPlaceMarker = mMap.addMarker(searchPlace)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F))
                        var removeMarker = hashMapMarker["searchPlace"]
                        removeMarker?.remove()
                        hashMapMarker["searchPlace"] = searchPlaceMarker
                    }else{
                        Toast.makeText(activity,"Lugar No encontrado", Toast.LENGTH_LONG)
                    }
                }
                return false
            }
        })
        mMapView.getMapAsync(this)

    }

    object myInstace{
        fun newInstance(param1: String): ProblematicasMapaFragment{
            var fragment: ProblematicasMapaFragment = ProblematicasMapaFragment()
            var args: Bundle = Bundle()
            args.putString("Titulo", param1)
            fragment.arguments = args
            Log.d("myinstance","entra a crear fragment mapa")
            return fragment
        }
    }

    fun setProblematicas(prob2: Problematica2, prob3: Problematica3){
        mMap.clear()
        this.problematica2 = prob2
        this.problematica3 = prob3
        getAllProblematica3()
    }

    fun ubicacionPorDefecto(googleMap: GoogleMap?){
        var coordenadas: LatLng = LatLng(Parametros.latPlazaSantaRosa,Parametros.lngPlazaSantaRosa)
        var defaultLocation: CameraUpdate = CameraUpdateFactory.newLatLngZoom(coordenadas,16F)
        googleMap?.animateCamera(defaultLocation)
        mMap = googleMap!!
    }

    fun actualizarUbicacion(loc: Location){
        lat = loc.latitude
        lng = loc.longitude
        agregarMarcador(lat,lng)
    }

    fun agregarMarcador(lat: Double, lng: Double){
        var coordenadas: LatLng = LatLng(lat,lng)
        var miUbicacion: CameraUpdate = CameraUpdateFactory.newLatLngZoom(coordenadas,16F)
        marcador.remove()
        marcador = mMap.addMarker(MarkerOptions1().position(coordenadas).title(direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapnavigation)))
        mMap.animateCamera(miUbicacion)
    }

    internal var locListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //actualizarUbicacion(location);
            // setLocation(location);
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }

        override fun onProviderEnabled(provider: String) {
            mensaje = "GPS ACTIVADO"
            locationStart()
            setMensaje()

        }

        override fun onProviderDisabled(provider: String) {
            mensaje = "GPS DESACTIVADO"
            locationStart()
            setMensaje()
        }
    }

    fun setMensaje(){
        Toast.makeText(activity,mensaje,Toast.LENGTH_LONG).show()
    }

    fun locationStart(){
        var mLocManager: LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gpsEnabled){
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
        googleMap?.setOnMapClickListener {
            viewAlertDialog.descripcionProblematica.setText("")
            latLngMarker = it
            problematicaDialogAlert.show()
        }
        ubicacionPorDefecto(googleMap)
    }

    fun addMarkersInit(problematicasLocations: List<ProblematicaLocation>){
        var markers: ArrayList<MarkerOptions1> = viewModel.addMarkersInit(problematicasLocations)
        markers.forEach { marker ->
            mMap.addMarker(marker)
        }
    }

    fun getAllProblematica3(){
        problematica3?.idProblematica3?.let { viewModel.getInitProblematicasLocation(it) }
        viewModel.getInitProblematicaLocation().observe(this,Observer<List<ProblematicaLocation>>{ locationsInit ->
            if (locationsInit != null) {
                addMarkersInit(locationsInit)
            }
        })
    }

    fun getMarkerOption(prob2: Int): MarkerOptions1{
        var markerOption: MarkerOptions1 = MarkerOptions1()
        //var icon: Bitmap = Bitmap.createBitmap(R.drawable.agua_potable_prob)
        when(prob2){
            Parametros.subProbAguaPotable -> {
                var drawable: Drawable? = ContextCompat.getDrawable(this!!.context!!, R.drawable.agua_potable_prob)
                markerOption.icon(Utilidades.getMarkerIconFromDrawable(drawable!!))
            }
        }
        return markerOption
    }

    fun guardarMarcadorProblematica(){
        if(problematica3 != null) {
            var pl: ProblematicaLocation = ProblematicaLocation()
            pl.descripcion = "agregada desde app"
            pl.problematica3 = problematica3
            pl.latitud = latLngMarker!!.latitude
            pl.longitud = latLngMarker!!.longitude
            var descripcion: String = viewAlertDialog.descripcionProblematica.text.toString()
            pl.descripcion = descripcion
            viewModel.guardarProblematicaLocation(pl)
            viewModel.getSaveLocationLiveData().observe(this, Observer<ProblematicaLocation> { location ->
                var markerOption: MarkerOptions1 = getMarkerOption(problematica2.idProblematica2)
                markerOption.position(latLngMarker!!)
                markerOption.title(pl.descripcion)
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLngMarker))
                mMap.addMarker(markerOption)
            })
        }else{
            errorProblematicaDialogAlert.show()
        }
    }
}
