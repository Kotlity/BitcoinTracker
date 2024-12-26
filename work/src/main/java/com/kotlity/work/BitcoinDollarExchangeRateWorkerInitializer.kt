package com.kotlity.work

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager

object BitcoinDollarExchangeRateWorkerInitializer {

    /**
     *  This method is a workaround to manually initialize the sync process instead of relying on
     *  automatic initialization with Androidx Startup. It is called from the app module's
     *  Application.onCreate() and should be only done once
     */
    fun initialize(appContext: Context) {
        AppInitializer.getInstance(appContext).initializeComponent(SyncInitializer::class.java)
    }
}

internal const val SyncWorkName = "SyncWorkName"

class SyncInitializer : Initializer<BitcoinDollarExchangeRateWorkerInitializer> {
    override fun create(context: Context): BitcoinDollarExchangeRateWorkerInitializer {
        WorkManager.getInstance(context).apply {

            // Run sync on app startup and ensure only one sync worker runs at any time
            enqueueUniquePeriodicWork(
                SyncWorkName,
                ExistingPeriodicWorkPolicy.KEEP,
                BitcoinDollarExchangeRateWorker.startWork()
            )
        }

        return BitcoinDollarExchangeRateWorkerInitializer
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf()
}