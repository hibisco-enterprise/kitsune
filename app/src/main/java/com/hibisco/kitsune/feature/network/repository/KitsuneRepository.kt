package com.hibisco.kitsune.feature.network.repository

import com.hibisco.kitsune.feature.network.API
import com.hibisco.kitsune.feature.network.RetroFitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

open class KitsuneRepository (private val baseUrl: String) {
        suspend fun fetchData(service: suspend (api: API) -> Response<*>) =
            withContext(Dispatchers.IO) {
                val api = RetroFitInstance.getRetrofit(baseUrl)
                return@withContext service(api)
            }
}