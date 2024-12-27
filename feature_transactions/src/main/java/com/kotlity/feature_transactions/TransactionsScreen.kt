package com.kotlity.feature_transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kotlity.domain.errors.DatabaseError
import com.kotlity.feature_transactions.actions.TransactionsAction
import com.kotlity.feature_transactions.composables.BitcoinBalanceSection
import com.kotlity.feature_transactions.composables.BitcoinDollarExchangeRateSection
import com.kotlity.feature_transactions.composables.TopUpBalanceAlertDialog
import com.kotlity.feature_transactions.composables.TransactionList
import com.kotlity.feature_transactions.models.TransactionListItem
import com.kotlity.feature_transactions.states.TransactionsState
import com.kotlity.feature_transactions.states.ValidationStatus
import com.kotlity.presentation.Event
import com.kotlity.presentation.ObserveAsEvents
import com.kotlity.presentation.onError
import com.kotlity.presentation.toString
import com.kotlity.resources.R
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    transactionsViewModel: TransactionsViewModel = koinViewModel(),
    onAddTransactionClick: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit
) {

    val transactionsState by transactionsViewModel.state.collectAsStateWithLifecycle()
    val eventFlow = transactionsViewModel.eventFlow
    val transactions = transactionsViewModel.transactions.collectAsLazyPagingItems()
    val bitcoinBalanceValidationStatus = transactionsViewModel.bitcoinBalanceValidationStatus

    TransactionsContent(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .systemBarsPadding(),
        transactionsState = transactionsState,
        eventFlow = eventFlow,
        transactions = transactions,
        bitcoinBalanceValidationStatus = bitcoinBalanceValidationStatus,
        onAction = transactionsViewModel::onAction,
        onAddTransactionClick = onAddTransactionClick,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
fun TransactionsContent(
    modifier: Modifier = Modifier,
    transactionsState: TransactionsState,
    eventFlow: Flow<Event<Unit, DatabaseError>>,
    transactions: LazyPagingItems<TransactionListItem>,
    bitcoinBalanceValidationStatus: ValidationStatus,
    onAction: (TransactionsAction) -> Unit,
    onAddTransactionClick: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit
) {

    val context = LocalContext.current

    ObserveAsEvents(
        eventFlow,
    ) { event ->
        event.onError { error ->
            onShowSnackbar(error.toString(context))
        }
    }
    
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BitcoinBalanceSection(
                modifier = Modifier.padding(dimensionResource(id = R.dimen._10dp)),
                bitcoinBalanceUi = transactionsState.bitcoinBalance,
                onTopUpBalanceClick = {
                    onAction(TransactionsAction.OnPopupUpdate)
                }
            )
            BitcoinDollarExchangeRateSection(
                bitcoinDollarExchangeRate = transactionsState.bitcoinDollarExchangeRate
            )
        }
        OutlinedButton(onClick = onAddTransactionClick) {
            Text(text = stringResource(id = R.string.addTransaction))
        }
        TransactionList(
            modifier = Modifier.weight(1f),
            transactions = transactions
        )
    }
    if (transactionsState.isPopupMenuDisplayed) {
        TopUpBalanceAlertDialog(
            bitcoinDollarRate = transactionsState.bitcoinDollarExchangeRate?.standardFormat,
            balance = transactionsState.editableBalance.toString(),
            supportingText = if (bitcoinBalanceValidationStatus is ValidationStatus.Error) bitcoinBalanceValidationStatus.message.toString(context) else null,
            isEnabled = bitcoinBalanceValidationStatus is ValidationStatus.Success,
            isError = bitcoinBalanceValidationStatus is ValidationStatus.Error,
            onDismiss = {
                onAction(TransactionsAction.OnPopupUpdate)
            },
            onConfirm = {
                onAction(TransactionsAction.OnUpsertBitcoinBalance)
                onAction(TransactionsAction.OnPopupUpdate)
            },
            onBalanceChange = {
                onAction(TransactionsAction.OnUpdateBitcoinBalance(it))
            }
        )
    }
}