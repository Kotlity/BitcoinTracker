package com.kotlity.feature_transactions.mappers

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

/**
 *  Format a long to displayable day, for example:
 *  12312312312312313 -> "15 December"
 */
fun Long.toDisplayableDay(
    instant: Instant = Instant.ofEpochMilli(this),
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault(),
    dayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM", locale)
): String {
    val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
    return zonedDateTime.format(dayFormatter)
}