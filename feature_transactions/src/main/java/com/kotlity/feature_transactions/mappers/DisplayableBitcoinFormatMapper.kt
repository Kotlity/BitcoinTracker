package com.kotlity.feature_transactions.mappers

import android.icu.text.NumberFormat
import com.kotlity.feature_transactions.models.DisplayableBitcoinFormat
import com.kotlity.feature_transactions.utils.Constants.MAX_FRACTION_DIGITS
import com.kotlity.feature_transactions.utils.Constants.MIN_FRACTION_DIGITS
import java.util.Locale

/**
 *  Format a float to displayable format, for example:
 *  15.8345f -> "15.83"
 */
fun Float.toDisplayableBitcoinFormat(): DisplayableBitcoinFormat {
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = MIN_FRACTION_DIGITS
        maximumFractionDigits = MAX_FRACTION_DIGITS
    }
    val formattedValue = numberFormat.format(this)

    return DisplayableBitcoinFormat(
        standardFormat = this,
        formatted = formattedValue
    )
}