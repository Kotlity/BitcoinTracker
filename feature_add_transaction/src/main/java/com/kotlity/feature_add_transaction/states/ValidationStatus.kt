package com.kotlity.feature_add_transaction.states

import com.kotlity.domain.errors.BitcoinValidationError

sealed interface ValidationStatus {

    data class Error(val message: BitcoinValidationError.Transaction): ValidationStatus
    data object Success: ValidationStatus
    data object Unspecified: ValidationStatus
}