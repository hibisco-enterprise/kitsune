package com.hibisco.kitsune.feature.network.model

data class DonatorRequest(
    val bloodType: String,
    val user: UserRequest
)
