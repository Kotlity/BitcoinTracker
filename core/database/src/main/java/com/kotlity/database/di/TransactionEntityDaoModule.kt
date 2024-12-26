package com.kotlity.database.di

import com.kotlity.database.BitcoinTrackerDatabase
import org.koin.dsl.module

val transactionEntityDaoModule = module {
    factory { get<BitcoinTrackerDatabase>().transactionEntityDao }
}