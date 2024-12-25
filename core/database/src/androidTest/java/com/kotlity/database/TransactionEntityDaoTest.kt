package com.kotlity.database

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.testing.asSnapshot
import com.google.common.truth.Truth.assertThat
import com.kotlity.database.daos.TransactionEntityDao
import com.kotlity.database.di.daos.testTransactionEntityDaoModule
import com.kotlity.database.di.testBitcoinTrackerDatabaseModule
import com.kotlity.database.di.testDispatcherHandlerModule
import com.kotlity.database.entities.TransactionEntity
import com.kotlity.domain.models.Category
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.inject
import kotlin.random.Random

private fun ClosedFloatingPointRange<Float>.random(): Float {
    return (start + Random.nextFloat() * (endInclusive - start))
}

private fun getRandomCategory(): Category {
    val categoryValues = Category.entries
    return categoryValues[Random.nextInt(categoryValues.size)]
}

private val mockTransactions = (1..32).map { index ->
    TransactionEntity(
        id = index.toLong(),
        bitcoinAmount = (0.1f..100f).random(),
        transactionAmount = (1f..10000f).random(),
        category = getRandomCategory(),
        timestamp = System.currentTimeMillis() - (index * 5000)
    )
}

private const val PAGE_SIZE = 20

class TransactionEntityDaoTest: BaseDaoTest(
    modules = listOf(
        testBitcoinTrackerDatabaseModule,
        testTransactionEntityDaoModule,
        testDispatcherHandlerModule
    )
) {

    private val transactionEntityDao by inject<TransactionEntityDao>()

    private lateinit var pagingData: Flow<PagingData<TransactionEntity>>

    @get:Rule
    val mockKRule = MockKRule(this)

    @Before
    fun setup() {
        pagingData = Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        )) { transactionEntityDao.loadAllTransactions() }.flow
    }

    @Test
    fun initially_load__all_TransactionEntities_returns_empty_list() = runTest {
        val transactions = pagingData.asSnapshot() // returns a List of data after all load operations are complete

        assertThat(transactions).isEmpty()
    }

    @Test
    fun insert_TransactionEntities_successfully() = runTest {
        mockTransactions.forEach {
            transactionEntityDao.addTransaction(it)
        }
        val transactions = pagingData.asSnapshot()

        assertThat(transactions).isEqualTo(mockTransactions)
    }

    @Test
    fun successfully_load_first_page() = runTest {
        mockTransactions.forEach {
            transactionEntityDao.addTransaction(it)
        }

        val pagingSource = transactionEntityDao.loadAllTransactions()

        // loading params for the first page
        val params = PagingSource.LoadParams.Refresh<Int>(
            key = null, // first page
            loadSize = PAGE_SIZE, // load first items
            placeholdersEnabled = false
        )
        val loadResult = pagingSource.load(params = params)

        assertThat(loadResult).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        val page = loadResult as PagingSource.LoadResult.Page

        assertThat(page.data.size).isEqualTo(PAGE_SIZE)
        assertThat(page.data.first().id).isEqualTo(1)
        assertThat(page.data.last().id).isEqualTo(20)
    }

    @Test
    fun successfully_scroll_to_the_second_page_and_load_remaining_transactions() = runTest {
        mockTransactions.forEach {
            transactionEntityDao.addTransaction(it)
        }

        val pagingSource = transactionEntityDao.loadAllTransactions()

        val initialParams = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = PAGE_SIZE,
            placeholdersEnabled = false
        )
        val initialLoadResult = pagingSource.load(params = initialParams) as PagingSource.LoadResult.Page

        // loading params for the second page
        val finalParams = PagingSource.LoadParams.Append(
            key = initialLoadResult.nextKey ?: 1, // get the next page index, or if it does not exist - 1
            loadSize = PAGE_SIZE,
            placeholdersEnabled = false
        )
        val finalLoadResult = pagingSource.load(params = finalParams)

        assertThat(finalLoadResult).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        val page = finalLoadResult as PagingSource.LoadResult.Page

        assertThat(page.data.size).isEqualTo(mockTransactions.size - PAGE_SIZE)
        assertThat(page.data.first().id).isEqualTo(21)
        assertThat(page.data.last().id).isEqualTo(32)
    }

}