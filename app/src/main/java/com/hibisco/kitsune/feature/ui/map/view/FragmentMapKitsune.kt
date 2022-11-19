package com.hibisco.kitsune.feature.ui.map.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CalendarView
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
import com.hibisco.kitsune.feature.network.model.BloodTypeStock
import com.hibisco.kitsune.feature.network.model.Hospital
import com.hibisco.kitsune.feature.ui.base.MainActivity
import com.hibisco.kitsune.feature.ui.calendar.view.CalendarActivity
import com.hibisco.kitsune.feature.ui.map.delegate.MapDelegate
import com.hibisco.kitsune.feature.ui.map.viewModel.MapViewModel
import kotlin.math.roundToInt


class FragmentMapKitsune: Fragment(R.layout.activity_map), MapDelegate {
    lateinit var viewModel: MapViewModel
    lateinit var hospitals: List<Hospital>
    var sheetBinding: ActivityConfirmDonationBinding? = null
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

            sheetBinding = ActivityConfirmDonationBinding.inflate(layoutInflater,
                    null,
                    false)
            dialog.setContentView(sheetBinding!!.root)
            sheetBinding!!.btnX.setOnClickListener {
                dialog.dismiss()
                sheetBinding = null
            }

            sheetBinding!!.btnConfirm.setOnClickListener {
                activity?.let {
                    val calendarActivity = Intent(it, CalendarActivity::class.java)
                    startActivity(calendarActivity)
                }

            }

            val title = hospital.user.name
            val subtitle = hospital.user.email
            val address = "${hospital.user.address.address}, " +
                    "${hospital.user.address.number} - " +
                    "${hospital.user.address.neighborhood}, ${hospital.user.address.city} - " +
                    "${hospital.user.address.uf}, ${hospital.user.address.cep}"
            val phone = hospital.user.phone

            sheetBinding!!.tvTitle.setText(title)
            sheetBinding!!.tvSubtitle.setText(subtitle)
            sheetBinding!!.tvAddress.setText(address)
            sheetBinding!!.tvPhone.setText(phone)
            viewModel.getBloodStock(hospital.idHospital)
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

    override fun bloodStockResponse(bloodStock: List<BloodTypeStock>) {
        var oPositive: Double? = null
        var oNegative: Double? = null
        var aPositive: Double? = null
        var aNegative: Double? = null
        var bPositive: Double? = null
        var bNegative: Double? = null
        var abPositive: Double? = null
        var abNegative: Double? = null

        bloodStock.forEach { bloodStock ->
            when (bloodStock.bloodType) {
                "O+" -> oPositive = bloodStock.percentage
                "O-" -> oNegative = bloodStock.percentage
                "A+" -> aPositive = bloodStock.percentage
                "A-" -> aNegative = bloodStock.percentage
                "B+" -> bPositive = bloodStock.percentage
                "B-" -> bNegative = bloodStock.percentage
                "AB+" -> abPositive = bloodStock.percentage
                else -> abNegative = bloodStock.percentage
            }
        }

        sheetBinding!!.tvPctOPos.text = "${oPositive?.roundToInt().toString()}%"
        sheetBinding!!.tvPctONeg.text = "${oNegative?.roundToInt().toString()}%"
        sheetBinding!!.tvPctAPos.text = "${aPositive?.roundToInt().toString()}%"
        sheetBinding!!.tvPctANeg.text = "${aNegative?.roundToInt().toString()}%"
        sheetBinding!!.tvPctBPos.text = "${bPositive?.roundToInt().toString()}%"
        sheetBinding!!.tvPctBNeg.text = "${bNegative?.roundToInt().toString()}%"
        sheetBinding!!.tvPctAbPos.text = "${abPositive?.roundToInt().toString()}%"
        sheetBinding!!.tvPctAbNeg.text = "${abNegative?.roundToInt().toString()}%"

        sheetBinding!!.imgOPos.setImageResource(getImageByBloodPercentage(oPositive))
        sheetBinding!!.imgONeg.setImageResource(getImageByBloodPercentage(oNegative))
        sheetBinding!!.imgAPos.setImageResource(getImageByBloodPercentage(aPositive))
        sheetBinding!!.imgANeg.setImageResource(getImageByBloodPercentage(aNegative))
        sheetBinding!!.imgBPos.setImageResource(getImageByBloodPercentage(bPositive))
        sheetBinding!!.imgBNeg.setImageResource(getImageByBloodPercentage(bNegative))
        sheetBinding!!.imgAbPos.setImageResource(getImageByBloodPercentage(abPositive))
        sheetBinding!!.imgAbNeg.setImageResource(getImageByBloodPercentage(abNegative))
    }

    fun getImageByBloodPercentage(percentage: Double?): Int {
        val empty: Int = R.drawable.estoque_vazio
        val half: Int = R.drawable.estoque_medio
        val full: Int = R.drawable.estoque_cheio

        if (percentage == null) { return empty }
        if (percentage < 30.0) {
            return R.drawable.estoque_vazio
        } else if (percentage < 60.0) {
            return half
        } else {
            return full
        }
    }

    override fun getBloodStockFailed(error: String) {
        TODO("Not yet implemented")
    }
}

