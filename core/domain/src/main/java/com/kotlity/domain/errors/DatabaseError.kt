package com.kotlity.domain.errors

/**
 *  Enum class that represents database errors
 */
enum class DatabaseError: Error {
    ILLEGAL_STATE,
    SQLITE_CONSTRAINT,
    SQLITE_EXCEPTION,
    ILLEGAL_ARGUMENT,
    UNKNOWN
}