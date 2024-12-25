package com.kotlity.domain

import kotlinx.coroutines.CoroutineDispatcher


/**
 *  This interface is used to easily replace coroutine dispatchers in testing cases
 */
interface DispatcherHandler {

    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}