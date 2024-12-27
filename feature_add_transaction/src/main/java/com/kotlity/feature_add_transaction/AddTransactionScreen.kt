package com.kotlity.feature_add_transaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kotlity.domain.errors.DatabaseError
import com.kotlity.feature_add_transaction.actions.AddTransactionAction
import com.kotlity.feature_add_transaction.composables.CategoriesDropdownMenu
import com.kotlity.feature_add_transaction.states.AddTransactionState
import com.kotlity.feature_add_transaction.states.ValidationStatus
import com.kotlity.presentation.Event
import com.kotlity.presentation.ObserveAsEvents
import com.kotlity.presentation.onError
import com.kotlity.presentation.toString
import com.kotlity.resources.R.*
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddTransactionScreen(
    modifier: Modifier = Modifier,
    addTransactionViewModel: AddTransactionViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit
) {

    val addTransactionState by addTransactionViewModel.state.collectAsStateWithLifecycle()
    val eventFlow = addTransactionViewModel.eventFlow
    val transactionBalanceValidationStatus = addTransactionViewModel.transactionBalanceValidationStatus

    AddTransactionContent(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .systemBarsPadding(),
        addTransactionState = addTransactionState,
        eventFlow = eventFlow,
        transactionBalanceValidationStatus = transactionBalanceValidationStatus,
        onAction = addTransactionViewModel::onAction,
        onNavigateBack = onNavigateBack,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
private fun AddTransactionContent(
    modifier: Modifier = Modifier,
    addTransactionState: AddTransactionState,
    eventFlow: Flow<Event<Unit, DatabaseError>>,
    transactionBalanceValidationStatus: ValidationStatus,
    onAction: (AddTransactionAction) -> Unit,
    onNavigateBack: () -> Unit,
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

    Box(modifier = modifier) {
        IconButton(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onNavigateBack
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = dimensionResource(id = dimen._10dp)),
                text = stringResource(id = string.transactionTitle),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = dimen._25dp)))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = string.transactionDescription),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(text = stringResource(id = string.totalValue) + " ${addTransactionState.editableTransactionRate} " + stringResource(id = string.pcs))
            }
            Text(text = stringResource(id = string.balance) + " ${addTransactionState.balance}$")
            OutlinedTextField(
                value = addTransactionState.editableTransactionBalance.toString(),
                onValueChange = {
                    onAction(AddTransactionAction.OnUpdateTransactionBalance(it))
                },
                supportingText = {
                    if (transactionBalanceValidationStatus is ValidationStatus.Error) {
                        Text(text = transactionBalanceValidationStatus.message.toString(context))
                    }
                },
                isError = transactionBalanceValidationStatus is ValidationStatus.Error,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = dimen._10dp)))
            Text(
                text = stringResource(id = string.selectedCategory),
                style = MaterialTheme.typography.bodyMedium
            )
            CategoriesDropdownMenu(
                selectedCategory = addTransactionState.editableCategory,
                expanded = addTransactionState.isDropdownMenuExpanded,
                onExpandedChange = {
                    onAction(AddTransactionAction.OnIsDropdownMenuExpandedUpdate(isExpanded = it))
                },
                onSelectedCategory = { category ->
                    onAction(AddTransactionAction.OnUpdateTransactionCategory(category = category))
                },
                onDismiss = {
                    onAction(AddTransactionAction.OnIsDropdownMenuExpandedUpdate(isExpanded = false))
                }
            )
        }
        OutlinedButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.6f)
                .padding(bottom = dimensionResource(id = dimen._5dp)),
            enabled = transactionBalanceValidationStatus is ValidationStatus.Success,
            onClick = {
                onAction(AddTransactionAction.OnUpsertBitcoinBalance)
                onNavigateBack()
            }
        ) {
            Text(
                text = stringResource(id = string.add),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}