package com.hibisco.kitsune.feature.network

import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.network.model.Hospital
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {

    @GET("hospitals")
    fun getHospitals (): Call<List<Hospital>>

    @POST("donator/login")
    fun login (
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<Donator>
}