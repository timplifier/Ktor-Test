package com.timplifier.ktortest.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.timplifier.ktortest.domain.either.Either
import com.timplifier.ktortest.presentation.ui.state.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected fun <T> mutableUiStateFlow() = MutableStateFlow<UIState<T>>(UIState.Idle())

    /**
     * Collect network request
     *
     * @return [UIState] depending request result
     */
    protected fun <T> Flow<Either<String, T>>.gatherRequest(
        state: MutableStateFlow<UIState<T>>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.value = UIState.Loading()
            this@gatherRequest.collect {
                when (it) {
                    is Either.Left -> state.value = UIState.Error(it.value)
                    is Either.Right -> state.value = UIState.Success(it.value)
                }
            }
        }
    }

    /**
     * Collect network request with mapping from domain to ui
     *
     * @return [UIState] depending request result
     */
    protected fun <T, S> Flow<Either<String, T>>.gatherRequest(
        state: MutableStateFlow<UIState<S>>,
        mappedData: (T) -> S
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.value = UIState.Loading()
            this@gatherRequest.collect {
                when (it) {
                    is Either.Left -> state.value = UIState.Error(it.value)
                    is Either.Right -> state.value = UIState.Success(mappedData(it.value))
                }
            }
        }
    }

    protected fun <T : Any, S : Any> Flow<PagingData<T>>.gatherPagingRequest(
        mappedData: (data: T) -> S,
    ) = map {
        it.map { data -> mappedData(data) }
    }.cachedIn(viewModelScope)
}