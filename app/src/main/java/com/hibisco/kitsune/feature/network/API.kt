package com.hibisco.kitsune.feature.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {

    @GET("hospitals")
    suspend fun getHospitals (): retrofit2.Response<ResponseBody>

    @POST("donator/login")
    suspend fun login(
        @Query("login") login: String,
        @Query("password") password: String
    )
}