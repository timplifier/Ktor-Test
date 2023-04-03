package com.timplifier.ktortest.domain.useCases

import com.timplifier.ktortest.domain.repositories.MessengerRepository
import org.koin.core.annotation.Factory

@Factory
class CloseWebSocketUseCase(private val messengerRepository: MessengerRepository) {
    operator fun invoke() = messengerRepository.closeWebSocket()
}