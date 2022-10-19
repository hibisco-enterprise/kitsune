package com.hibisco.kitsune.feature.ui.signup.delegate

interface SignupDelegate {
    fun registerSuccessful()
    fun registerFailed(error: String)
}