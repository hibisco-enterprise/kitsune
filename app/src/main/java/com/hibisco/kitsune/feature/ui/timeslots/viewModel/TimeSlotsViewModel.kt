package com.hibisco.kitsune.feature.ui.timeslots.viewModel

import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.network.model.AppointmentRequestDTO
import com.hibisco.kitsune.feature.ui.calendar.model.DateModel
import com.hibisco.kitsune.feature.ui.timeslots.delegate.TimeSlotsDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.Month

class TimeSlotsViewModel(val delegate: TimeSlotsDelegate) {
    private val retrofit = RetroFitInstance.getRetrofitKitsune()

    fun createAppointment(date: DateModel,
                          idHospital: Long,
                          idDonator: Long,
                          hour: Int,
                          minute: Int) {
        val dateTime = LocalDateTime.of(date.year, Month.of(date.month), date.day, hour, minute)
        println(dateTime.toString())
        val appointment = AppointmentRequestDTO(dateTime.toString().replace("T", " ") + ":00", idDonator, idHospital)

        retrofit.createAppointment(appointment).enqueue(
            object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                   delegate.onAppointmentCreated()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    delegate.onAppointmentCreationFailed()
                }

            }
        )
    }

}