package com.timplifier.ktortest.presentation.ui.activity

import androidx.lifecycle.viewModelScope
import com.timplifier.ktortest.domain.models.Message
import com.timplifier.ktortest.domain.useCases.CloseWebSocketUseCase
import com.timplifier.ktortest.domain.useCases.GetMessagesUseCase
import com.timplifier.ktortest.domain.useCases.SendMessageUseCase
import com.timplifier.ktortest.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val closeWebSocketUseCase: CloseWebSocketUseCase
) : BaseViewModel() {

    private val _sendMessageState = mutableUiStateFlow<Unit>()
    val sendMessageState = _sendMessageState.asStateFlow()

    val messageState = getMessagesUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),
        Message("")
    )

    fun sendMessage(message: String) = sendMessageUseCase(message).gatherRequest(_sendMessageState)

//    fun getMessages() {
//        viewModelScope.launch {
//            getMessagesUseCase().collectLatest {
//                _messageState.value = _messageState.value.toMutableList().also { list ->
//                    list.add(it)
//                }
//            }
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        closeWebSocketUseCase()
    }
}