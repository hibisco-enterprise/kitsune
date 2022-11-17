package com.hibisco.kitsune.feature.network

import com.hibisco.kitsune.feature.network.model.BloodTypeStock
import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.network.model.DonatorRequest
import com.hibisco.kitsune.feature.network.model.Hospital
import com.hibisco.kitsune.feature.network.request.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface API {

    @GET("hospitals")
    fun getHospitals (): Call<List<Hospital>>

    @POST("donators/login")
    fun login (@Body body: LoginRequest): Call<Donator>

    @POST("donators/register")
    fun register (@Body body: DonatorRequest): Call<String>

    @GET("hospitals/blood")
    fun getBlockStock(@Path("id") id: Long?): Call<List<BloodTypeStock>>
}