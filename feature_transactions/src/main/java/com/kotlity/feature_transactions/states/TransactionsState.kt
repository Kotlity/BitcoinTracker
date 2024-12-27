package com.kotlity.feature_transactions.states

import androidx.compose.runtime.Immutable
import com.kotlity.feature_transactions.models.BitcoinBalanceUi
import com.kotlity.feature_transactions.models.DisplayableBitcoinFormat

/**
 *  Marks class as producing immutable instances
 */
@Immutable
data class TransactionsState(
    val isPopupMenuDisplayed: Boolean = false,
    val bitcoinBalance: BitcoinBalanceUi? = null,
    val bitcoinDollarExchangeRate: DisplayableBitcoinFormat? = null,
    val editableBalance: Float = 0f
)