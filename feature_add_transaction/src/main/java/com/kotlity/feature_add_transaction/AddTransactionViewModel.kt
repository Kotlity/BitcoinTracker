package com.kotlity.feature_add_transaction

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kotlity.domain.errors.DatabaseError
import com.kotlity.domain.models.Category
import com.kotlity.domain.onError
import com.kotlity.feature_add_transaction.actions.AddTransactionAction
import com.kotlity.feature_add_transaction.mappers.toBitcoinBalance
import com.kotlity.feature_add_transaction.mappers.toDisplayableBitcoinFormat
import com.kotlity.feature_add_transaction.mappers.toDisplayableTime
import com.kotlity.feature_add_transaction.mappers.toTransaction
import com.kotlity.feature_add_transaction.models.BitcoinBalanceUi
import com.kotlity.feature_add_transaction.models.TransactionUi
import com.kotlity.feature_add_transaction.navigation.AddTransactionDestination
import com.kotlity.feature_add_transaction.states.AddTransactionState
import com.kotlity.feature_add_transaction.states.ValidationStatus
import com.kotlity.feature_add_transaction.validation.TransactionValidator
import com.kotlity.presentation.Event
import com.kotlity.presentation.toStateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    private val addTransactionRepository: AddTransactionRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val args = savedStateHandle.toRoute<AddTransactionDestination>()
    private val bitcoinDollarExchangeRate = args.rate
    private val balance = args.balance

    private val _state: MutableStateFlow<AddTransactionState> = MutableStateFlow(AddTransactionState(bitcoinDollarExchangeRate, balance))
    val state = _state
        .toStateFlow(
            scope = viewModelScope,
            initialValue = AddTransactionState(bitcoinDollarExchangeRate, balance)
        )

    var transactionBalanceValidationStatus by derivedStateOf {
        mutableStateOf<ValidationStatus>(ValidationStatus.Unspecified)
    }.value
        private set

    private val _eventChannel = Channel<Event<Unit, DatabaseError>>()
    val eventFlow = _eventChannel.receiveAsFlow()

    fun onAction(action: AddTransactionAction) {
        when(action) {
            is AddTransactionAction.OnUpdateTransactionBalance -> onUpdateTransactionBalance(action.data)
            is AddTransactionAction.OnUpdateTransactionCategory -> onUpdateTransactionCategory(action.category)
            is AddTransactionAction.OnIsDropdownMenuExpandedUpdate -> onIsDropdownMenuExpandedUpdate(action.isExpanded)
            AddTransactionAction.OnUpsertBitcoinBalance -> onUpsertBitcoinBalance()
        }
    }

    private fun onUpdateTransactionBalance(data: String) {
        val validationStatus = TransactionValidator().invoke(data, balance)
        transactionBalanceValidationStatus = validationStatus

        val transactionBalance =  data.toFloatOrNull() ?: 0f
        val transactionRate = transactionBalance / _state.value.bitcoinDollarExchangeRate

        _state.update {
            it.copy(
                editableTransactionBalance = transactionBalance,
                editableTransactionRate = transactionRate
            )
        }
    }

    private fun onUpdateTransactionCategory(data: Category) {
        _state.update {
            it.copy(editableCategory = data)
        }
    }

    private fun onIsDropdownMenuExpandedUpdate(isExpanded: Boolean) {
        _state.update {
            it.copy(isDropdownMenuExpanded = isExpanded)
        }
    }

    /**
     *  Function that calculates balance and transaction data
     */
    private fun onUpsertBitcoinBalance() {
        viewModelScope.launch {
            val currentBitcoinBalance = _state.value.balance
            val currentTypedTransactionBalance = _state.value.editableTransactionBalance
            val currentTypedTransactionRate = _state.value.editableTransactionRate
            val currentBitcoinExchangeRate = _state.value.bitcoinDollarExchangeRate
            val currentCategory = _state.value.editableCategory

            val currentBitcoinsAmount = currentBitcoinBalance / currentBitcoinExchangeRate
            val updatedAmount = currentBitcoinsAmount - currentTypedTransactionRate
            val updatedTransactionAmount = currentTypedTransactionRate * currentBitcoinExchangeRate

            val updatedBitcoinBalance = currentBitcoinBalance - currentTypedTransactionBalance
            val updatedDisplayableAmount = updatedAmount.toDisplayableBitcoinFormat()
            val updatedDisplayableBalance = updatedBitcoinBalance.toDisplayableBitcoinFormat()

            val updatedBitcoinBalanceUi = BitcoinBalanceUi(amount = updatedDisplayableAmount, displayableBalance = updatedDisplayableBalance)

            addTransactionRepository.upsertBitcoinBalance(entity = updatedBitcoinBalanceUi.toBitcoinBalance())
                .onError { error ->
                    sendErrorToChannel(error = error)
                    return@launch
                }

            val transactionUi = TransactionUi(
                bitcoinAmount = currentTypedTransactionRate,
                displayableTransactionAmount = updatedTransactionAmount.toDisplayableBitcoinFormat(),
                category = currentCategory,
                time = System.currentTimeMillis().toDisplayableTime()
            )

            addTransactionRepository.addTransaction(data = transactionUi.toTransaction())
                .onError { error ->
                    sendErrorToChannel(error = error)
                }
        }
    }

    private suspend fun sendErrorToChannel(error: DatabaseError) {
        _eventChannel.send(Event.Error(error))
    }
}