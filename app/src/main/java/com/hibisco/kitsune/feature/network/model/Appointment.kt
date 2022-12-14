package com.hibisco.kitsune.feature.network.model

import java.time.LocalDateTime

data class Appointment(
    val idAppointment: Long,
    val dhAppointment: LocalDateTime,
    val accepted: Boolean,
    val donator: Donator,
    val hospital: Hospital
)
