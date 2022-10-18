package com.hibisco.kitsune.feature.network

import com.hibisco.kitsune.feature.network.response.ViaCepResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIViaCep {

    @GET("{cep}/json")
    fun getAddressByCep(@Path("cep") cep: String):Call<ViaCepResponse>

}