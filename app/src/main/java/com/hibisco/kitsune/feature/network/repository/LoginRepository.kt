package com.hibisco.kitsune.feature.network.repository

class LoginRepository(baseUrl: String): KitsuneRepository(baseUrl) {

    suspend fun getHospitals() = fetchData {
        it.getHospitals()
    }

    suspend fun login(login: String, password: String ) = fetchData{
        it.login(login, password)
    }

}