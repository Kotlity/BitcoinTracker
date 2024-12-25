package com.kotlity.network_connectivity.di

import com.kotlity.network_connectivity.NetworkConnectionViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val networkConnectionViewModelModule = module {
    viewModelOf(::NetworkConnectionViewModel)
}