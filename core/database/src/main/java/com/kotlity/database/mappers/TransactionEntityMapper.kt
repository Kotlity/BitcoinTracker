package com.kotlity.database.mappers

import com.kotlity.database.entities.TransactionEntity
import com.kotlity.domain.models.Transaction

fun TransactionEntity.toTransaction(): Transaction {
    return Transaction(
        id = id,
        bitcoinAmount = bitcoinAmount,
        transactionAmount = transactionAmount,
        category = category,
        timestamp = timestamp
    )
}

fun Transaction.toTransactionEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        bitcoinAmount = bitcoinAmount,
        transactionAmount = transactionAmount,
        category = category,
        timestamp = timestamp
    )
}