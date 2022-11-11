package com.hibisco.kitsune.feature.ui.map.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivityConfirmDonationBinding
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
            googleMap.setOnMarkerClickListener {
                onMarkerClick(marker, count++)
            }

        }
    }

    fun onMarkerClick(marker: Marker?, index: Int): Boolean {
        if (marker == null || index > hospitals.size ) { return false }
        val hospital: Hospital = hospitals.get(index)
        showDialogOne(hospital)
        return true
    }

    fun showDialogOne(hospital: Hospital) {
        activity?.let{
            val dialog = BottomSheetDialog(it).apply {
                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE )
            }

            val sheetBinding: ActivityConfirmDonationBinding =
                ActivityConfirmDonationBinding.inflate(layoutInflater,
                    null,
                    false)
            dialog.setContentView(sheetBinding.root)
            sheetBinding.btnX.setOnClickListener {
                dialog.dismiss()
            }

            val title = hospital.user.name
            val subtitle = hospital.user.email
            val address = "${hospital.user.address.address}, " +
                    "${hospital.user.address.number} - " +
                    "${hospital.user.address.neighborhood}, ${hospital.user.address.city} - " +
                    "${hospital.user.address.uf}, ${hospital.user.address.cep}"
            val phone = hospital.user.phone

            sheetBinding.tvTitle.setText(title)
            sheetBinding.tvSubtitle.setText(subtitle)
            sheetBinding.tvAddress.setText(address)
            sheetBinding.tvPhone.setText(phone)
            dialog.show()
        }
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
        activity?.let {
            Toast.makeText(it, "Erro no carregamento", Toast.LENGTH_SHORT)
        }
    }
}

