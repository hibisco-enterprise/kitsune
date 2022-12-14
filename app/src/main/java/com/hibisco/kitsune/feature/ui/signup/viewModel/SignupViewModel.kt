package com.hibisco.kitsune.feature.ui.signup.viewModel

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.hibisco.kitsune.R
import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.network.model.DonatorRequest
import com.hibisco.kitsune.feature.network.request.LoginRequest
import com.hibisco.kitsune.feature.ui.base.KitsuneViewModel
import com.hibisco.kitsune.feature.ui.signup.delegate.SignupDelegate
import com.hibisco.kitsune.feature.ui.signup.view.SignupActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupViewModel(val delegate: SignupDelegate): KitsuneViewModel() {
    private val retrofit = RetroFitInstance.getRetrofitKitsune()

    fun signup(donator: DonatorRequest){
        retrofit.register(donator).enqueue(
            object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    println(response)
                    if (response.code() == 201){
                        println(response)
                        println("Criado")
                        delegate.registerSuccessful()
                    } else {
                        delegate.errorOnRegister(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                    println(t.message.toString())
                    delegate.registerFailed(t.message.toString())
                }

            }
        )
    }
}