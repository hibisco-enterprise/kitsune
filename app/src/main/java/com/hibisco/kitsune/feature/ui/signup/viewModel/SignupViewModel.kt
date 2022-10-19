package com.hibisco.kitsune.feature.ui.signup.viewModel

import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.network.model.DonatorRequest
import com.hibisco.kitsune.feature.network.request.LoginRequest
import com.hibisco.kitsune.feature.ui.base.KitsuneViewModel
import com.hibisco.kitsune.feature.ui.signup.delegate.SignupDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupViewModel(delegate: SignupDelegate): KitsuneViewModel() {
    private val retrofit = RetroFitInstance.getRetrofitKitsune()
    val delegate: SignupDelegate = delegate

    fun signup(donator: DonatorRequest){
        retrofit.register(donator).enqueue(
            object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    delegate.registerSuccessful()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    delegate.registerFailed(t.message.toString())
                }

            }
        )
    }
}