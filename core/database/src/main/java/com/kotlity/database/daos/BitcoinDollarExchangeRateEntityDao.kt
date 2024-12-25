package com.kotlity.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kotlity.database.entities.BitcoinDollarExchangeRateEntity
import kotlinx.coroutines.flow.Flow

/**
 *  DAO for BitcoinDollarExchangeRateEntity access
 */
@Dao
interface BitcoinDollarExchangeRateEntityDao {

    /**
     *  Insert BitcoinDollarExchangeRateEntity into the database if it does not exist, or overwrite it with the same PrimaryKey
     */
    @Upsert
    suspend fun upsertBitcoinDollarExchangeRate(entity: BitcoinDollarExchangeRateEntity)

    /**
     *  Reactively load BitcoinDollarExchangeRateEntity from the database
     */
    @Query("SELECT * FROM BitcoinDollarExchangeRateEntity WHERE id = 1")
    fun loadBitcoinDollarExchangeRate(): Flow<BitcoinDollarExchangeRateEntity?>
}