package com.kotlity.network_connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.kotlity.domain.DispatcherHandler
import com.kotlity.domain.Response
import com.kotlity.domain.errors.NetworkConnectionError
import com.kotlity.domain.models.NetworkStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class DefaultNetworkConnectionDataSource(
    context: Context,
    private val dispatcherHandler: DispatcherHandler
): NetworkConnectionDataSource {

    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val getNetworkRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI_AWARE)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()
    } else {
        NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()
    }

    override fun observeNetworkStatus(): Flow<Response<NetworkStatus, NetworkConnectionError>> {
        return callbackFlow {

            val networkCallback = object: NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    val networkStatus = connectivityManager.getNetworkStatus(network = network)
                    trySend(Response.Success(data = networkStatus))
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(Response.Success(data = NetworkStatus.NO_INTERNET))
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(Response.Success(data = NetworkStatus.NO_INTERNET))
                }
            }

            try {
                connectivityManager.registerNetworkCallback(getNetworkRequest, networkCallback)
            } catch (e: SecurityException) {
                trySend(Response.Error(error = NetworkConnectionError.SECURITY))
            } catch (e: Exception) {
                trySend(Response.Error(error = NetworkConnectionError.UNKNOWN))
            }

            awaitClose {
                try {
                    connectivityManager.unregisterNetworkCallback(networkCallback)
                } catch (e: IllegalArgumentException) {
                    trySend(Response.Error(error = NetworkConnectionError.ILLEGAL_ARGUMENT))
                } catch (e: Exception) {
                    trySend(Response.Error(error = NetworkConnectionError.UNKNOWN))
                }
            }
        }
            .distinctUntilChanged()
            .flowOn(dispatcherHandler.io)
    }

    private fun ConnectivityManager.getNetworkStatus(network: Network): NetworkStatus {
        val networkCapabilities = getNetworkCapabilities(network)
        val isTransportWiFiAware = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) == true
        val hasInternetConnection = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true ||
                isTransportWiFiAware

        return if (hasInternetConnection) NetworkStatus.HAS_INTERNET
            else NetworkStatus.NO_INTERNET
    }
}