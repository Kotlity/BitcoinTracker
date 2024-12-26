package com.kotlity.work.di

import com.kotlity.network.BitcoinDollarExchangeRateDataSource
import com.kotlity.network.BitcoinDollarExchangeRateDataSourceImplementation
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val bitcoinDollarExchangeRateDataSourceModule = module {
    factoryOf(::BitcoinDollarExchangeRateDataSourceImplementation) { bind<BitcoinDollarExchangeRateDataSource>() }
}