package com.kotlity.domain.models

/**
 *  BitcoinBalanceEntity representation that restricts its directly usage in the presentation layer
 */
data class BitcoinBalance(
    val amount: Float,
    val balance: Float
)
