package com.hibisco.kitsune.feature.ui.profile.delegate

import com.hibisco.kitsune.feature.network.model.Donator

interface ProfileDelegate {

    fun editProfileSuccessful()
    fun errorOnEditProfile(error: String)
    fun editProfileFailed(error: String)

    fun editAddressSuccessful()
    fun errorOnEditAddress(error: String)
    fun editAddressFailed(error: String)

    fun donatorResponse(donator: Donator)
    fun getDonatorFailed(error: String)

    fun logoffSuccessful()
    fun logoffFailed(error: String)

}