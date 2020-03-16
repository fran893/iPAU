package com.example.fran.ipau.views

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SearchView
import android.widget.Toast
import com.example.fran.ipau.R
import com.example.fran.ipau.models.Problematica2
import com.example.fran.ipau.models.Problematica3
import com.example.fran.ipau.models.ProblematicaLocation
import com.example.fran.ipau.utils.Parametros
import com.example.fran.ipau.utils.Utilidades
import com.example.fran.ipau.viewmodels.ProblematicasMapaFragmentViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_problematicas_mapa.*
import kotlinx.android.synthetic.main.activity_problematicas_mapa.view.*
import kotlinx.android.synthetic.main.desc_problematica_location.view.*
import kotlinx.android.synthetic.main.probress_bar_loading.*
import kotlinx.android.synthetic.main.probress_bar_loading.view.*
import kotlinx.android.synthetic.main.view_alert_dialog_add_problematica.view.descripcionProblematica
import kotlinx.android.synthetic.main.view_alert_dialog_error_problematica.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import com.google.android.gms.maps.model.MarkerOptions as MarkerOptions1

class ProblematicasMapaFragment  : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    val KEY_TITLE: String = "Titulo"
    private lateinit var mMap: GoogleMap
    private lateinit var marcador: Marker
    private lateinit var problematica2: Problematica2
    private lateinit var problematicaLocation: ProblematicaLocation
    private var problematica3: Problematica3? = null
    private lateinit var mMapView: MapView
    private lateinit var mView: View
    private lateinit var viewModel: ProblematicasMapaFragmentViewModel
    private lateinit var problematicaDialogAlert: AlertDialog
    private lateinit var errorProblematicaDialogAlert: AlertDialog
    private lateinit var activateGps: AlertDialog
    private lateinit var alertaPermisosAlertDialog: AlertDialog
    private lateinit var alertDialogMarkerProb: AlertDialog
    private lateinit var viewAlertDialog: View
    private lateinit var viewAlertDialogErrorProblematica : View
    private lateinit var alertDialogSelectLayoutMap: AlertDialog
    private lateinit var viewAlertDialogSelectLayoutMap: View
    private lateinit var viewDescProbLocation: View
    private var latLngMarker: LatLng? = null //longitud y latitud que se obtienen cuando el usuario toca en el mapa
    private lateinit var hashMapMarker: HashMap<String, Marker>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var alertDialogNoInternet: AlertDialog
    private var guardarProblematica: Boolean = true
    private lateinit var progressLoadinSuccessDialog: AlertDialog
    private lateinit var viewAlertPorgressLoadingSuccess: View
    private lateinit var viewAlertDialogError: View

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
        viewAlertDialogError = LayoutInflater.from(this.activity).inflate(R.layout.view_alert_dialog_error_problematica, null)
        errorProblematicaDialogAlert = AlertDialog.Builder(this.activity!!)
                .setCancelable(true)
                .setTitle("Error")
                .setIcon(R.drawable.ic_error_24dp)
                .setView(viewAlertDialogError)
                .create()
        alertDialogSelectLayoutMap = AlertDialog.Builder(this.activity!!)
                .setCancelable(true)
                .setTitle("Tipo de Mapa")
                //.setView(R.layout.view_select_layout_map)
                .create()
        alertaPermisosAlertDialog = AlertDialog.Builder(this.activity!!)
                .setCancelable(true)
                .setTitle("Permisos de Ubicación iPau")
                .setMessage("Para que la aplicación pueda mostrar su ubicacion actual" +
                        " necesita que se le concedan los permisos de Ubicación. Para eso" +
                        " ingrese a las opciones de permisos de la app")
                .setPositiveButton("Ir a permisos de app", DialogInterface.OnClickListener{_,_ ->
                    var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    var uri: Uri = Uri.fromParts("package", activity!!.packageName,null)
                    intent.data = uri
                    activity!!.startActivity(intent)
                })
                .create()
        alertDialogNoInternet = AlertDialog.Builder(this.activity!!)
                .setCancelable(true)
                .setTitle("Sin Internet")
                .setMessage("Debe haber conexión a internet")
                .create()
        viewAlertPorgressLoadingSuccess = LayoutInflater.from(this.activity).inflate(R.layout.probress_bar_loading, null)
        progressLoadinSuccessDialog = AlertDialog.Builder(this.activity!!)
                .setCancelable(false)
                .setView(viewAlertPorgressLoadingSuccess)
                .create()
        val rxPermissions = RxPermissions(this)
        mView.setUbicacion.setOnClickListener {
            Log.d("get Location btn","busca location")
            rxPermissions
                    .requestEach(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe { permission ->
                        if (permission.granted) {
                            Log.d("ACEPTADOS", "PERMISOS ACEPTADOS!!! =D")
                            getLocation()
                        }else if (permission.shouldShowRequestPermissionRationale) {
                            Log.d("DENEGADO", "PERMISOS NO ACEPTADOS D:")
                        }else {
                            alertaPermisosAlertDialog.show()
                        }
                    }
        }
        viewDescProbLocation = LayoutInflater.from(this.activity).inflate(R.layout.desc_problematica_location,null)
        alertDialogMarkerProb = AlertDialog.Builder(this.activity!!)
                .setCancelable(true)
                .setTitle("Problematica ya localizada")
                .setView(viewDescProbLocation)
                .setPositiveButton("Si", DialogInterface.OnClickListener{_,_ ->
                    addCountProbLocation()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener{_,_ ->

                })
                .create()
        viewAlertDialogSelectLayoutMap = LayoutInflater.from(this.activity).inflate(R.layout.view_select_layout_map,null)
        mView.setLayout.setOnClickListener {
            alertDialogSelectLayoutMap.show()

        }

        alertDialogSelectLayoutMap.setView(viewAlertDialogSelectLayoutMap)
        var rdGroup = viewAlertDialogSelectLayoutMap.findViewById<RadioGroup>(R.id.selectLayout)
        var tipoSatelite = viewAlertDialogSelectLayoutMap.findViewById<RadioButton>(R.id.tipoSatelite)
        var tipoPredeterminado = viewAlertDialogSelectLayoutMap.findViewById<RadioButton>(R.id.tipoPredeterminado)
        tipoSatelite.isChecked = true
        rdGroup.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == tipoSatelite.id)
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            else if(checkedId == tipoPredeterminado.id)
                mMap.mapType =  GoogleMap.MAP_TYPE_NORMAL
        }
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
                var loc: String = location.query.toString()
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

    @SuppressLint("MissingPermission")
    fun getLocation(){
       var lm: LocationManager = this!!.context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        var networkEnabled: Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        Log.d("gpsEnabled","pgs activado?? "+gpsEnabled)
        Log.d("isOnline()","isOnline()?? "+isOnline())
        if (!gpsEnabled || !isOnline()){
            Log.d("Entra**** ","entra a if sin conexion")
            if(!gpsEnabled) {
                activateGps = AlertDialog.Builder(this.activity!!)
                        .setCancelable(true)
                        .setTitle("Habilitar Ubicación")
                        .setPositiveButton("Opciones", DialogInterface.OnClickListener { _, _ ->
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        })
                        .create()
                activateGps.show()
            }
            if(!isOnline()){
                alertDialogNoInternet.show()
            }
        }else{

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this!!.activity!!)
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            agregarMarcador(location.latitude, location.longitude)
                        }else{
                            var lastKnowLocation = getLastKnownLocation(lm)
                            if(lastKnowLocation != null)
                                agregarMarcador(lastKnowLocation.latitude, lastKnowLocation.longitude)

                        }
                    }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(mLocation: LocationManager): Location? {
        var bestLocation: Location? = null
        var providers: List<String> = mLocation.getProviders(true)
        for (provider: String in providers) {
            var l = mLocation.getLastKnownLocation(provider)
            if (l == null)
                continue
            else {
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    bestLocation = l
                }
            }
        }
        return bestLocation
    }

    fun isOnline(): Boolean {
        val connMgr = this!!.context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
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
        if(isOnline())
            getAllProblematica3()
        else
            alertDialogNoInternet.show()
    }

    fun ubicacionPorDefecto(googleMap: GoogleMap?){
        var coordenadas: LatLng = LatLng(Parametros.latPlazaSantaRosa,Parametros.lngPlazaSantaRosa)
        var defaultLocation: CameraUpdate = CameraUpdateFactory.newLatLngZoom(coordenadas,16F)
        googleMap?.animateCamera(defaultLocation)
        mMap = googleMap!!
    }

    fun agregarMarcador(lat: Double, lng: Double){
        var coordenadas: LatLng = LatLng(lat,lng)
        var miUbicacion: CameraUpdate = CameraUpdateFactory.newLatLngZoom(coordenadas,16F)
        marcador = mMap.addMarker(MarkerOptions1().position(coordenadas).title("Mi ubicación actual"))
        mMap.animateCamera(miUbicacion)
    }

    internal var locListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onProviderDisabled(provider: String) {
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.setOnMarkerClickListener(this)
        googleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
        googleMap?.setOnMapClickListener {
            if(isOnline()) {
                if (guardarProblematica) {
                    viewAlertDialog.descripcionProblematica.setText("")
                    latLngMarker = it
                    problematicaDialogAlert.show()
                }
            }else
                alertDialogNoInternet.show()
        }
        ubicacionPorDefecto(googleMap)
        mView.isClickable = false
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        /*alertDialogMarkerProb = AlertDialog.Builder(this.activity!!)
                .setCancelable(true)
                .setTitle("Titulo")
                .setMessage("Mensaje")
                .create()
        alertDialogMarkerProb.show()*/
        //to do
        //llamar a metodo que busca problematica location
        var latMarker: Double = marker!!.position.latitude
        var lngMarker: Double = marker!!.position.longitude
        viewModel.getLocation(latMarker, lngMarker)
        viewModel.getProblematicaLocationLatLngLive().observe(this ,Observer<ProblematicaLocation> {location ->
            problematicaLocation = location!!
            viewDescProbLocation.descProblematica.text = problematicaLocation.descripcion
            viewDescProbLocation.countMarker.text = problematicaLocation.cantVecesMarcada.toString()
            alertDialogMarkerProb.show()
        })

        return true
    }

    fun addCountProbLocation(){
        viewModel.updateMarkerCountLocProb(problematicaLocation.idProblematicaLocation.toLong())
        viewModel.getUpdateCountProbLocationLive().observe(this, Observer {

        })
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
            var descripcion: String = viewAlertDialog.descripcionProblematica.text.toString()
            if(descripcion.length < 70){
                progressLoadinSuccessDialog.show()
                progressLoadinSuccessDialog.window.setLayout(650,400)
                progressLoadinSuccessDialog.progressBarLoading.visibility = View.VISIBLE
                progressLoadinSuccessDialog.checkICon.visibility = View.GONE
                progressLoadinSuccessDialog.btnOk.visibility = View.GONE
                viewAlertPorgressLoadingSuccess.textLoadModal.text = "Agregando Problemática"
                guardarProblematica = false
                var pl = ProblematicaLocation()
                pl.problematica3 = problematica3
                pl.latitud = latLngMarker!!.latitude
                pl.longitud = latLngMarker!!.longitude
                pl.descripcion = descripcion
                viewModel.guardarProblematicaLocation(pl)
                viewModel.getSaveLocationLiveData().observe(this, Observer<ProblematicaLocation> { location ->
                    var markerOption: MarkerOptions1 = getMarkerOption(problematica2.idProblematica2)
                    markerOption.position(latLngMarker!!)
                    markerOption.title(pl.descripcion)
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLngMarker))
                    viewAlertPorgressLoadingSuccess.progressBarLoading.visibility = View.INVISIBLE
                    progressLoadinSuccessDialog.checkICon.visibility = View.VISIBLE
                    progressLoadinSuccessDialog.btnOk.visibility = View.VISIBLE
                    progressLoadinSuccessDialog.btnOk.setOnClickListener {
                        progressLoadinSuccessDialog.dismiss()
                    }
                    viewAlertPorgressLoadingSuccess.textLoadModal.text = "Problématica agregada con éxito"
                    mMap.addMarker(markerOption)
                    guardarProblematica = true
                })
            }else{
                viewAlertDialogError.tvError.text = "La máxima cantidad de cáracteres para la descripción es de 70."
                errorProblematicaDialogAlert.show()
            }
        }else{
            errorProblematicaDialogAlert.show()
        }
    }
}
