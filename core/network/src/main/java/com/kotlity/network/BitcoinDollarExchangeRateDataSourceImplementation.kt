package com.kotlity.network

import com.kotlity.domain.DispatcherHandler
import com.kotlity.domain.Response
import com.kotlity.domain.errors.NetworkError
import com.kotlity.network.dtos.BitcoinDollarExchangeRateDto
import com.kotlity.network.utils.Constants.BITCOIN_DOLLAR_EXCHANGE_RATE_URL
import com.kotlity.network.utils.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.withContext

class BitcoinDollarExchangeRateDataSourceImplementation(
    private val ktorClient: HttpClient,
    private val dispatcherHandler: DispatcherHandler
): BitcoinDollarExchangeRateDataSource {

    override suspend fun getBitcoinDollarExchangeRate(): Response<BitcoinDollarExchangeRateDto, NetworkError> {
        return withContext(dispatcherHandler.io) {
            safeCall {
                ktorClient.get(urlString = BITCOIN_DOLLAR_EXCHANGE_RATE_URL)
            }
        }
    }
}