package com.hibisco.kitsune.feature.ui.login.delegate

import com.hibisco.kitsune.feature.network.model.Donator

interface LoginDelegate {
    fun loginSuccessful(response: Donator)
    fun loginFailed()
}