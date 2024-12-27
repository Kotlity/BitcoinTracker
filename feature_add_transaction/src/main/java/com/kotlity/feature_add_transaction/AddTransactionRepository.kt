package com.kotlity.feature_add_transaction

import com.kotlity.domain.Response
import com.kotlity.domain.errors.DatabaseError
import com.kotlity.domain.models.BitcoinBalance
import com.kotlity.domain.models.Transaction

interface AddTransactionRepository {

    suspend fun upsertBitcoinBalance(entity: BitcoinBalance): Response<Unit, DatabaseError>

    suspend fun addTransaction(data: Transaction): Response<Unit, DatabaseError>

}