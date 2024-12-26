package com.kotlity.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.kotlity.domain.DispatcherHandler
import com.kotlity.domain.onError
import com.kotlity.domain.onSuccess
import com.kotlity.network.BitcoinDollarExchangeRateDataSource
import com.kotlity.network.toBitcoinDollarExchangeRate
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 *  Worker which syncs BitcoinDollarExchangeRate in the background once an hour
 */
class BitcoinDollarExchangeRateWorker(
    appContext: Context,
    params: WorkerParameters,
    private val dispatcherHandler: DispatcherHandler,
    private val bitcoinDollarExchangeRateDataSource: BitcoinDollarExchangeRateDataSource,
    private val bitcoinDollarExchangeRateRepository: BitcoinDollarExchangeRateRepository
): CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return withContext(dispatcherHandler.io) {
            val networkCallResult = bitcoinDollarExchangeRateDataSource.getBitcoinDollarExchangeRate()
            networkCallResult
                .onError { Result.retry() }
                .onSuccess { dto ->
                    val dataToInsert = dto.toBitcoinDollarExchangeRate()
                    val result = bitcoinDollarExchangeRateRepository.upsertBitcoinDollarExchangeRate(data = dataToInsert)
                    result.onError { Result.retry() }
                }
            Result.success()
        }
    }

    companion object {

        /**
         *  Worker constrains
         */
        private val constrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        /**
         *  Created PeriodicWorkRequest and tells the WorkManager how to execute the work
         */
        fun startWork(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<BitcoinDollarExchangeRateWorker>(1, TimeUnit.HOURS)
                .setConstraints(constrains)
                .build()
        }
    }
}