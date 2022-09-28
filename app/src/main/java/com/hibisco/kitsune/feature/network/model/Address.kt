package com.hibisco.kitsune.feature.network.model

data class Address(
    val address: String,
    val cep: String,
    val city: String,
    val idAddress: Int,
    val latitude: Double,
    val longitude: Double,
    val neighborhood: String,
    val number: Int,
    val uf: String
)
