package com.kotlity.bitcointracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kotlity.feature_add_transaction.navigation.AddTransactionDestination
import com.kotlity.feature_add_transaction.navigation.addTransactionScreen
import com.kotlity.feature_transactions.navigation.TransactionsDestination
import com.kotlity.feature_transactions.navigation.transactionsScreen

@Composable
fun BitcoinTrackerNavHost(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TransactionsDestination
    ) {
        transactionsScreen(
            onAddTransactionClick = { rate, balance ->
                val destination = AddTransactionDestination(rate, balance)
                navController.navigate(destination)
            },
            onShowSnackbar = onShowSnackbar
        )
        addTransactionScreen(
            onNavigateBack = {
                navController.navigateUp()
            },
            onShowSnackbar = onShowSnackbar
        )
    }
}