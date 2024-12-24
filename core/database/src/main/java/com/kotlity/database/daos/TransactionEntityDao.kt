package com.kotlity.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kotlity.database.entities.TransactionEntity

/**
 *  DAO for TransactionEntity access
 */
@Dao
interface TransactionEntityDao {

    /**
     *  Insert TransactionEntity into the database
     */
    @Insert
    suspend fun addTransaction(entity: TransactionEntity)

    /**
     *  Load all transactions with pagination mechanism from the database, sorted by time in descending order(newest to oldest)
     *
     *  PagingSource is a base class that is responsible for providing data for pagination, where:
     *  Int - type of key which define what data to load;
     *  TransactionEntity - type of data loaded in by this PagingSource
     */
    @Query("SELECT * FROM transactionentity ORDER BY timestamp DESC")
    fun loadAllTransactions(): PagingSource<Int, TransactionEntity>
}