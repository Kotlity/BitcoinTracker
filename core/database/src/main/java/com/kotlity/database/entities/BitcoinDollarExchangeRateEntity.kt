package com.kotlity.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BitcoinDollarExchangeRateEntity(
    @PrimaryKey
    val id: Int = 1, // always 1, since the table should only store one record
    val rate: Float // Bitcoin exchange rate against the dollar
)