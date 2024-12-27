package com.kotlity.feature_transactions.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kotlity.feature_transactions.TransactionsScreen
import kotlinx.serialization.Serializable

@Serializable
object TransactionsDestination

fun NavGraphBuilder.transactionsScreen(
    onAddTransactionClick: (rate: Float, balance: Float) -> Unit,
    onShowSnackbar: suspend (String) -> Unit
) {
    composable<TransactionsDestination> {
        TransactionsScreen(
            onAddTransactionClick = onAddTransactionClick,
            onShowSnackbar = onShowSnackbar
        )
    }
}