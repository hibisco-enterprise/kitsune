package com.hibisco.kitsune.feature.network.model

data class UserRequest(
    val email: String,
    val documentNumber: String,
    val password: String,
    val phone: String,
    val address: AddressRequest
)
