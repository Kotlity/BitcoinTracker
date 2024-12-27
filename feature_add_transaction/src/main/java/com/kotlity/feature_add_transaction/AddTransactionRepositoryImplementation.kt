package com.kotlity.feature_add_transaction

import com.kotlity.database.daos.BitcoinBalanceEntityDao
import com.kotlity.database.daos.TransactionEntityDao
import com.kotlity.database.mappers.toBitcoinBalanceEntity
import com.kotlity.database.mappers.toTransactionEntity
import com.kotlity.database.utils.databaseCall
import com.kotlity.domain.DispatcherHandler
import com.kotlity.domain.Response
import com.kotlity.domain.errors.DatabaseError
import com.kotlity.domain.models.BitcoinBalance
import com.kotlity.domain.models.Transaction

class AddTransactionRepositoryImplementation(
    private val dispatcherHandler: DispatcherHandler,
    private val bitcoinBalanceEntityDao: BitcoinBalanceEntityDao,
    private val transactionEntityDao: TransactionEntityDao
): AddTransactionRepository {

    override suspend fun upsertBitcoinBalance(entity: BitcoinBalance): Response<Unit, DatabaseError> {
        return databaseCall(
            dispatcher = dispatcherHandler.io,
            call = {
                val result = bitcoinBalanceEntityDao.upsertBitcoinBalance(entity = entity.toBitcoinBalanceEntity())
                Response.Success(data = result)
            }
        )
    }

    override suspend fun addTransaction(data: Transaction): Response<Unit, DatabaseError> {
        return databaseCall(
            dispatcher = dispatcherHandler.io,
            call = {
                val result = transactionEntityDao.addTransaction(entity = data.toTransactionEntity())
                Response.Success(data = result)
            }
        )
    }
}