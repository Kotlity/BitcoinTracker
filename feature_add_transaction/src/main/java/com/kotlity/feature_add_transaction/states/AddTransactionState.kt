package com.kotlity.feature_add_transaction.states

import androidx.compose.runtime.Immutable
import com.kotlity.domain.models.Category

/**
 *  Marks class as producing immutable instances
 */
@Immutable
data class AddTransactionState(
    val bitcoinDollarExchangeRate: Float,
    val balance: Float,
    val editableTransactionRate: Float = 0f,
    val editableTransactionBalance: Float = 0f,
    val editableCategory: Category = Category.GROCERIES,
    val isDropdownMenuExpanded: Boolean = false
)
