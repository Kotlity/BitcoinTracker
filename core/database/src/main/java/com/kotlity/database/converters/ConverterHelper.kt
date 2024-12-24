package com.kotlity.database.converters

/**
 *  Inline function that helps convert objects to a supported type for the Room database and vice versa
 */
internal inline fun <O> converterHelper(converterBlock: () -> O): O {
    return try {
        converterBlock()
    } catch (e: Exception) {
        throw ConverterException("An error occurred while trying to convert Category enum: ${e.message.toString()}")
    }
}