package com.timplifier.ktortest.data.repositories

import com.timplifier.ktortest.data.base.makeNetworkRequestNoNetworkError
import com.timplifier.ktortest.domain.either.Either
import com.timplifier.ktortest.domain.models.Message
import com.timplifier.ktortest.domain.repositories.MessengerRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class MessengerRepositoryImpl(private val httpClient: HttpClient) : MessengerRepository {
    private val webSocket by lazy {
        runBlocking {
            httpClient.webSocketSession {
                url("wss://socketsbay.com/wss/v2/1/demo/")
            }
        }
    }

    override fun sendMessage(message: String) = makeNetworkRequestNoNetworkError {
        webSocket.outgoing.send(Frame.Text(Json.encodeToString(Message(message)))).also {
            getMessages()
        }
    }

    override fun getMessages() =
        flow {
            val incomingMessages = webSocket.incoming.consumeAsFlow().filterIsInstance<Frame.Text>()
                .mapNotNull { Json.decodeFromString<Message>(it.readText()) }
            emitAll(incomingMessages)
        }


    override fun closeWebSocket(): Flow<Either<String, Unit>> = makeNetworkRequestNoNetworkError {
        webSocket.close()
    }
}