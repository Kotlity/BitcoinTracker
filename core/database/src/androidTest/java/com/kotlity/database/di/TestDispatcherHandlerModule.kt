package com.kotlity.database.di

import com.kotlity.database.TestDispatcherHandler
import com.kotlity.domain.DispatcherHandler
import org.koin.dsl.module

val testDispatcherHandlerModule = module {
    factory<DispatcherHandler> { TestDispatcherHandler() }
}