package com.kotlity.domain.models

/**
 *  TransactionEntity representation that restricts its directly usage in the presentation layer
 */
data class Transaction(
    val id: Long? = null,
    val bitcoinAmount: Float,
    val transactionAmount: Float,
    val category: Category?,
    val timestamp: Long
)
