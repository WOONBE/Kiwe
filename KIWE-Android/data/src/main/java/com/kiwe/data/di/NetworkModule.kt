package com.kiwe.data.di

import com.kiwe.data.BuildConfig
import com.kiwe.domain.usecase.manager.token.GetTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.charsets.Charsets
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton

private const val TAG = "NetworkModule"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = BuildConfig.BASE_URL
    private const val BASE_IMAGE_URL = BuildConfig.BASE_IMAGE_URL
    private const val FAST_URL = BuildConfig.FAST_URL

    private const val NETWORK_TIMEOUT = 5000L

    @Provides
    @Spring
    @Singleton
    fun provideHttpsClient(getTokenUseCase: GetTokenUseCase) =
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        useAlternativeNames = true
                        ignoreUnknownKeys = true
                        encodeDefaults = false
                    },
                )
            }

            Charsets {
                register(Charsets.UTF_8)
            }

            install(HttpTimeout) {
                requestTimeoutMillis = NETWORK_TIMEOUT
                connectTimeoutMillis = NETWORK_TIMEOUT
                socketTimeoutMillis = NETWORK_TIMEOUT
            }

            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            Timber.tag(TAG).v(message)
                        }
                    }
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    Timber.tag(TAG).d("HTTPs status: ${response.status.value}")
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.ContentType, Charsets.UTF_8)
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    host = BASE_URL
                }
                runBlocking {
                    val token = getTokenUseCase()
                    if (token != null) {
                        header("Authorization", "Bearer ${token.accessToken}")
                        header("Refresh-Token", token.refreshToken)
                    }
                }
            }
        }

    @Provides
    @Fast
    @Singleton
    fun provideHttpClient(getTokenUseCase: GetTokenUseCase) =
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        useAlternativeNames = true
                        ignoreUnknownKeys = true
                        encodeDefaults = false
                    },
                )
            }

            Charsets {
                register(Charsets.UTF_8)
            }

            install(HttpTimeout) {
                requestTimeoutMillis = NETWORK_TIMEOUT
                connectTimeoutMillis = NETWORK_TIMEOUT
                socketTimeoutMillis = NETWORK_TIMEOUT
            }

            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            Timber.tag(TAG).v(message)
                        }
                    }
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    Timber.tag(TAG).d("HTTP status: ${response.status.value}")
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.ContentType, Charsets.UTF_8)
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTP
                    host = FAST_URL
                    port = 9988
                }
                runBlocking {
                    val token = getTokenUseCase()
                    if (token != null) {
                        header("Authorization", "Bearer ${token.accessToken}")
                        header("Refresh-Token", token.refreshToken)
                    }
                }
            }
        }
}
