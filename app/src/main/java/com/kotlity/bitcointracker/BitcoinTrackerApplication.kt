package com.kotlity.bitcointracker

import android.app.Application
import com.kotlity.database.di.bitcoinBalanceEntityDaoModule
import com.kotlity.database.di.bitcoinDollarExchangeRateEntityDaoModule
import com.kotlity.database.di.bitcoinTrackerDatabaseModule
import com.kotlity.database.di.transactionEntityDaoModule
import com.kotlity.feature_add_transaction.di.addTransactionRepositoryModule
import com.kotlity.feature_add_transaction.di.addTransactionViewModelModule
import com.kotlity.feature_transactions.di.transactionsRepositoryModule
import com.kotlity.feature_transactions.di.transactionsViewModelModule
import com.kotlity.network.di.dispatcherHandlerModule
import com.kotlity.network.di.ktorClientModule
import com.kotlity.network_connectivity.di.networkConnectionDataSourceModule
import com.kotlity.network_connectivity.di.networkConnectionViewModelModule
import com.kotlity.work.BitcoinDollarExchangeRateWorkerInitializer
import com.kotlity.work.di.bitcoinDollarExchangeRateDataSourceModule
import com.kotlity.work.di.bitcoinDollarExchangeRateWorkerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class BitcoinTrackerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        BitcoinDollarExchangeRateWorkerInitializer.initialize(this)
    }
}

private fun Application.setupKoin() {
    startKoin {
        androidLogger()
        androidContext(this@setupKoin)
        workManagerFactory()
        modules(
            dispatcherHandlerModule,
            ktorClientModule,
            bitcoinTrackerDatabaseModule,
            bitcoinDollarExchangeRateEntityDaoModule,
            bitcoinBalanceEntityDaoModule,
            transactionEntityDaoModule,
            bitcoinDollarExchangeRateDataSourceModule,
            bitcoinDollarExchangeRateWorkerModule,
            networkConnectionDataSourceModule,
            networkConnectionViewModelModule,
            transactionsRepositoryModule,
            transactionsViewModelModule,
            addTransactionRepositoryModule,
            addTransactionViewModelModule
        )
    }
}