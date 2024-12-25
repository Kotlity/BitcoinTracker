package com.kotlity.domain.errors

/**
 *  Enum class that represents network connection errors while defining
 */
enum class NetworkConnectionError: Error {
    SECURITY,
    ILLEGAL_ARGUMENT,
    UNKNOWN
}