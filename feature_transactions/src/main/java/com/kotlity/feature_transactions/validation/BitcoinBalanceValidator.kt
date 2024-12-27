package com.kotlity.feature_transactions.validation

import com.kotlity.domain.errors.BitcoinValidationError
import com.kotlity.feature_transactions.states.ValidationStatus

private const val MINIMUM_VALUE = 0.1f
private const val MAXIMUM_VALUE = 1000f

class BitcoinBalanceValidator {

    operator fun invoke(data: String): ValidationStatus {
        if (data.isBlank()) return ValidationStatus.Error(message = BitcoinValidationError.BalanceReplenishment.BLANK)

        val value = data.toFloatOrNull() ?: return ValidationStatus.Error(message = BitcoinValidationError.BalanceReplenishment.INVALID_VALUE)

        if (value < MINIMUM_VALUE) return ValidationStatus.Error(message = BitcoinValidationError.BalanceReplenishment.LESS_THAN_MINIMUM_VALUE)
        if (value > MAXIMUM_VALUE) return ValidationStatus.Error(message = BitcoinValidationError.BalanceReplenishment.GREATER_THAN_MAXIMUM_VALUE)
        else return ValidationStatus.Success
    }
}