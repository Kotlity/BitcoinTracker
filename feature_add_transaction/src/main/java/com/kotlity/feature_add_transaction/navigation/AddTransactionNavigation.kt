package com.kotlity.feature_add_transaction.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kotlity.feature_add_transaction.AddTransactionScreen
import kotlinx.serialization.Serializable

@Serializable
data class AddTransactionDestination(val rate: Float, val balance: Float)

fun NavGraphBuilder.addTransactionScreen(
    onNavigateBack: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit
) {
    composable<AddTransactionDestination> {
        AddTransactionScreen(
            onNavigateBack = onNavigateBack,
            onShowSnackbar = onShowSnackbar
        )
    }
}