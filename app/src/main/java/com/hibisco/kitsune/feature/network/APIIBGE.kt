package com.hibisco.kitsune.feature.network

import com.hibisco.kitsune.feature.network.model.ibge.Estado
import com.hibisco.kitsune.feature.network.model.ibge.Municipio
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface APIIBGE {

    @GET("?orderBy=nome")
    fun getEstados(): Call<List<Estado>>

    @GET("{uf}/municipios?orderBy=nome")
    fun getMunicipios(@Path("uf") uf: String): Call<List<Municipio>>

}