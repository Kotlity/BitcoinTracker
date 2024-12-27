package com.kotlity.network_connectivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlity.domain.errors.NetworkConnectionError
import com.kotlity.domain.models.NetworkStatus
import com.kotlity.domain.onErrorFlow
import com.kotlity.domain.onSuccessFlow
import com.kotlity.presentation.Event
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow

class NetworkConnectionViewModel(private val networkConnectionDataSource: NetworkConnectionDataSource): ViewModel() {

    init {
        getNetworkStatus()
    }

    private val _networkConnectionError = Channel<Event<NetworkStatus, NetworkConnectionError>>()
    val networkConnectionError = _networkConnectionError.receiveAsFlow()

    private fun getNetworkStatus() {
        networkConnectionDataSource.observeNetworkStatus()
            .onSuccessFlow { networkStatus ->
                if (networkStatus == NetworkStatus.NO_INTERNET) _networkConnectionError.send(Event.Success(data = networkStatus))
            }
            .onErrorFlow { networkConnectionError ->
                _networkConnectionError.send(Event.Error(error = networkConnectionError))
            }
            .launchIn(viewModelScope)
    }
}