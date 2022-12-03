package com.recimaps.recimaps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.recimaps.recimaps.databinding.ActivityMapsBinding
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import kotlin.concurrent.schedule

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val dataBase = FirebaseFirestore.getInstance()
    private lateinit var email: String

    override fun onMapReady(googleMap: GoogleMap) {

        val bundle: Bundle? = intent.extras
        email = bundle?.getString("email").toString()

        mMap = googleMap
        mMap.setMinZoomPreference(11f)
        mMap.setMaxZoomPreference(20f)
        tosInt()
        enableLocation()
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomView.setOnItemSelectedListener {
            val profileInte = Intent(this, ProfileActivity::class.java)
            //val mapsInte = Intent(this, MapsActivity::class.java)
            val pointInte = Intent(this, AddInterestPointActivity::class.java).apply {
                putExtra("email", email)
            }

            when (it.itemId) {
                R.id.perfil -> startActivity(profileInte)
                R.id.publi -> {
                    val tos = mMap.cameraPosition.target
                    mMap.addMarker(
                        MarkerOptions()
                            .position(tos)
                            .draggable(true)
                    )
                    val center = tos.toString()
                    addCoords(center)
                    Timer("SettingUp", false).schedule(500) {
                        startActivity(pointInte)
                    }
                }
                else -> {
                }
            }
            true
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation() {
        if (!::mMap.isInitialized) return
        if (isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mMap.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                this, "Debes ajustar los permisos de localización",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    this, "Ve a los permisos de localización y aceptalos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::mMap.isInitialized) return
        if (!isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mMap.isMyLocationEnabled = false
            Toast.makeText(
                this, "Ve a los permisos de localización y aceptalos.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Ubicacion actual ", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(
            this, "Estas en ${p0.latitude}, ${p0.longitude}.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun tosInt() {

        val tos = LatLng(-33.4912422, -70.5935661)
        mMap.addMarker(MarkerOptions().position(tos).title("Punto limpio"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tos))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tos, 13f), 1, null)
        val tos1 = LatLng(-33.43788, -70.58087)
        mMap.addMarker(MarkerOptions().position(tos1).title("Recieco"))
        val tos2 = LatLng(-33.4656328, -70.6000689)
        mMap.addMarker(MarkerOptions().position(tos2).title("Circular"))
        val tos3 = LatLng(-33.4619722, -70.574565)
        mMap.addMarker(MarkerOptions().position(tos3).title("Sociedad puntos y tejidos SPA"))
        val tos4 = LatLng(-33.4824628, -70.6095227)
        mMap.addMarker(MarkerOptions().position(tos4).title("Punto verde"))
        val tos5 = LatLng(-33.4774616, -70.6034151)
        mMap.addMarker(MarkerOptions().position(tos5).title("ECOCITEX"))
        val tos6 = LatLng(-33.4551468, -70.592697)
        mMap.addMarker(MarkerOptions().position(tos6).title("Sin Envase"))
        val tos7 = LatLng(-33.4520164, -70.5939894)
        mMap.addMarker(MarkerOptions().position(tos7).title("Municipalidad de Ñuñoa"))
    }

    private fun addCoords(coord: String) {
        dataBase.collection("coordenadas").document(email).set(
            hashMapOf("coordenada" to coord)
        )
    }

}


