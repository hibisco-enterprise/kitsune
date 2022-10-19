package com.hibisco.kitsune.feature.ui.signup.viewModel

import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.network.model.AddressRequest
import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.network.model.User
import com.hibisco.kitsune.feature.ui.base.KitsuneViewModel
import com.hibisco.kitsune.feature.ui.signup.delegate.SignupDelegate


class SignupViewModel(delegate: SignupDelegate): KitsuneViewModel() {
    private val retrofit = RetroFitInstance.getRetrofitKitsune()
    val delegate: SignupDelegate = delegate

    fun signup(){
        
    }
}