package com.timplifier.ktortest.domain.repositories

import com.timplifier.ktortest.domain.either.Either
import com.timplifier.ktortest.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessengerRepository {
    fun sendMessage(message: String): Flow<Either<String, Unit>>
    fun getMessages(): Flow<Message>
    fun closeWebSocket(): Flow<Either<String, Unit>>
}