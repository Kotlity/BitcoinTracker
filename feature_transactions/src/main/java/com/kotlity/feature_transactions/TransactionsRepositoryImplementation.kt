package com.kotlity.feature_transactions

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kotlity.database.daos.BitcoinBalanceEntityDao
import com.kotlity.database.daos.BitcoinDollarExchangeRateEntityDao
import com.kotlity.database.daos.TransactionEntityDao
import com.kotlity.database.mappers.toBitcoinBalance
import com.kotlity.database.mappers.toBitcoinBalanceEntity
import com.kotlity.database.mappers.toBitcoinDollarExchangeRate
import com.kotlity.database.mappers.toTransaction
import com.kotlity.database.mappers.toTransactionEntity
import com.kotlity.database.utils.databaseCall
import com.kotlity.database.utils.databaseFlowCall
import com.kotlity.domain.DispatcherHandler
import com.kotlity.domain.Response
import com.kotlity.domain.errors.DatabaseError
import com.kotlity.domain.models.BitcoinBalance
import com.kotlity.domain.models.BitcoinDollarExchangeRate
import com.kotlity.domain.models.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

private const val PAGE_SIZE = 20
class TransactionsRepositoryImplementation(
    private val dispatcherHandler: DispatcherHandler,
    private val bitcoinBalanceEntityDao: BitcoinBalanceEntityDao,
    private val transactionEntityDao: TransactionEntityDao,
    private val bitcoinDollarExchangeRateEntityDao: BitcoinDollarExchangeRateEntityDao
): TransactionsRepository {

    override suspend fun upsertBitcoinBalance(entity: BitcoinBalance): Response<Unit, DatabaseError> {
        return databaseCall(
            dispatcher = dispatcherHandler.io,
            call = {
                val result = bitcoinBalanceEntityDao.upsertBitcoinBalance(entity = entity.toBitcoinBalanceEntity())
                Response.Success(data = result)
            }
        )
    }

    override fun loadBitcoinBalance(): Flow<Response<BitcoinBalance?, DatabaseError>> {
        return databaseFlowCall(
            dispatcher = dispatcherHandler.io,
            flowProvider = { bitcoinBalanceEntityDao.loadBitcoinBalance() },
            mapper = { entity -> entity?.toBitcoinBalance() }
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

    override fun loadAllTransactions(): Flow<PagingData<Transaction>> {
        return Pager(
            PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            )
        ) { transactionEntityDao.loadAllTransactions() }
            .flow
            .map { pagingData ->
                pagingData.map { entity ->
                    entity.toTransaction()
                }
            }
            .flowOn(dispatcherHandler.io)

    }

    override fun loadBitcoinDollarExchangeRate(): Flow<Response<BitcoinDollarExchangeRate?, DatabaseError>> {
        return databaseFlowCall(
            dispatcher = dispatcherHandler.io,
            flowProvider = { bitcoinDollarExchangeRateEntityDao.loadBitcoinDollarExchangeRate() },
            mapper = { entity -> entity?.toBitcoinDollarExchangeRate() }
        )
    }
}