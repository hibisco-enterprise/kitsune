package com.hibisco.kitsune.feature.ui.map.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivityMapBinding
import com.hibisco.kitsune.feature.network.model.Hospital
import com.hibisco.kitsune.feature.ui.map.delegate.MapDelegate
import com.hibisco.kitsune.feature.ui.map.viewModel.MapViewModel

class MapKitsuneFragment: Fragment(R.layout.activity_map), MapDelegate {
    lateinit var viewModel: MapViewModel
    lateinit var hospitals: List<Hospital>
    private lateinit var binding: ActivityMapBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       //binding.tituloFragment.text = arguments?.getString("EMAIL")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMapBinding.inflate(inflater)

        viewModel = MapViewModel(this)
        setActions()
        viewModel.getHospitals()

        return binding.root
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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            addMarkers(googleMap)
        }
    }

    fun setActions() {

    }

    override fun getHospitalsFailed(error: String) {
        // Toast.makeText(this, "Erro no carregamento", Toast.LENGTH_SHORT)
    }
}

