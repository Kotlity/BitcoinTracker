package com.kotlity.feature_transactions.models

import com.kotlity.domain.models.Category

data class TransactionUi(
    val id: Long? = null,
    val bitcoinAmount: Float,
    val displayableTransactionAmount: DisplayableBitcoinFormat,
    val category: Category?,
    val time: DisplayableTime
)