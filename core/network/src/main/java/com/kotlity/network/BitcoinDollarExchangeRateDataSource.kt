package com.kotlity.network

import com.kotlity.domain.Response
import com.kotlity.domain.errors.NetworkError
import com.kotlity.network.dtos.BitcoinDollarExchangeRateDto

interface BitcoinDollarExchangeRateDataSource {

    suspend fun getBitcoinDollarExchangeRate(): Response<BitcoinDollarExchangeRateDto, NetworkError>
}