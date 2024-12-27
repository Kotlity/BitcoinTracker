package com.kotlity.feature_add_transaction.di

import com.kotlity.feature_add_transaction.AddTransactionRepository
import com.kotlity.feature_add_transaction.AddTransactionRepositoryImplementation
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val addTransactionRepositoryModule = module {
    singleOf(::AddTransactionRepositoryImplementation) { bind<AddTransactionRepository>() }
}