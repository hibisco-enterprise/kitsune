package com.hibisco.kitsune.feature.ui.map.viewModel

import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.network.model.BloodTypeStock
import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.network.model.Hospital
import com.hibisco.kitsune.feature.network.request.LoginRequest
import com.hibisco.kitsune.feature.ui.base.KitsuneViewModel
import com.hibisco.kitsune.feature.ui.map.delegate.MapDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(val delegate: MapDelegate): KitsuneViewModel() {
    private val retrofit = RetroFitInstance.getRetrofitKitsune()

    fun getHospitals() {
        retrofit.getHospitals().enqueue(
                object: Callback<List<Hospital>> {
                    override fun onResponse(
                        call: Call<List<Hospital>>,
                        response: Response<List<Hospital>>
                    ) {
                        response.body()?.let { delegate.hospitalsResponse(it) }
                    }

                    override fun onFailure(call: Call<List<Hospital>>, t: Throwable) {
                       delegate.getHospitalsFailed(t.message.toString())
                    }
                }
            )
    }

    fun getBloodStock(hospitalId: Long) {
        retrofit.getBlockStock(hospitalId).enqueue(
            object: Callback<List<BloodTypeStock>> {
                override fun onResponse(
                    call: Call<List<BloodTypeStock>>,
                    response: Response<List<BloodTypeStock>>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<List<BloodTypeStock>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
        )
    }
}