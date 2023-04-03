package com.timplifier.ktortest.domain.di

import com.timplifier.ktortest.domain.useCases.CloseWebSocketUseCase
import com.timplifier.ktortest.domain.useCases.GetMessagesUseCase
import com.timplifier.ktortest.domain.useCases.SendMessageUseCase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

//val domainModule = module {
//
//    factory { SendMessageUseCase(get()) }
//
//    factory { GetMessagesUseCase(get()) }
//
//    factory { CloseWebSocketUseCase(get()) }
//}

@Module
@ComponentScan("com.timplifier.ktortest.domain")
class DomainModule