package com.kotlity.feature_transactions

import androidx.paging.PagingData
import com.kotlity.domain.Response
import com.kotlity.domain.errors.DatabaseError
import com.kotlity.domain.models.BitcoinBalance
import com.kotlity.domain.models.BitcoinDollarExchangeRate
import com.kotlity.domain.models.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    suspend fun upsertBitcoinBalance(entity: BitcoinBalance): Response<Unit, DatabaseError>

    fun loadBitcoinBalance(): Flow<Response<BitcoinBalance?, DatabaseError>>

    suspend fun addTransaction(data: Transaction): Response<Unit, DatabaseError>

    fun loadAllTransactions(): Flow<PagingData<Response<Transaction, DatabaseError>>>

    fun loadBitcoinDollarExchangeRate(): Flow<Response<BitcoinDollarExchangeRate?, DatabaseError>>
}