package com.kotlity.network_connectivity

import com.kotlity.domain.Response
import com.kotlity.domain.errors.NetworkConnectionError
import com.kotlity.domain.models.NetworkStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeNetworkConnectionDataSource: NetworkConnectionDataSource {

    private val observableNetworkStatusState: MutableStateFlow<Response<NetworkStatus, NetworkConnectionError>> = MutableStateFlow(Response.Success(data = NetworkStatus.IDLE))

    fun updateNetworkStatusState(networkStatusState: Response<NetworkStatus, NetworkConnectionError>) {
        observableNetworkStatusState.update { networkStatusState }
    }

    override fun observeNetworkStatus(): Flow<Response<NetworkStatus, NetworkConnectionError>> {
        return observableNetworkStatusState
    }
}