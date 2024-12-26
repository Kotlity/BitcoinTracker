package com.kotlity.work.di

import com.kotlity.work.BitcoinDollarExchangeRateRepository
import com.kotlity.work.BitcoinDollarExchangeRateRepositoryImplementation
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val bitcoinDollarExchangeRateRepositoryModule = module {
    factoryOf(::BitcoinDollarExchangeRateRepositoryImplementation) { bind<BitcoinDollarExchangeRateRepository>() }
}