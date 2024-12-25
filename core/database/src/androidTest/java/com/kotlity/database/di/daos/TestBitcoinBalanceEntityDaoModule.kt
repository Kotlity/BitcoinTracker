package com.kotlity.database.di.daos

import com.kotlity.database.BitcoinTrackerDatabase
import org.koin.dsl.module

val testBitcoinBalanceEntityDaoModule = module {
    factory { get<BitcoinTrackerDatabase>().bitcoinBalanceEntityDao }
}