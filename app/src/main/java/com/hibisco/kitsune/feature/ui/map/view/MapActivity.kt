package com.hibisco.kitsune.feature.ui.map.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivityLoginBinding
import com.hibisco.kitsune.databinding.ActivityMapBinding
import com.hibisco.kitsune.feature.network.model.BloodTypeStock
import com.hibisco.kitsune.feature.network.model.Hospital
import com.hibisco.kitsune.feature.ui.login.viewModel.LoginViewModel
import com.hibisco.kitsune.feature.ui.map.delegate.MapDelegate
import com.hibisco.kitsune.feature.ui.map.viewModel.MapViewModel

class MapActivity: AppCompatActivity(), MapDelegate {
    private lateinit var binding: ActivityMapBinding
    lateinit var viewModel: MapViewModel
    lateinit var hospitals: List<Hospital>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        viewModel = MapViewModel(this)
        setContentView(binding.root)
        setActions()
        viewModel.getHospitals()
    }

    private fun addMarkers(googleMap: GoogleMap) {
        hospitals.forEach { hospital ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(hospital.user.name)
                    .snippet(hospital.user.address.address)
                    .position(LatLng(hospital.user.address.latitude, hospital.user.address.longitude))
            )
        }
    }
    override fun hospitalsResponse(hospitals: List<Hospital>) {
        this.hospitals = hospitals
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            addMarkers(googleMap)
        }

    }

    fun setActions() {

    }

    override fun getHospitalsFailed(error: String) {
        Toast.makeText(applicationContext, "Erro no carregamento", Toast.LENGTH_SHORT)
    }

    override fun bloodStockResponse(bloodStock: List<BloodTypeStock>) {
        TODO("Not yet implemented")
    }

    override fun getBloodStockFailed(error: String) {
        TODO("Not yet implemented")
    }
}