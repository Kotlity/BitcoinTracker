package com.kotlity.work.di

import com.kotlity.work.BitcoinDollarExchangeRateWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val bitcoinDollarExchangeRateWorkerModule = module {
    includes(
        bitcoinDollarExchangeRateDataSourceModule,
        bitcoinDollarExchangeRateRepositoryModule
    )
    workerOf(::BitcoinDollarExchangeRateWorker)
}