package com.hibisco.kitsune.feature.ui.login.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivityConfirmDonationBinding
import com.hibisco.kitsune.databinding.ActivityLoginBinding
import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.network.model.Hospital
import com.hibisco.kitsune.feature.ui.base.MainActivity
import com.hibisco.kitsune.feature.ui.calendar.model.DateModel
import com.hibisco.kitsune.feature.ui.login.delegate.LoginDelegate
import com.hibisco.kitsune.feature.ui.login.viewModel.LoginViewModel
import com.hibisco.kitsune.feature.ui.map.view.ConfirmDonationActivity
import com.hibisco.kitsune.feature.ui.map.view.MapActivity
import com.hibisco.kitsune.feature.ui.signup.view.SignupActivity

class LoginActivity: AppCompatActivity(), LoginDelegate {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel
    private var milkString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = LoginViewModel(this)
        setContentView(binding.root)

        auth = Firebase.auth
        setActions()
    }


    override fun onStart() {
        super.onStart()
        checkIfIsLogged()
    }

    private fun checkIfIsLogged() {
        val sharedPreference = getSharedPreferences("USER_DATA", 0)
        val modelString: String? = sharedPreference.getString("userModelString","defaultUser")
        val milkString: String? = sharedPreference.getString("milkString","defaultMilk")

        if (modelString != null &&  milkString != null) {
            if (modelString == "defaultUser" || milkString == "defaultMilk") { return }
            val donator: Donator = gsonToDonator(modelString)
            binding.emailEt.setText(donator.user.email)
            binding.passwordEt.setText(milkString)

            viewModel.login(donator.user.email, milkString)
        }
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
        this.milkString = password
        return true
    }

    override fun loginSuccessful(response: Donator) {
        Toast.makeText(baseContext, response.toString(), Toast.LENGTH_LONG).show()

        val sharedPreference =  getSharedPreferences("USER_DATA", 0)
        var editor = sharedPreference.edit()

        if (milkString != null) {
            editor.putString("userModelString", this.donatorToGson(response))
            editor.putString("milkString", milkString)
        }

        editor.commit()

        val main = Intent(this, MainActivity::class.java)
        startActivity(main)
    }

    override fun loginFailed(error: String) {
        Toast.makeText(baseContext, "Login deu ruim", Toast.LENGTH_LONG).show()
    }

    fun donatorToGson(donator: Donator): String {
        val gson = Gson()
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonString: String = gson.toJson(donator)
        println(jsonString)

        val jsonStringPretty: String = gsonPretty.toJson(donator)
        println(jsonStringPretty)

        return jsonStringPretty
    }

    fun gsonToDonator(gsonString: String): Donator {
        val gson = Gson()
        val model: Donator = gson.fromJson(gsonString, Donator::class.java)
        println("> From JSON String:\n" + model)

        return model
    }
}