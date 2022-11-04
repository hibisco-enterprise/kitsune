package com.hibisco.kitsune.feature.ui.map.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hibisco.kitsune.R
import com.hibisco.kitsune.feature.network.model.Hospital
import com.hibisco.kitsune.feature.ui.base.MainActivity
import com.hibisco.kitsune.feature.ui.map.delegate.MapDelegate
import com.hibisco.kitsune.feature.ui.map.viewModel.MapViewModel


class FragmentMapKitsune: Fragment(R.layout.activity_map), MapDelegate {
    lateinit var viewModel: MapViewModel
    lateinit var hospitals: List<Hospital>
    // private lateinit var binding: FragmentMapKitsuneBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       //binding.tituloFragment.text = arguments?.getString("EMAIL")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding = ActivityMapBinding.inflate(inflater)
        viewModel = MapViewModel(this)
        viewModel.getHospitals()
        setActions()

        // return binding.root
        return inflater.inflate(R.layout.activity_map, container, false)
    }

    private fun addMarkers(googleMap: GoogleMap) {
        var count = 0
        hospitals.forEach { hospital ->

            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(hospital.user.name)
                    .snippet(hospital.user.address.address)
                    .position(LatLng(hospital.user.address.latitude, hospital.user.address.longitude))
            )
            if (marker != null) { marker.tag = count }
            count++
            googleMap.setOnMarkerClickListener {
                onMarkerClick(hospital, marker)
            }

        }
    }

    fun onMarkerClick(hospital: Hospital?, marker: Marker?): Boolean {
        if (marker == null || hospital == null) {
            return false
        }

        activity?.let{
            val intent = Intent (it, ConfirmDonationActivity::class.java)
            intent.putExtra("hospitalJson", hospitalToGson(hospital))
            it.startActivity(intent)
        }

        return true
    }

    override fun hospitalsResponse(hospitals: List<Hospital>) {
        this.hospitals = hospitals
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            addMarkers(googleMap)

            googleMap.setOnMapLoadedCallback {
                val bounds = LatLngBounds.builder()
                hospitals.forEach { bounds.include(LatLng(it.user.address.latitude, it.user.address.longitude)) }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
            }
        }
    }

    fun setActions() {

    }

    fun hospitalToGson(hospital: Hospital?): String {
        val gson = Gson()
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonHospital: String = gson.toJson(hospital)
        println(jsonHospital)

        val jsonHospitalPretty: String = gsonPretty.toJson(hospital)
        println(jsonHospitalPretty)

        return jsonHospitalPretty
    }

    override fun getHospitalsFailed(error: String) {
        // Toast.makeText(this, "Erro no carregamento", Toast.LENGTH_SHORT)
    }
}

