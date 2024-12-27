package com.kotlity.feature_transactions.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kotlity.feature_transactions.models.DisplayableBitcoinFormat
import com.kotlity.resources.R.*

@Composable
fun BitcoinDollarExchangeRateSection(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    bitcoinDollarExchangeRate: DisplayableBitcoinFormat?
) {

    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        Text(text = stringResource(id = string.bitcoinRate))
        Text(text = ((bitcoinDollarExchangeRate?.formatted) ?: "0") + "$")
    }
}