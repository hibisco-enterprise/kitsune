package com.hibisco.kitsune.feature.ui.signup.delegate

interface SignupDelegate {
    fun registerSuccessful()
    fun errorOnRegister(error: String)
    fun registerFailed(error: String)
}