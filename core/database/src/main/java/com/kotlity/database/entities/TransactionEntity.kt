package com.kotlity.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kotlity.domain.models.Category

@Entity
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val bitcoinAmount: Float, // the amount of Bitcoins replenished or spent
    val transactionAmount: Float, // the amount of replenishment or expenditure of Bitcoin at the dollar exchange rate
    val category: Category?,
    val timestamp: Long // transaction time is represented in long format for easy formatting for the UI
)