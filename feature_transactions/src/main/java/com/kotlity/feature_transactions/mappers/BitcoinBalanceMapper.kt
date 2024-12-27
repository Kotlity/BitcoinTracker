package com.kotlity.feature_transactions.mappers

import com.kotlity.domain.models.BitcoinBalance
import com.kotlity.feature_transactions.models.BitcoinBalanceUi

fun BitcoinBalance.toBitcoinBalanceUi(): BitcoinBalanceUi {
    return BitcoinBalanceUi(
        amount = amount.toDisplayableBitcoinFormat(),
        displayableBalance = balance.toDisplayableBitcoinFormat()
    )
}

fun BitcoinBalanceUi.toBitcoinBalance(): BitcoinBalance {
    return BitcoinBalance(
        amount = amount.standardFormat,
        balance = displayableBalance.standardFormat
    )
}