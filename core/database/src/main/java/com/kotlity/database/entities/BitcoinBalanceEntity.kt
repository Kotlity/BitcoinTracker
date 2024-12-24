package com.kotlity.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BitcoinBalanceEntity(
    @PrimaryKey
    val id: Int = 1, // always 1, since the table should only store one record
    val amount: Float, // current Bitcoin balance at the dollar exchange rate
    val balance: Float // total Bitcoin balance
)