package com.kotlity.network_connectivity

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkConnectionDataSourceModule = module {
    single<NetworkConnectionDataSource> { DefaultNetworkConnectionDataSource(androidContext(), get()) }
}