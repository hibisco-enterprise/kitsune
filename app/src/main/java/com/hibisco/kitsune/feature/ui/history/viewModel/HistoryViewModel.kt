package com.hibisco.kitsune.feature.ui.history.viewModel

import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.network.model.Appointment
import com.hibisco.kitsune.feature.network.model.DonatorRequest
import com.hibisco.kitsune.feature.ui.base.KitsuneViewModel
import com.hibisco.kitsune.feature.ui.history.delegate.HistoryDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel(val delegate: HistoryDelegate): KitsuneViewModel() {

    private val retrofit = RetroFitInstance.getRetrofitKitsune()

    fun getAppointments(id: Long){
        retrofit.getAppointments(id).enqueue(
            object: Callback<List<Appointment>> {
                override fun onResponse(call: Call<List<Appointment>>, response: Response<List<Appointment>>) {
                    println(response)
                    println("Histórico recuperado")
                    if (response.code() == 404){
                        delegate.appointmentsResponse(ArrayList<Appointment>())
                    } else {
                        delegate.appointmentsResponse(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<List<Appointment>>, t: Throwable) {

                    println(t.message.toString())
                    delegate.getAppointmentsFailed(t.message.toString())
                }

            }
        )
    }

}