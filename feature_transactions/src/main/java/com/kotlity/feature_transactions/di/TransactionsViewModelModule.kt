package com.kotlity.feature_transactions.di

import com.kotlity.feature_transactions.TransactionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val transactionsViewModelModule = module {
    viewModelOf(::TransactionsViewModel)
}