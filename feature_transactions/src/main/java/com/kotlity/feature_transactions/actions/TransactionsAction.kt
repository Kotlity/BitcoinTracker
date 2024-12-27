package com.kotlity.feature_transactions.actions

import com.kotlity.feature_transactions.models.BitcoinBalanceUi

/**
 *  Defined all actions for Transactions screen
 */
sealed interface TransactionsAction {

    data class OnUpdateBitcoinBalance(val data: String): TransactionsAction
    data object OnUpsertBitcoinBalance: TransactionsAction
    data object OnLoadBitcoinBalance: TransactionsAction
    data object OnLoadBitcoinDollarExchangeRate: TransactionsAction
    data object OnPopupUpdate: TransactionsAction

}