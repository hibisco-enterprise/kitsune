package com.hibisco.kitsune.feature.ui.map.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivityConfirmDonationBinding
import com.hibisco.kitsune.databinding.ActivityLoginBinding

class ConfirmDonationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmDonationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActions()
    }

    fun setActions() {
        binding.btnX.setOnClickListener {
            finish()
        }
    }
}