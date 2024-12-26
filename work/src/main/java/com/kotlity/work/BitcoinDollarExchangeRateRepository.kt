package com.kotlity.work

import com.kotlity.domain.Response
import com.kotlity.domain.errors.DatabaseError
import com.kotlity.domain.models.BitcoinDollarExchangeRate

interface BitcoinDollarExchangeRateRepository {

    suspend fun upsertBitcoinDollarExchangeRate(data: BitcoinDollarExchangeRate): Response<Unit, DatabaseError>
}