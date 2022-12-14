package com.hibisco.kitsune.feature.ui.history.model

data class BeautifulAppointment(
    val year: String,
    val donationData: MutableList<DonationData>
)

data class DonationData(
    val date: String,
    val hour: String,
    val locale: String
)
