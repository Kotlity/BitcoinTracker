package com.kotlity.network.utils

import com.kotlity.domain.Response
import com.kotlity.domain.errors.NetworkError
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

/**
 *  Helper function that helps make calls via Ktor-Client
 */
suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Response<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return Response.Error(error = NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        return Response.Error(error = NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Response.Error(error = NetworkError.UNKNOWN)
    }
    return networkResponseToResponse<T>(response = response)
}