package com.kotlity.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.module.Module
import org.koin.test.KoinTest

/**
 *  An abstract class that helps initialize Koin and its modules, where:
 *  KoinTest is an interface that designates an object as a KoinComponent, which allows injecting dependencies from Koin modules.
 */
@RunWith(AndroidJUnit4::class) // tells JUnit that the code will be executed not on the JVM, but using android emulator or physical device
@SmallTest // tells JUnit that this code belongs to unit tests
abstract class BaseDaoTest(
    modules: List<Module>
): KoinTest {

    /**
     * A rule that automatically starts Koin and load modules before each test and stops it with unload modules after each test
     */
    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule(
        modules = modules
    )

    /**
     * A rule that automatically clears all BitcoinTrackerDatabase tables and closes itself after each test
     */
    @get:Rule(order = 1)
    val closingBitcoinTrackerDatabaseRule = ClosingBitcoinTrackerDatabaseRule()

    /**
     *  A rule that automatically swaps main coroutine dispatcher to test coroutine dispatcher
     */
    @get:Rule(order = 2)
    val mainDispatcherRule = MainDispatcherRule()

    /**
     *  Swaps the background executor used by the Architecture Components with a different one which executes each task synchronously
     */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
}