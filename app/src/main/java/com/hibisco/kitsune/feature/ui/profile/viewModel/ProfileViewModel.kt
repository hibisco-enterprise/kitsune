package com.hibisco.kitsune.feature.ui.profile.viewModel

import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.network.model.AddressRequest
import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.network.model.DonatorRequest
import com.hibisco.kitsune.feature.ui.base.KitsuneViewModel
import com.hibisco.kitsune.feature.ui.profile.delegate.ProfileDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(val delegate: ProfileDelegate): KitsuneViewModel() {

    private val retrofit = RetroFitInstance.getRetrofitKitsune()

    fun saveProfile(id: Long, donator: DonatorRequest){
        retrofit.saveProfile(id, donator).enqueue(
            object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    println(response)
                    if (response.code() == 200){
                        println(response)
                        println("Dados do perfil atualizados")
                        delegate.editProfileSuccessful()
                    } else {
                        delegate.errorOnEditProfile(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                    println(t.message.toString())
                    delegate.editProfileFailed(t.message.toString())
                }

            }
        )
    }

    fun saveAddress(id: Int, address: AddressRequest){
        // Precisa integrar com o local storage
        retrofit.saveAddress(id, address).enqueue(
            object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    println(response)
                    if (response.code() == 200){
                        println(response)
                        println("Endereço atualizado")
                        delegate.editAddressSuccessful()
                    } else {
                        delegate.errorOnEditAddress(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                    println(t.message.toString())
                    delegate.editAddressFailed(t.message.toString())
                }

            }
        )
    }

    fun getDonator(id: Long){
        retrofit.getDonator(id).enqueue(
            object: Callback<Donator> {
                override fun onResponse(call: Call<Donator>, response: Response<Donator>) {
                    println(response)
                    println("Recuperado")
                    delegate.donatorResponse(response.body()!!)
                }

                override fun onFailure(call: Call<Donator>, t: Throwable) {
                    println(t.message.toString())
                    delegate.getDonatorFailed(t.message.toString())
                }

            }
        )
    }

    fun logoff(id: Int){
        retrofit.logoff(id).enqueue(
            object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    println(response)
                    if (response.code() == 200){
                        println("Saiu")
                        delegate.logoffSuccessful()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    println(t.message.toString())
                    delegate.logoffFailed(t.message.toString())
                }

            }
        )
    }

}