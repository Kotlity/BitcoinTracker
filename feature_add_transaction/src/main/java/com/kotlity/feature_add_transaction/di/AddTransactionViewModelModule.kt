package com.kotlity.feature_add_transaction.di

import com.kotlity.feature_add_transaction.AddTransactionViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val addTransactionViewModelModule = module {
    viewModelOf(::AddTransactionViewModel)
}