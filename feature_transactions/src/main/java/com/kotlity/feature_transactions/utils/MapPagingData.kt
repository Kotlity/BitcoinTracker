package com.kotlity.feature_transactions.utils

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import androidx.paging.PagingData
import androidx.paging.map
import com.kotlity.domain.Response
import com.kotlity.domain.errors.DatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 *  Helper function that maps PagingData to PagingData<Response>, also catches exceptions
 */
internal inline fun <T : Any, R : Any> Flow<PagingData<T>>.mapPagingData(
    crossinline mapper: (T) -> Response<R, DatabaseError>
): Flow<PagingData<Response<R, DatabaseError>>> {
    return this
        .map { pagingData ->
            pagingData.map { item ->
                try {
                    mapper(item)
                } catch (e: IllegalStateException) {
                    Response.Error(DatabaseError.ILLEGAL_STATE)
                } catch (e: IllegalArgumentException) {
                    Response.Error(DatabaseError.ILLEGAL_ARGUMENT)
                } catch (e: SQLiteConstraintException) {
                    Response.Error(DatabaseError.SQLITE_CONSTRAINT)
                } catch (e: SQLiteException) {
                    Response.Error(DatabaseError.SQLITE_EXCEPTION)
                } catch (e: Exception) {
                    Response.Error(DatabaseError.UNKNOWN)
                }
            }
        }
        .catch { throwable ->
            val error = when(throwable) {
                is IllegalStateException -> DatabaseError.ILLEGAL_STATE
                is IllegalArgumentException -> DatabaseError.ILLEGAL_ARGUMENT
                is SQLiteConstraintException -> DatabaseError.SQLITE_CONSTRAINT
                is SQLiteException -> DatabaseError.SQLITE_EXCEPTION
                else -> DatabaseError.UNKNOWN

            }
            emit(
                PagingData.from(
                    listOf(Response.Error(error = error))
                )
            )
        }
}