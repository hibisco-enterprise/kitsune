package com.hibisco.kitsune.feature.network.model

data class AddressRequest(
    val address: String,
    val neighborhood: String,
    val city: String,
    val uf: String,
    val cep: String,
    val number: Int
)
