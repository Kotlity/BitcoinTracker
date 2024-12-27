package com.kotlity.feature_transactions.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.kotlity.feature_transactions.models.BitcoinBalanceUi
import com.kotlity.resources.R.*
import com.kotlity.resources.ResourcesConstant._16sp

@Composable
fun BitcoinBalanceSection(
    modifier: Modifier = Modifier,
    bitcoinBalanceUi: BitcoinBalanceUi?,
    onTopUpBalanceClick: () -> Unit
) {

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = string.balance),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = _16sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = dimen._3dp)))
            Text(
                text = (bitcoinBalanceUi?.amount?.formatted ?: "0") + " " + stringResource(id = string.pcs) + " ~ ",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = (bitcoinBalanceUi?.displayableBalance?.formatted ?: "0") + " $"
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = dimen._5dp)))
        }
        OutlinedButton(onClick = onTopUpBalanceClick) {
            Text(text = stringResource(id = string.topUpBalance))
        }
    }
}