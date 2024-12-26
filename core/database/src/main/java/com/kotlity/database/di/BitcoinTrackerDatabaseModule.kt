package com.kotlity.database.di

import androidx.room.Room
import com.kotlity.database.BitcoinTrackerDatabase
import com.kotlity.database.Constants.BITCOIN_TRACKER_DATABASE_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val bitcoinTrackerDatabaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            BitcoinTrackerDatabase::class.java,
            BITCOIN_TRACKER_DATABASE_NAME
        )
            .build()
    }
}