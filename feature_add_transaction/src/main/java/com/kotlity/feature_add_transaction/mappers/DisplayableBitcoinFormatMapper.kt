package com.kotlity.feature_add_transaction.mappers

import android.icu.text.NumberFormat
import com.kotlity.feature_add_transaction.models.DisplayableBitcoinFormat
import java.util.Locale

/**
 *  Format a float to displayable format, for example:
 *  15.8345f -> "15.83"
 */
fun Float.toDisplayableBitcoinFormat(): DisplayableBitcoinFormat {
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 1
        maximumFractionDigits = 2
    }
    val formattedValue = numberFormat.format(this)

    return DisplayableBitcoinFormat(
        standardFormat = this,
        formatted = formattedValue
    )
}