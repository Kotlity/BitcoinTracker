package com.kotlity.database.mappers

import com.kotlity.database.entities.BitcoinDollarExchangeRateEntity
import com.kotlity.domain.models.BitcoinDollarExchangeRate

fun BitcoinDollarExchangeRateEntity.toBitcoinDollarExchangeRate(): BitcoinDollarExchangeRate {
    return BitcoinDollarExchangeRate(rate = rate)
}