package com.kotlity.network_connectivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlity.domain.errors.NetworkConnectionError
import com.kotlity.domain.models.NetworkStatus
import com.kotlity.domain.onErrorFlow
import com.kotlity.domain.onSuccessFlow
import com.kotlity.presentation.Event
import com.kotlity.presentation.toStateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class NetworkConnectionViewModel(private val networkConnectionDataSource: NetworkConnectionDataSource): ViewModel() {

    private val _state = MutableStateFlow(NetworkStatus.IDLE)
    val state = _state
        .onStart { getNetworkStatus() }
        .toStateFlow(
            scope = viewModelScope,
            initialValue = NetworkStatus.IDLE
        )

    private val _networkConnectionError = Channel<Event<Unit, NetworkConnectionError>>()
    val networkConnectionError = _networkConnectionError.receiveAsFlow()

    private fun getNetworkStatus() {
        networkConnectionDataSource.observeNetworkStatus()
            .onSuccessFlow { networkStatus ->
                _state.update { networkStatus }
            }
            .onErrorFlow { networkConnectionError ->
                _networkConnectionError.send(Event.Error(error = networkConnectionError))
            }
            .launchIn(viewModelScope)
    }
}