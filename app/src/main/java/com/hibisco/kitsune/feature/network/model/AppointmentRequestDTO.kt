package com.hibisco.kitsune.feature.network.model

import java.time.LocalDateTime

data class AppointmentRequestDTO(
    val dhAppointment: LocalDateTime,
    val FkDonator: Long,
    val FkHospital: Long
)
