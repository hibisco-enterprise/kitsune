package com.hibisco.kitsune.feature.network

import retrofit2.http.GET
import retrofit2.http.Path

interface APIIBGE {

    @GET("?orderBy=nome")
    fun getEstados(): List<Any>

    @GET("{uf}/municipios?orderBy=nome")
    fun getMunicipios(@Path("uf") uf: String): List<Any>

}