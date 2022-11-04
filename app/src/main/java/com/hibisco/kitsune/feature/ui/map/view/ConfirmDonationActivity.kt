package com.hibisco.kitsune.feature.ui.map.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivityConfirmDonationBinding
import com.hibisco.kitsune.databinding.ActivityLoginBinding
import com.hibisco.kitsune.feature.network.model.Hospital

class ConfirmDonationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmDonationBinding
    private lateinit var hospital: Hospital

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmDonationBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setActions()

        val hospitalString: String = intent.getStringExtra("hospital").toString()
        this.hospital = gsonToHospital(hospitalString)
        setTextsWithObject(hospital)
    }

    fun setActions() {
        binding.btnX.setOnClickListener {
            finish()
        }
    }

    fun setTextsWithObject(hospital: Hospital) {
        val title = hospital.user.name
        val subtitle = hospital.user.email
        val address = "${hospital.user.address.address}, ${hospital.user.address.number} - " +
                "${hospital.user.address.neighborhood}, ${hospital.user.address.city} - " +
                "${hospital.user.address.uf}, ${hospital.user.address.cep}"
        val phone = hospital.user.phone

        binding.tvTitle.setText(title)
        binding.tvSubtitle.setText(subtitle)
        binding.tvAddress.setText(address)
        binding.tvPhone.setText(phone)
    }

    fun gsonToHospital(hospitalJson: String): Hospital {
        val gson = Gson()

        val hospital: Hospital = gson.fromJson(hospitalJson, Hospital::class.java)
        println("> From JSON String:\n" + hospital)

        return hospital
    }
}