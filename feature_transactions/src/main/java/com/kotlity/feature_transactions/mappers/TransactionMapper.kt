package com.kotlity.feature_transactions.mappers

import com.kotlity.domain.models.Transaction
import com.kotlity.feature_transactions.models.TransactionUi

fun Transaction.toTransactionUi(): TransactionUi {
    return TransactionUi(
        id = id ?: 0,
        bitcoinAmount = bitcoinAmount,
        displayableTransactionAmount = transactionAmount.toDisplayableBitcoinFormat(),
        category = category,
        time = timestamp.toDisplayableTime()
    )
}

fun TransactionUi.toTransaction(): Transaction {
    return Transaction(
        bitcoinAmount = bitcoinAmount,
        transactionAmount = displayableTransactionAmount.standardFormat,
        category = category,
        timestamp = time.value
    )
}