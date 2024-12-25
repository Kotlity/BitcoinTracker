package com.kotlity.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 *  Class that helps to configure HttpClient
 */
class KtorClient private constructor(val client: HttpClient) {

    class KtorClientBuilder {
        private var engine: HttpClientEngine? = null
        private var logLevel: LogLevel = LogLevel.NONE
        private var logger: Logger = Logger.DEFAULT
        private var prettyPrint: Boolean = false
        private var ignoreUnknownKeys: Boolean = false
        private var contentType: ContentType = ContentType.Any

        fun engine(engine: HttpClientEngine): KtorClientBuilder {
            this.engine = engine
            return this
        }

        fun logging(logLevel: LogLevel, logger: Logger): KtorClientBuilder {
            this.logLevel = logLevel
            this.logger = logger
            return this
        }

        fun contentNegotiation(prettyPrint: Boolean, ignoreUnknownKeys: Boolean): KtorClientBuilder {
            this.prettyPrint = prettyPrint
            this.ignoreUnknownKeys = ignoreUnknownKeys
            return this
        }

        fun defaultContentType(contentType: ContentType): KtorClientBuilder {
            this.contentType = contentType
            return this
        }

        fun build(): KtorClient {
            val engine = this.engine ?: throw IllegalArgumentException("HttpClientEngine is required")
            val client = HttpClient(engine) {
                install(Logging) {
                    level = logLevel
                    logger = this@KtorClientBuilder.logger
                }
                install(ContentNegotiation) {
                    json(
                        json = Json {
                            prettyPrint = this@KtorClientBuilder.prettyPrint
                            ignoreUnknownKeys = this@KtorClientBuilder.ignoreUnknownKeys
                        }
                    )
                }
                defaultRequest { // set default request parameters
                    contentType(this@KtorClientBuilder.contentType)
                }
            }
            return KtorClient(client)
        }
    }
}