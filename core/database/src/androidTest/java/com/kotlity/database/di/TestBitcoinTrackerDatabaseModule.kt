package com.kotlity.database.di

import androidx.room.Room
import com.kotlity.database.BitcoinTrackerDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val testBitcoinTrackerDatabaseModule = module {
    single {
        Room.inMemoryDatabaseBuilder(
            androidContext(),
            BitcoinTrackerDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }
}