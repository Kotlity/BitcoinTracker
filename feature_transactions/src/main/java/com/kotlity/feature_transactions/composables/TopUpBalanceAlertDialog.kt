package com.kotlity.feature_transactions.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.kotlity.resources.R.*

@Composable
fun TopUpBalanceAlertDialog(
    modifier: Modifier = Modifier,
    bitcoinDollarRate: Float?,
    balance: String,
    supportingText: String?,
    isEnabled: Boolean,
    isError: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onBalanceChange: (String) -> Unit
) {

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = isEnabled
            ) {
                Text(text = stringResource(id = string.topUpBalance))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(id = string.cancel),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = string.topUpBalanceTitle),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            TopUpBalanceEditableSection(
                modifier = Modifier.fillMaxWidth(),
                bitcoinDollarRate = bitcoinDollarRate,
                balance = balance,
                supportingText = supportingText,
                isError = isError,
                onBalanceChange = onBalanceChange
            )
        }
    )
}

@Composable
private fun TopUpBalanceEditableSection(
    modifier: Modifier,
    bitcoinDollarRate: Float?,
    balance: String,
    supportingText: String?,
    isError: Boolean,
    onBalanceChange: (String) -> Unit
) {

    val totalValue = (bitcoinDollarRate?.let { balance.toFloat() * it }?.toString() ?: "0") + "$"

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = string.topUpBalanceDescription),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = dimen._5dp)))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = string.totalValue))
            Text(text = totalValue)
        }
        OutlinedTextField(
            value = balance,
            onValueChange = onBalanceChange,
            supportingText = {
                supportingText?.let {
                    Text(text = supportingText)
                }
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
    }
}