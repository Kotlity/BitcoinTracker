package com.kotlity.network_connectivity.di

import com.kotlity.network_connectivity.DefaultNetworkConnectionDataSource
import com.kotlity.network_connectivity.NetworkConnectionDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkConnectionDataSourceModule = module {
    single<NetworkConnectionDataSource> { DefaultNetworkConnectionDataSource(androidContext(), get()) }
}