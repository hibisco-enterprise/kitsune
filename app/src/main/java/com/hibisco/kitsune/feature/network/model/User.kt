package com.hibisco.kitsune.feature.network.model

data class User(
    val authenticated: Boolean,
    val documentNumber: String,
    val email: String,
    val idUser: Int,
    val name: String,
    val phone: String,
    val address: Address
)