package com.kotlity.feature_transactions

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.kotlity.domain.errors.DatabaseError
import com.kotlity.domain.onError
import com.kotlity.domain.onErrorFlow
import com.kotlity.domain.onSuccessFlow
import com.kotlity.feature_transactions.actions.TransactionsAction
import com.kotlity.feature_transactions.mappers.toBitcoinBalance
import com.kotlity.feature_transactions.mappers.toBitcoinBalanceUi
import com.kotlity.feature_transactions.mappers.toDisplayableBitcoinFormat
import com.kotlity.feature_transactions.mappers.toDisplayableDay
import com.kotlity.feature_transactions.mappers.toDisplayableTime
import com.kotlity.feature_transactions.mappers.toTransaction
import com.kotlity.feature_transactions.mappers.toTransactionUi
import com.kotlity.feature_transactions.models.BitcoinBalanceUi
import com.kotlity.feature_transactions.models.TransactionListItem
import com.kotlity.feature_transactions.models.TransactionUi
import com.kotlity.feature_transactions.states.TransactionsState
import com.kotlity.feature_transactions.states.ValidationStatus
import com.kotlity.feature_transactions.validation.BitcoinBalanceValidator
import com.kotlity.presentation.Event
import com.kotlity.presentation.toStateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionsViewModel(private val transactionsRepository: TransactionsRepository): ViewModel() {

    private val _state: MutableStateFlow<TransactionsState> = MutableStateFlow(TransactionsState())
    val state = _state
        .onStart {
            onAction(TransactionsAction.OnLoadBitcoinDollarExchangeRate)
            onAction(TransactionsAction.OnLoadBitcoinBalance)
        }
        .toStateFlow(
            scope = viewModelScope,
            initialValue = TransactionsState()
        )

    var bitcoinBalanceValidationStatus by derivedStateOf {
        mutableStateOf<ValidationStatus>(ValidationStatus.Unspecified)
    }.value
        private set

    val transactions: Flow<PagingData<TransactionListItem>> = transactionsRepository.loadAllTransactions()
        .map { pagingData -> pagingData.map { transaction -> TransactionListItem.TransactionItem(transaction = transaction.toTransactionUi()) }
        .insertSeparators { before: TransactionListItem.TransactionItem?, after: TransactionListItem.TransactionItem? -> // Logic for inserting separators(day headers)
            if (after == null) return@insertSeparators null // Last item

            val afterDate = after.transaction.time.value.toDisplayableDay()
            if (before == null) return@insertSeparators TransactionListItem.SeparatorItem(date = afterDate) // First item

            val beforeDate = before.transaction.time.value.toDisplayableDay()
            if (beforeDate != afterDate) return@insertSeparators TransactionListItem.SeparatorItem(date = afterDate) // Different days
            else null
        }
        }
        .cachedIn(viewModelScope)

    private val _eventChannel = Channel<Event<Unit, DatabaseError>>()
    val eventFlow = _eventChannel.receiveAsFlow()

    fun onAction(action: TransactionsAction) {
        when(action) {
            is TransactionsAction.OnUpdateBitcoinBalance -> onUpdateBitcoinBalance(action.data)
            is TransactionsAction.OnUpsertBitcoinBalance -> onUpsertBitcoinBalance()
            TransactionsAction.OnLoadBitcoinBalance -> onLoadBitcoinBalance()
            TransactionsAction.OnLoadBitcoinDollarExchangeRate -> onLoadBitcoinDollarExchangeRate()
            TransactionsAction.OnPopupUpdate -> onPopupUpdate()
        }
    }

    private fun onUpdateBitcoinBalance(data: String) {
        val validationStatus = BitcoinBalanceValidator().invoke(data)
        bitcoinBalanceValidationStatus = validationStatus
        _state.update {
            it.copy(editableBalance = data.toFloatOrNull() ?: 0f)
        }
    }

    private fun onUpsertBitcoinBalance() {
        viewModelScope.launch {
            val currentBitcoinBalance = _state.value.bitcoinBalance?.amount?.standardFormat
            val currentTypedAmount = _state.value.editableBalance
            val currentBitcoinExchangeRate = _state.value.bitcoinDollarExchangeRate

            val updatedAmount = currentBitcoinBalance?.let { it + currentTypedAmount } ?: currentTypedAmount

            val totalBalance = currentBitcoinExchangeRate?.standardFormat?.let { it * updatedAmount } ?: updatedAmount
            val updatedDisplayableBalance = totalBalance.toDisplayableBitcoinFormat()

            val updatedBitcoinBalance = BitcoinBalanceUi(amount = updatedAmount.toDisplayableBitcoinFormat(), displayableBalance = updatedDisplayableBalance)

            transactionsRepository.upsertBitcoinBalance(entity = updatedBitcoinBalance.toBitcoinBalance())
                .onError { error ->
                    sendErrorToChannel(error = error)
                    return@launch
                }

            val totalTransaction = currentBitcoinExchangeRate?.standardFormat?.let { it * currentTypedAmount } ?: currentTypedAmount
            val transactionUi = TransactionUi(
                bitcoinAmount = currentTypedAmount,
                displayableTransactionAmount = totalTransaction.toDisplayableBitcoinFormat(),
                category = null,
                time = System.currentTimeMillis().toDisplayableTime()
            )

            transactionsRepository.addTransaction(data = transactionUi.toTransaction())
                .onError { error ->
                    sendErrorToChannel(error = error)
                }
        }
    }

    private fun onLoadBitcoinBalance() {
        transactionsRepository.loadBitcoinBalance()
            .onSuccessFlow { balance ->
                val bitcoinBalanceUi = balance?.toBitcoinBalanceUi()
                _state.update {
                    it.copy(bitcoinBalance = bitcoinBalanceUi)
                }
            }
            .onErrorFlow { error ->
                sendErrorToChannel(error)
            }
            .launchIn(viewModelScope)
    }

    private fun onLoadBitcoinDollarExchangeRate() {
        transactionsRepository.loadBitcoinDollarExchangeRate()
            .onSuccessFlow { rate ->
                _state.update {
                    it.copy(bitcoinDollarExchangeRate = rate?.rate?.toDisplayableBitcoinFormat())
                }
            }
            .onErrorFlow { error ->
                sendErrorToChannel(error)
            }
            .launchIn(viewModelScope)
    }

    private fun onPopupUpdate() {
        _state.update {
            val previousIsPopupMenuDisplayedState = _state.value.isPopupMenuDisplayed
            it.copy(
                isPopupMenuDisplayed = !previousIsPopupMenuDisplayedState,
                editableBalance = 0f
            )
        }
        bitcoinBalanceValidationStatus = ValidationStatus.Unspecified
    }

    private suspend fun sendErrorToChannel(error: DatabaseError) {
        _eventChannel.send(Event.Error(error))
    }
}