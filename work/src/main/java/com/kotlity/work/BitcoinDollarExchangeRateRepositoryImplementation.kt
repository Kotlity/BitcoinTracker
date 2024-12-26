package com.kotlity.work

import com.kotlity.database.daos.BitcoinDollarExchangeRateEntityDao
import com.kotlity.domain.DispatcherHandler
import com.kotlity.domain.Response
import com.kotlity.domain.errors.DatabaseError
import com.kotlity.domain.models.BitcoinDollarExchangeRate
import com.kotlity.work.utils.databaseCall

class BitcoinDollarExchangeRateRepositoryImplementation(
    private val bitcoinDollarExchangeRateEntityDao: BitcoinDollarExchangeRateEntityDao,
    private val dispatcherHandler: DispatcherHandler
): BitcoinDollarExchangeRateRepository {

    override suspend fun upsertBitcoinDollarExchangeRate(data: BitcoinDollarExchangeRate): Response<Unit, DatabaseError> {
        return databaseCall(
            dispatcher = dispatcherHandler.io,
            call = {
                val entity = data.toBitcoinDollarExchangeRateEntity()
                bitcoinDollarExchangeRateEntityDao.upsertBitcoinDollarExchangeRate(entity = entity)
                Response.Success(data = Unit)
            }
        )
    }
}