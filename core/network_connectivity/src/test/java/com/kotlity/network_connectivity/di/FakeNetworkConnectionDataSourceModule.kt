package com.kotlity.network_connectivity.di

import com.kotlity.network_connectivity.FakeNetworkConnectionDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val fakeNetworkConnectionDataSourceModule = module {
    factoryOf(::FakeNetworkConnectionDataSource)
}