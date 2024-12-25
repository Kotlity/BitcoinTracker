package com.kotlity.network.di

import com.kotlity.domain.DispatcherHandler
import com.kotlity.network.DefaultDispatcherHandler
import org.koin.dsl.module

val dispatcherHandlerModule = module {
    single<DispatcherHandler> { DefaultDispatcherHandler() }
}