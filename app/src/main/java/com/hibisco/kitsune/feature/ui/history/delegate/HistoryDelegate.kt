package com.hibisco.kitsune.feature.ui.history.delegate

import com.hibisco.kitsune.feature.network.model.Appointment

interface HistoryDelegate {

    fun appointmentsResponse(appointments: List<Appointment>)
    fun getAppointmentsFailed(error: String)

}