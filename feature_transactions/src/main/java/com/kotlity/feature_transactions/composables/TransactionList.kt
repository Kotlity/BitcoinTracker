package com.kotlity.feature_transactions.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.kotlity.feature_transactions.models.TransactionListItem
import com.kotlity.resources.R.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(dimensionResource(id = dimen._5dp)),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(dimensionResource(id = dimen._5dp)),
    transactions: LazyPagingItems<TransactionListItem>
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement
    ) {
        for (index in 0 until transactions.itemCount) {
            when(val peekingItem = transactions.peek(index)) { // Returns the presented item at the specified position, without notifying Paging of the item access that would normally trigger page loads
                is TransactionListItem.SeparatorItem -> stickyHeader(key = peekingItem.hashCode()) {
                    val header = transactions.get(index) as TransactionListItem.SeparatorItem
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(
                            modifier = Modifier.padding(dimensionResource(id = dimen._3dp)),
                            text = header.date,
                            color = MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
                is TransactionListItem.TransactionItem -> item(key = peekingItem.transaction.time.value) {
                    val isTransaction = peekingItem.transaction.category != null
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(id = dimen._100dp)),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = if (isTransaction) MaterialTheme.colorScheme.errorContainer else CardDefaults.cardColors().containerColor
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensionResource(id = dimen._5dp))
                        ) {
                            Row {
                                Text(text = (if (isTransaction) stringResource(id = string.bitcoinsSpent) else stringResource(id = string.bitcoinsReplenished))
                                        + " " + peekingItem.transaction.bitcoinAmount.toString())
                                Spacer(modifier = Modifier.width(dimensionResource(id = dimen._5dp)))
                                Text(text = "~ " + peekingItem.transaction.displayableTransactionAmount.formatted + "$")
                            }
                            peekingItem.transaction.category?.let {
                                Text(text = "Category: ${it.name.lowercase()}")
                            }
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                Text(text = peekingItem.transaction.time.formattedValue)
                            }
                        }
                    }
                }
            }
        }
    }
}