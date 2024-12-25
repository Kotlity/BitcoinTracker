package com.kotlity.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class BitcoinDollarExchangeRateDto(
    val bpi: BpiDto
)