package com.kotlity.feature_add_transaction.validation

import com.kotlity.domain.errors.BitcoinValidationError
import com.kotlity.feature_add_transaction.states.ValidationStatus

private const val MINIMUM_VALUE = 1f
private const val MAXIMUM_VALUE = 1000000f

class TransactionValidator {

    operator fun invoke(data: String, currentBalance: Float): ValidationStatus {
        if (data.isBlank()) return ValidationStatus.Error(message = BitcoinValidationError.Transaction.BLANK)

        val value = data.toFloatOrNull() ?: return ValidationStatus.Error(message = BitcoinValidationError.Transaction.INVALID_VALUE)

        if (value < MINIMUM_VALUE) return ValidationStatus.Error(message = BitcoinValidationError.Transaction.LESS_THAN_MINIMUM_VALUE)
        if (value > MAXIMUM_VALUE) return ValidationStatus.Error(message = BitcoinValidationError.Transaction.GREATER_THAN_MAXIMUM_VALUE)
        if (value > currentBalance) return ValidationStatus.Error(message = BitcoinValidationError.Transaction.GREATER_THAN_MAXIMUM_VALUE)
        else return ValidationStatus.Success
    }
}