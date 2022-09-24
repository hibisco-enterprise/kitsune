package com.hibisco.kitsune.feature.ui.Login.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivityLoginBinding
import com.hibisco.kitsune.feature.StartKoin
import com.hibisco.kitsune.feature.ui.Login.ViewModel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_login)
        auth = Firebase.auth

        setActions()
    }

    fun setActions() {

    }

    override fun onStart() {
        super.onStart()
        StartKoin.initKoin(this)

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        // if(currentUser != null){  reload();  }
    }
}