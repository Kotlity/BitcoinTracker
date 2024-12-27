package com.kotlity.feature_transactions.models

data class BitcoinBalanceUi(
    val amount: DisplayableBitcoinFormat,
    val displayableBalance: DisplayableBitcoinFormat
)
