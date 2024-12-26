package com.kotlity.database.utils

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.kotlity.domain.Response
import com.kotlity.domain.errors.DatabaseError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 *  Helper function that catches database exceptions
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