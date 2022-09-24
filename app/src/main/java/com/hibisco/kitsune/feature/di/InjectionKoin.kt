package com.hibisco.kitsune.feature.di

import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.network.repository.LoginRepository
import com.hibisco.kitsune.feature.ui.Login.ViewModel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    val baseUrl = "http://10.0.2.2:8080/"
    // Repositories
    single {LoginRepository(baseUrl) }

    // ViewModels
    viewModel { LoginViewModel(get()) }

    // Retrofit
    single { RetroFitInstance }
}