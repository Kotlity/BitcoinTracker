package com.kotlity.database

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kotlity.database.daos.BitcoinBalanceEntityDao
import com.kotlity.database.di.daos.testBitcoinBalanceEntityDaoModule
import com.kotlity.database.di.testBitcoinTrackerDatabaseModule
import com.kotlity.database.di.testDispatcherHandlerModule
import com.kotlity.database.entities.BitcoinBalanceEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.test.inject

private val mockBitcoinBalanceEntity = BitcoinBalanceEntity(
    amount = 12.5f,
    balance = 12.5f * 97481.23f
)

class BitcoinBalanceEntityDaoTest: BaseDaoTest(
    modules = listOf(
        testBitcoinTrackerDatabaseModule,
        testBitcoinBalanceEntityDaoModule,
        testDispatcherHandlerModule
    )
) {

    private val bitcoinBalanceEntityDao by inject<BitcoinBalanceEntityDao>()

    @Test
    fun initially_load_BitcoinBalanceEntity_returns_null() = runTest {
        val result = bitcoinBalanceEntityDao.loadBitcoinBalance().first()

        assertThat(result).isNull()
    }

    @Test
    fun insert_BitcoinBalanceEntity_successfully() = runTest {
        bitcoinBalanceEntityDao.upsertBitcoinBalance(entity = mockBitcoinBalanceEntity)
        val result = bitcoinBalanceEntityDao.loadBitcoinBalance().first()

        assertThat(result).isEqualTo(mockBitcoinBalanceEntity)
    }

    @Test
    fun update_BitcoinBalanceEntity_successfully() = runTest {
        val updatedBitcoinBalanceEntity = mockBitcoinBalanceEntity.copy(amount = 3.3f)

        bitcoinBalanceEntityDao.apply {
            upsertBitcoinBalance(entity = mockBitcoinBalanceEntity)
            upsertBitcoinBalance(entity = updatedBitcoinBalanceEntity)
        }

        bitcoinBalanceEntityDao.loadBitcoinBalance().test { // subscribe and launched flow
            val result = awaitItem() // suspends and await first emitted item
            assertThat(result).isEqualTo(updatedBitcoinBalanceEntity)
        }
    }
}