package com.hibisco.kitsune.feature.network

import com.hibisco.kitsune.feature.network.model.*
import com.hibisco.kitsune.feature.network.request.LoginRequest
import retrofit2.Call
import retrofit2.http.*

interface API {

    @GET("hospitals")
    fun getHospitals (): Call<List<Hospital>>

    @POST("donators/login")
    fun login (@Body body: LoginRequest): Call<Donator>

    @POST("donators/register")
    fun register (@Body body: DonatorRequest): Call<String>

    @GET("hospitals/blood/{id}")
    fun getBlockStock(
        @Path("id") id: Long
    ): Call<List<BloodTypeStock>>

    @POST("donators/appointment")
    fun createAppointment(@Body appointment: AppointmentRequestDTO): Call<Void>
}