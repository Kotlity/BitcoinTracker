package com.kotlity.network.utils

import com.kotlity.domain.Response
import com.kotlity.domain.errors.NetworkError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 *  Helper function that handles HttpResponse and maps it to application Response
 */
suspend inline fun <reified T> networkResponseToResponse(
    response: HttpResponse
): Response<T, NetworkError> {
    return when(response.status.value) {
        in 200..299 -> tryToDeserializeData(response = response)
        408 -> Response.Error(error = NetworkError.REQUEST_TIMEOUT)
        429 -> Response.Error(error = NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Response.Error(error = NetworkError.SERVER_ERROR)
        else -> Response.Error(error = NetworkError.UNKNOWN)
    }
}

suspend inline fun <reified T> tryToDeserializeData(response: HttpResponse): Response<T, NetworkError> {
    return try {
        val data = response.body<T>()
        Response.Success(data = data)
    } catch (e: NoTransformationFoundException) {
        Response.Error(error = NetworkError.SERIALIZATION)
    }
}