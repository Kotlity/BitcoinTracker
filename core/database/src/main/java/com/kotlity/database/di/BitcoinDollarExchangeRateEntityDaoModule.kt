package com.kotlity.database.di

import com.kotlity.database.BitcoinTrackerDatabase
import org.koin.dsl.module

val bitcoinDollarExchangeRateEntityDaoModule = module {
    factory { get<BitcoinTrackerDatabase>().bitcoinDollarExchangeRateEntityDao }
}