package com.kotlity.bitcointracker

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kotlity.bitcointracker.navigation.BitcoinTrackerNavHost
import com.kotlity.domain.errors.NetworkConnectionError
import com.kotlity.domain.models.NetworkStatus
import com.kotlity.network_connectivity.NetworkConnectionViewModel
import com.kotlity.presentation.DefaultSnackbarHost
import com.kotlity.presentation.Event
import com.kotlity.presentation.ObserveAsEvents
import com.kotlity.presentation.onError
import com.kotlity.presentation.onSuccess
import com.kotlity.presentation.toString
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun BitcoinTrackerApp(
    modifier: Modifier = Modifier,
    networkConnectionViewModel: NetworkConnectionViewModel = koinViewModel()
) {

    val snackbarHostState = remember {
        SnackbarHostState()
    }


    BitcoinTrackerApp(
        modifier = modifier,
        networkConnectionError = networkConnectionViewModel.networkConnectionError,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun BitcoinTrackerApp(
    modifier: Modifier = Modifier,
    networkConnectionError: Flow<Event<NetworkStatus, NetworkConnectionError>>,
    snackbarHostState: SnackbarHostState
) {

    val context = LocalContext.current

    ObserveAsEvents(
        events = arrayOf(networkConnectionError)
    ) { event ->
        event
            .onError { error ->
                snackbarHostState.showSnackbar(message = error.toString())
            }
            .onSuccess { networkStatus ->
                snackbarHostState.showSnackbar(message = networkStatus.toString(context))
            }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { DefaultSnackbarHost(snackbarHostState = snackbarHostState) }
    ) { paddingValues ->

        BitcoinTrackerNavHost(
            modifier = Modifier.padding(paddingValues),
            onShowSnackbar = { message ->
                snackbarHostState.showSnackbar(message = message)
            }
        )
    }
}