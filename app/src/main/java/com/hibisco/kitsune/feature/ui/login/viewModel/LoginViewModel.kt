package com.hibisco.kitsune.feature.ui.login.viewModel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.network.model.Hospital
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
            object : Callback<List<Hospital>> {
                override fun onResponse(
                    call: Call<List<Hospital>>,
                    response: Response<List<Hospital>>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<List<Hospital>>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            }
        )
    }

    fun login(email: String, password: String) {
        retrofit.login(email, password).enqueue(
            object: Callback<Donator> {
                override fun onResponse(call: Call<Donator>, response: Response<Donator>) {
                    response.body()?.let { delegate::loginSuccessful }
                }

                override fun onFailure(call: Call<Donator>, t: Throwable) {
                    delegate.loginFailed()
                }

            }
        )
    }
}

