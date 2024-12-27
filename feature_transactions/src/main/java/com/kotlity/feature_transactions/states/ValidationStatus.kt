package com.kotlity.feature_transactions.states

import com.kotlity.domain.errors.BitcoinValidationError

sealed interface ValidationStatus {

    data class Error(val message: BitcoinValidationError.BalanceReplenishment): ValidationStatus
    data object Success: ValidationStatus
    data object Unspecified: ValidationStatus
}