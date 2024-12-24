package com.kotlity.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kotlity.database.Constants.BITCOIN_TRACKER_DATABASE_VERSION
import com.kotlity.database.converters.CategoryConverter
import com.kotlity.database.daos.BitcoinBalanceEntityDao
import com.kotlity.database.daos.BitcoinDollarExchangeRateEntityDao
import com.kotlity.database.daos.TransactionEntityDao
import com.kotlity.database.entities.BitcoinBalanceEntity
import com.kotlity.database.entities.BitcoinDollarExchangeRateEntity
import com.kotlity.database.entities.TransactionEntity

@Database(
    entities = [ // Declare entities in the database
        BitcoinBalanceEntity::class,
        BitcoinDollarExchangeRateEntity::class,
        TransactionEntity::class
    ],
    version = BITCOIN_TRACKER_DATABASE_VERSION // Define database version
)
@TypeConverters(
    value = [CategoryConverter::class] // Declare TypeConverters in the database so that it knows how to convert an unsupported object type to a supported one
)
abstract class BitcoinTrackerDatabase: RoomDatabase() {

    /**
     *  Declare DAOs in the database for further use
     */
    abstract val bitcoinBalanceEntityDao: BitcoinBalanceEntityDao
    abstract val bitcoinDollarExchangeRateEntityDao: BitcoinDollarExchangeRateEntityDao
    abstract val transactionEntityDao: TransactionEntityDao
}