package com.timplifier.ktortest.presentation.ui.state

import com.timplifier.ktortest.domain.either.NetworkError

sealed class UIState<T> {
    class Idle<T> : UIState<T>()
    class Loading<T> : UIState<T>()
    class Error<T>(val error: String) : UIState<T>()
    class Success<T>(val data: T) : UIState<T>()
}