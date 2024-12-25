package com.kotlity.network.di

import com.kotlity.network.KtorClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.http.ContentType
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private fun configureKtorClient(): HttpClient {
    return KtorClient.KtorClientBuilder()
        .engine(CIO.create())
        .logging(LogLevel.ALL, Logger.DEFAULT)
        .contentNegotiation(prettyPrint = true, ignoreUnknownKeys = true)
        .defaultContentType(ContentType.Application.Json)
        .build()
        .client
}

val ktorClientModule = module {
    singleOf(::configureKtorClient)
}