package com.kotlity.network

import com.kotlity.domain.models.BitcoinDollarExchangeRate
import com.kotlity.network.dtos.BitcoinDollarExchangeRateDto

/**
 *  Helper function that maps dto object to its domain representation
 */
fun BitcoinDollarExchangeRateDto.toBitcoinDollarExchangeRate(): BitcoinDollarExchangeRate {
    return BitcoinDollarExchangeRate(rate = bpi.usd.rate)
}