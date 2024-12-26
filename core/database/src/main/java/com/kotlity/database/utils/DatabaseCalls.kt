package com.kotlity.database.utils

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.kotlity.domain.Response
import com.kotlity.domain.errors.DatabaseError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 *  Helper function that helps to make database calls and catches database exceptions
 */
suspend inline fun <reified T> databaseCall(
    dispatcher: CoroutineDispatcher,
    crossinline call: suspend () -> Response.Success<T>
): Response<T, DatabaseError> {
    return withContext(dispatcher) {
        try {
            call()
        } catch (e: IllegalStateException) {
            Response.Error(error = DatabaseError.ILLEGAL_STATE)
        } catch (e: IllegalArgumentException) {
            Response.Error(error = DatabaseError.ILLEGAL_ARGUMENT)
        } catch (e: SQLiteConstraintException) {
            Response.Error(error = DatabaseError.SQLITE_CONSTRAINT)
        } catch (e: SQLiteException) {
            Response.Error(error = DatabaseError.SQLITE_EXCEPTION)
        } catch (e: Exception) {
            Response.Error(error = DatabaseError.UNKNOWN)
        }
    }
}

/**
 *  Helper function that helps to observe database data and catches database exceptions
 */
inline fun <reified T, reified R> databaseFlowCall(
    dispatcher: CoroutineDispatcher,
    crossinline flowProvider: () -> Flow<T>,
    crossinline mapper: (T) -> R
): Flow<Response<R, DatabaseError>> {
    return flow<Response<R, DatabaseError>> {
        flowProvider()
            .map { mapper(it) }
            .collect {
                val result = Response.Success(it)
                emit(result)
            }
    }
        .catch { throwable ->
            val error = when(throwable) {
                is IllegalStateException -> DatabaseError.ILLEGAL_STATE
                is SQLiteConstraintException -> DatabaseError.SQLITE_CONSTRAINT
                is SQLiteException -> DatabaseError.SQLITE_EXCEPTION
                is IllegalArgumentException -> DatabaseError.ILLEGAL_ARGUMENT
                else -> DatabaseError.UNKNOWN
            }
            emit(Response.Error(error))
        }
        .flowOn(dispatcher)
}