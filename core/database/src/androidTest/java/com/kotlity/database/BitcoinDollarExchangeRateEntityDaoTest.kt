package com.kotlity.database

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kotlity.database.daos.BitcoinDollarExchangeRateEntityDao
import com.kotlity.database.di.daos.testBitcoinDollarExchangeRateEntityDaoModule
import com.kotlity.database.di.testBitcoinTrackerDatabaseModule
import com.kotlity.database.di.testDispatcherHandlerModule
import com.kotlity.database.entities.BitcoinDollarExchangeRateEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.test.inject

private val mockBitcoinDollarExchangeRateEntity = BitcoinDollarExchangeRateEntity(rate = 97481.23f)

class BitcoinDollarExchangeRateEntityDaoTest: BaseDaoTest(
    modules = listOf(
        testBitcoinTrackerDatabaseModule,
        testBitcoinDollarExchangeRateEntityDaoModule,
        testDispatcherHandlerModule
    )
) {

    private val bitcoinDollarExchangeRateEntityDao by inject<BitcoinDollarExchangeRateEntityDao>()

    @Test
    fun initially_load_BitcoinDollarExchangeRateEntity_returns_null() = runTest {
        val result = bitcoinDollarExchangeRateEntityDao.loadBitcoinDollarExchangeRate().first()

        assertThat(result).isNull()
    }

    @Test
    fun insert_BitcoinDollarExchangeRateEntity_successfully() = runTest {
        bitcoinDollarExchangeRateEntityDao.upsertBitcoinDollarExchangeRate(entity = mockBitcoinDollarExchangeRateEntity)
        val result = bitcoinDollarExchangeRateEntityDao.loadBitcoinDollarExchangeRate().first()

        assertThat(result).isEqualTo(mockBitcoinDollarExchangeRateEntity)
    }

    @Test
    fun update_BitcoinDollarExchangeRateEntity_successfully() = runTest {
        val updatedBitcoinDollarExchangeRateEntity = mockBitcoinDollarExchangeRateEntity.copy(rate = 98174.89f)

        bitcoinDollarExchangeRateEntityDao.apply {
            upsertBitcoinDollarExchangeRate(entity = mockBitcoinDollarExchangeRateEntity)
            upsertBitcoinDollarExchangeRate(entity = updatedBitcoinDollarExchangeRateEntity)
        }

        bitcoinDollarExchangeRateEntityDao.loadBitcoinDollarExchangeRate().test { // subscribe and launched flow
            val result = awaitItem() // suspends and await first emitted item
            assertThat(result).isEqualTo(updatedBitcoinDollarExchangeRateEntity)
        }
    }
}