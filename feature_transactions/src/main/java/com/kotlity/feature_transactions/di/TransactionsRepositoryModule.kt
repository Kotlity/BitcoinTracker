package com.kotlity.feature_transactions.di

import com.kotlity.feature_transactions.TransactionsRepository
import com.kotlity.feature_transactions.TransactionsRepositoryImplementation
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val transactionsRepositoryModule = module {
    singleOf(::TransactionsRepositoryImplementation) { bind<TransactionsRepository>() }
}