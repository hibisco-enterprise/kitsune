package com.hibisco.kitsune.feature.network

import retrofit2.http.GET
import retrofit2.http.Path

interface APIViaCep {

    @GET("{cep}/json")
    fun getAddressByCep(@Path("cep") cep: Long)

}