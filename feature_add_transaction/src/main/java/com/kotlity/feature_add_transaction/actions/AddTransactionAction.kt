package com.kotlity.feature_add_transaction.actions

import com.kotlity.domain.models.Category

/**
 *  Defined all actions for AddTransaction screen
 */
sealed interface AddTransactionAction {

    data class OnUpdateTransactionBalance(val data: String): AddTransactionAction
    data class OnUpdateTransactionCategory(val category: Category): AddTransactionAction
    data class OnIsDropdownMenuExpandedUpdate(val isExpanded: Boolean): AddTransactionAction
    data object OnUpsertBitcoinBalance: AddTransactionAction

}