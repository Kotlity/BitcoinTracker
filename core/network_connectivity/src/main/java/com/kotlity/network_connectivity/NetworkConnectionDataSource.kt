package com.kotlity.network_connectivity

import com.kotlity.domain.Response
import com.kotlity.domain.errors.NetworkConnectionError
import com.kotlity.domain.models.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface NetworkConnectionDataSource {

    fun observeNetworkStatus(): Flow<Response<NetworkStatus, NetworkConnectionError>>
}