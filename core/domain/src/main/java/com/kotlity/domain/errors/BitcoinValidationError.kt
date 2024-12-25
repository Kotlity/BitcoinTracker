package com.kotlity.domain.errors

/**
 *  Sealed interface that represents errors while validating balance replenishment and transactions
 */
sealed interface BitcoinValidationError: Error {

    enum class BalanceReplenishment: BitcoinValidationError {
        BLANK,
        LESS_THAN_MINIMUM_VALUE,
        GREATER_THAN_MAXIMUM_VALUE
    }

    enum class Transaction: BitcoinValidationError {
        BLANK,
        LESS_THAN_MINIMUM_VALUE,
        GREATER_THAN_CURRENT_BALANCE,
        GREATER_THAN_MAXIMUM_VALUE
    }

}