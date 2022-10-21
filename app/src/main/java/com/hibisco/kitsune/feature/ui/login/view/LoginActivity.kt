package com.hibisco.kitsune.feature.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hibisco.kitsune.databinding.ActivityLoginBinding
import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.ui.login.delegate.LoginDelegate
import com.hibisco.kitsune.feature.ui.login.viewModel.LoginViewModel
import com.hibisco.kitsune.feature.ui.map.view.MapActivity
import com.hibisco.kitsune.feature.ui.signup.view.SignupActivity

class LoginActivity: AppCompatActivity(), LoginDelegate {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = LoginViewModel(this)
        setContentView(binding.root)

        auth = Firebase.auth
        setActions()
    }

    private fun setActions() {
        binding.btnLogin.setOnClickListener{
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            if (checkFields(email, password)) {
                viewModel.login(email, password)
            }
        }

        binding.tvCreateAccount.setOnClickListener{
            val signup = Intent(this, SignupActivity::class.java)
            startActivity(signup)
        }
    }

    private fun checkFields(login: String?, password: String?): Boolean{
        if (login == null || login == "" || password == null || password == "") {
            Toast.makeText(applicationContext, "Preencha todos os campos.", Toast.LENGTH_LONG)
            return false
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
    }

    override fun loginSuccessful(response: Donator) {
        val map = Intent(this, MapActivity::class.java)
       // startActivity(map)
        Toast.makeText(baseContext, response.toString(), Toast.LENGTH_LONG).show()
    }

    override fun loginFailed(error: String) {
        Toast.makeText(baseContext, "Login deu ruim", Toast.LENGTH_LONG).show()
    }
}