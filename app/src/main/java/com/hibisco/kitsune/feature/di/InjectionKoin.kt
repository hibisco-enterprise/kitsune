package com.hibisco.kitsune.feature.di

import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.ui.login.viewModel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories

    // ViewModels
    // viewModel { LoginViewModel() }

    // Retrofit
    single { RetroFitInstance }
}