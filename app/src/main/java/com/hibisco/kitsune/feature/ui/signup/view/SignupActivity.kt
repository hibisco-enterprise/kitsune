package com.hibisco.kitsune.feature.ui.signup.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivityLoginBinding
import com.hibisco.kitsune.databinding.ActivitySignupBinding


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Spinner element
        // Spinner element
        val spinner = binding.bloodtypeSpinner

        // Spinner Drop down elements
        val bloodTypes: MutableList<String> = ArrayList()
        bloodTypes.add("O+")
        bloodTypes.add("O-")
        bloodTypes.add("A+")
        bloodTypes.add("A-")
        bloodTypes.add("B+")
        bloodTypes.add("B-")
        bloodTypes.add("AB+")
        bloodTypes.add("AB-")

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodTypes)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinner.adapter = dataAdapter

        binding.signupPgb.setProgress(1, false)
        binding.step1.visibility = View.VISIBLE
        binding.step2.visibility = View.GONE
        binding.step2.visibility = View.GONE

        binding.btnStep1.setOnClickListener {
            binding.signupPgb.setProgress(33, true)
            binding.step1.visibility = View.GONE
            binding.step2.visibility = View.VISIBLE
        }
        binding.btnStep2.setOnClickListener {
            binding.signupPgb.setProgress(67, true)
            binding.step2.visibility = View.GONE
            binding.step3.visibility = View.VISIBLE
        }
    }
}
