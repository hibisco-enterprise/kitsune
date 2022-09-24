package com.hibisco.kitsune.feature.stateFlow

sealed class StateFlow {

    data class Loading(val loading: Boolean): StateFlow()

    data class Success<T>(val data: T ): StateFlow()

    data class Error(val errorMessage: String, val errorCode: String?, val errorID: String?): StateFlow()


}