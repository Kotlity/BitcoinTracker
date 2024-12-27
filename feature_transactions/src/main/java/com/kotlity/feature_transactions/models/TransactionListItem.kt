package com.kotlity.feature_transactions.models

interface TransactionListItem {

    data class TransactionItem(val transaction: TransactionUi): TransactionListItem
    data class SeparatorItem(val date: String): TransactionListItem
}