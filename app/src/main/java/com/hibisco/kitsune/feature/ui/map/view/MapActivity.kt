package com.hibisco.kitsune.feature.ui.map.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hibisco.kitsune.databinding.ActivityLoginBinding
import com.hibisco.kitsune.databinding.ActivityMapBinding
import com.hibisco.kitsune.feature.ui.login.viewModel.LoginViewModel
import com.hibisco.kitsune.feature.ui.map.delegate.MapDelegate
import com.hibisco.kitsune.feature.ui.map.viewModel.MapViewModel

class MapActivity: AppCompatActivity(), MapDelegate {
    private lateinit var binding: ActivityMapBinding
    lateinit var viewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        viewModel = MapViewModel(this)
        setContentView(binding.root)
        setActions()
    }

    fun setActions() {

    }
}