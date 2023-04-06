package com.timplifier.ktortest.data.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.gson.GsonWebsocketContentConverter
import io.ktor.websocket.WebSocketDeflateExtension
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import java.util.zip.Deflater

@Module
@ComponentScan("com.timplifier.ktortest.data")
class DataModule {

    @Single
    fun createHttpClient() = HttpClient(OkHttp) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(WebSockets) {
            maxFrameSize = Long.MAX_VALUE
            contentConverter = GsonWebsocketContentConverter()
            pingInterval = 20_000
            extensions {
                install(WebSocketDeflateExtension) {
                    /**
                     * Compression level to use for [java.util.zip.Deflater].
                     */
                    compressionLevel = Deflater.DEFAULT_COMPRESSION

                    /**
                     * Prevent compressing small outgoing frames.
                     */
                    compressIfBiggerThan(bytes = 4 * 1024)
                }
            }
        }
    }
}