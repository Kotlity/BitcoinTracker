package com.kotlity.feature_add_transaction.models

data class BitcoinBalanceUi(
    val amount: DisplayableBitcoinFormat,
    val displayableBalance: DisplayableBitcoinFormat
)
