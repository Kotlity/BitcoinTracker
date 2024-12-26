package com.kotlity.work

import com.kotlity.database.entities.BitcoinDollarExchangeRateEntity
import com.kotlity.domain.models.BitcoinDollarExchangeRate

fun BitcoinDollarExchangeRate.toBitcoinDollarExchangeRateEntity(): BitcoinDollarExchangeRateEntity {
    return BitcoinDollarExchangeRateEntity(rate = rate)
}