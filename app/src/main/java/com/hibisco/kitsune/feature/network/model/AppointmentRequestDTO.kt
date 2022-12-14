package com.hibisco.kitsune.feature.network.model

import java.time.LocalDateTime

data class AppointmentRequestDTO(
    val dhAppointment: String,
    val fkDonator: Long,
    val fkHospital: Long
)
