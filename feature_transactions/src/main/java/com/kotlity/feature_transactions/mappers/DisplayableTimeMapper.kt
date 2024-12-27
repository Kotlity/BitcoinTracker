package com.kotlity.feature_transactions.mappers

import com.kotlity.feature_transactions.models.DisplayableTime
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

/**
 *  Format Unix timestamp to displayable format, for example:
 *  1672534800000L -> "12:00:00"
 */
fun Long.toDisplayableTime(
    instant: Instant = Instant.ofEpochMilli(this),
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault(),
    timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", locale)
): DisplayableTime {
    val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
    val formattedValue = zonedDateTime.format(timeFormatter)

    return DisplayableTime(
        value = this,
        formattedValue = formattedValue
    )
}