package com.kotlity.database.mappers

import com.kotlity.database.entities.BitcoinBalanceEntity
import com.kotlity.domain.models.BitcoinBalance

fun BitcoinBalanceEntity.toBitcoinBalance(): BitcoinBalance {
    return BitcoinBalance(
        amount = amount,
        balance = balance
    )
}