package com.kotlity.presentation

import android.content.Context
import com.kotlity.domain.errors.BitcoinValidationError
import com.kotlity.domain.errors.DatabaseError
import com.kotlity.domain.errors.NetworkConnectionError
import com.kotlity.domain.errors.NetworkError
import com.kotlity.domain.models.NetworkStatus
import com.kotlity.resources.R.string

fun DatabaseError.toString(context: Context): String {
    val resId = when(this) {
        DatabaseError.ILLEGAL_STATE -> string.databaseIllegalStateException
        DatabaseError.SQLITE_CONSTRAINT -> string.sqliteConstraintException
        DatabaseError.SQLITE_EXCEPTION -> string.sqliteException
        DatabaseError.ILLEGAL_ARGUMENT -> string.errorIllegalArgument
        DatabaseError.UNKNOWN -> string.errorUnknown
    }
    return context.getString(resId)
}

fun NetworkError.toString(context: Context): String {
    val resId = when(this) {
        NetworkError.REQUEST_TIMEOUT -> string.errorRequestTimeout
        NetworkError.TOO_MANY_REQUESTS -> string.errorTooManyRequests
        NetworkError.NO_INTERNET -> string.errorNoInternet
        NetworkError.SERVER_ERROR -> string.errorUnknown
        NetworkError.SERIALIZATION -> string.errorSerialization
        NetworkError.UNKNOWN -> string.errorUnknown
    }
    return context.getString(resId)
}

fun NetworkStatus.toString(context: Context): String {
    return when(this) {
        NetworkStatus.HAS_INTERNET, NetworkStatus.IDLE -> ""
        NetworkStatus.NO_INTERNET -> context.getString(string.errorNoInternet)
    }
}

fun NetworkConnectionError.toString(context: Context): String {
    val resId = when(this) {
        NetworkConnectionError.SECURITY -> string.errorSecurity
        NetworkConnectionError.ILLEGAL_ARGUMENT -> string.errorIllegalArgument
        NetworkConnectionError.UNKNOWN -> string.errorUnknown
    }
    return context.getString(resId)
}

fun BitcoinValidationError.toString(context: Context): String {
    val resId = when(this) {
        BitcoinValidationError.BalanceReplenishment.BLANK -> string.validationBlank
        BitcoinValidationError.BalanceReplenishment.INVALID_VALUE -> string.invalidValue
        BitcoinValidationError.BalanceReplenishment.LESS_THAN_MINIMUM_VALUE -> string.lessThanMinimumValue
        BitcoinValidationError.BalanceReplenishment.GREATER_THAN_MAXIMUM_VALUE -> string.greaterThanMaximumValue

        BitcoinValidationError.Transaction.BLANK -> string.validationBlank
        BitcoinValidationError.Transaction.INVALID_VALUE -> string.invalidValue
        BitcoinValidationError.Transaction.LESS_THAN_MINIMUM_VALUE -> string.lessThanMinimumValue
        BitcoinValidationError.Transaction.GREATER_THAN_CURRENT_BALANCE -> string.greaterThanCurrentBalance
        BitcoinValidationError.Transaction.GREATER_THAN_MAXIMUM_VALUE -> string.greaterThanMaximumValue
    }
    return context.getString(resId)
}