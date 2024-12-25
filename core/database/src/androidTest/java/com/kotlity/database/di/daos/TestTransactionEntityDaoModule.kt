package com.kotlity.database.di.daos

import com.kotlity.database.BitcoinTrackerDatabase
import org.koin.dsl.module

val testTransactionEntityDaoModule = module {
    factory { get<BitcoinTrackerDatabase>().transactionEntityDao }
}