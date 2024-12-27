package com.kotlity.domain

import com.kotlity.domain.errors.Error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

typealias error = Error

/**
 *  Sealed interface for processing responses, where:
 *  T is a generic type of data;
 *  E is a generic type of error
 */
sealed interface Response<out T, out E: error> {

    data class Success<out T>(val data: T): Response<T, Nothing>
    data class Error<out E: error>(val error: E): Response<Nothing, E>
}

/**
 *  Helper functions for simplified obtaining of the response
 */
inline fun <T, E: error> Response<T, E>.onSuccess(action: (T) -> Unit): Response<T, E> {
    return if (this is Response.Success) {
        action(data)
        this
    } else this
}

inline fun <T, E: error> Response<T, E>.onError(action: (E) -> Unit): Response<T, E> {
    return if (this is Response.Error) {
        action(error)
        this
    } else this
}

/**
 *  Helper functions for simplified obtaining of the response from a flow
 */
inline fun <T, E: error> Flow<Response<T, E>>.onSuccessFlow(crossinline action: suspend (T) -> Unit): Flow<Response<T, E>> {
    return onEach { response ->
        if (response is Response.Success) action(response.data)
    }
}

inline fun <T, E: error> Flow<Response<T, E>>.onErrorFlow(crossinline action: suspend (E) -> Unit): Flow<Response<T, E>> {
    return onEach { response ->
        if (response is Response.Error) action(response.error)
    }
}