package com.kotlity.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kotlity.database.entities.BitcoinBalanceEntity
import kotlinx.coroutines.flow.Flow

/**
 *  DAO for BitcoinBalanceEntity access
 */
@Dao
interface BitcoinBalanceEntityDao {

    /**
     *  Insert TransactionEntity into the database if it does not exist, or overwrite it with the same PrimaryKey
     */
    @Upsert
    suspend fun upsertBitcoinBalance(entity: BitcoinBalanceEntity)

    /**
     *  Reactively load BitcoinBalanceEntity from the database
     */
    @Query("SELECT * FROM BitcoinBalanceEntity WHERE id = 1")
    fun loadBitcoinBalance(): Flow<BitcoinBalanceEntity?>
}