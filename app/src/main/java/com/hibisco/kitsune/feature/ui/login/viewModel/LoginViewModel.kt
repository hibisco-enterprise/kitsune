package com.hibisco.kitsune.feature.ui.login.viewModel

import androidx.lifecycle.MutableLiveData
import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.stateFlow.StateFlow
import com.hibisco.kitsune.feature.ui.login.delegate.LoginDelegate
import com.hibisco.kitsune.feature.ui.base.KitsuneViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(delegate: LoginDelegate): KitsuneViewModel() {
    private val retrofit = RetroFitInstance.getRetrofit()
    val delegate: LoginDelegate = delegate

    fun testHospitalEndPoint() {
        retrofit.getHospitals().enqueue(
            object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

    fun login(email: String, password: String) {
        retrofit.login(email, password).enqueue(
            object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    delegate.loginSuccessful()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    delegate.loginFailed()
                }
            }
        )
    }
}

