package com.kotlity.database.di.daos

import com.kotlity.database.BitcoinTrackerDatabase
import org.koin.dsl.module

val testBitcoinDollarExchangeRateEntityDaoModule = module {
    factory { get<BitcoinTrackerDatabase>().bitcoinDollarExchangeRateEntityDao }
}