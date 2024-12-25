package com.kotlity.database

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ClosingBitcoinTrackerDatabaseRule: TestWatcher(), KoinComponent {

    private val bitcoinTrackerDatabase by inject<BitcoinTrackerDatabase>()

    override fun finished(description: Description?) {
        bitcoinTrackerDatabase.apply {
            clearAllTables()
            close()
        }
    }
}