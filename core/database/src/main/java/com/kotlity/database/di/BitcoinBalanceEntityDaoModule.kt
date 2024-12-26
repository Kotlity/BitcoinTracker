package com.kotlity.database.di

import com.kotlity.database.BitcoinTrackerDatabase
import org.koin.dsl.module

val bitcoinBalanceEntityDaoModule = module {
    factory { get<BitcoinTrackerDatabase>().bitcoinBalanceEntityDao }
}