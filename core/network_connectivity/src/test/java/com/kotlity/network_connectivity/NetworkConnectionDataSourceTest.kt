package com.kotlity.network_connectivity

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kotlity.domain.Response
import com.kotlity.domain.errors.NetworkConnectionError
import com.kotlity.domain.models.NetworkStatus
import com.kotlity.network_connectivity.di.fakeNetworkConnectionDataSourceModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class NetworkConnectionDataSourceTest: KoinTest {

    private val fakeNetworkConnectionDataSource by inject<FakeNetworkConnectionDataSource>()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(fakeNetworkConnectionDataSourceModule)
    }

    @Test
    fun `initial network status is NetworkStatus dot Idle`() = runTest {
        val result = fakeNetworkConnectionDataSource.observeNetworkStatus().first()

        assertThat(result).isInstanceOf(Response.Success::class.java)
        assertThat((result as Response.Success).data).isEqualTo(NetworkStatus.IDLE)
    }

    @Test
    fun `observing network connection returns NetworkStatus dot HAS_INTERNET`() = runTest {
        val updatedNetworkStatus = Response.Success(data = NetworkStatus.HAS_INTERNET)

        fakeNetworkConnectionDataSource.observeNetworkStatus().test {
            fakeNetworkConnectionDataSource.updateNetworkStatusState(networkStatusState = updatedNetworkStatus)

            val result = expectMostRecentItem()

            assertThat((result as Response.Success).data).isEqualTo(NetworkStatus.HAS_INTERNET)
        }
    }

    @Test
    fun `observing network connection returns NetworkStatus dot NO_INTERNET`() = runTest {
        val updatedNetworkStatus = Response.Success(data = NetworkStatus.NO_INTERNET)

        fakeNetworkConnectionDataSource.observeNetworkStatus().test {
            fakeNetworkConnectionDataSource.updateNetworkStatusState(networkStatusState = updatedNetworkStatus)

            val result = expectMostRecentItem()

            assertThat((result as Response.Success).data).isEqualTo(NetworkStatus.NO_INTERNET)
        }
    }

    @Test
    fun `observing network connection returns NetworkConnectionError dot SECURITY`() = runTest {
        val updatedNetworkStatus = Response.Error(error = NetworkConnectionError.SECURITY)

        fakeNetworkConnectionDataSource.observeNetworkStatus().test {
            fakeNetworkConnectionDataSource.updateNetworkStatusState(networkStatusState = updatedNetworkStatus)

            val result = expectMostRecentItem()

            assertThat((result as Response.Error).error).isEqualTo(NetworkConnectionError.SECURITY)
        }
    }
}