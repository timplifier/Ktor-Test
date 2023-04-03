package com.timplifier.ktortest.presentation.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.viewbinding.ViewBinding
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import com.timplifier.ktortest.domain.either.NetworkError
import com.timplifier.ktortest.presentation.ui.state.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<Binding : ViewBinding, ViewModel : BaseViewModel>(
    @LayoutRes layoutId: Int
) :
    Fragment(layoutId) {
    protected abstract val binding: Binding
    protected abstract val viewModel: ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        assembleViews()
        constructListeners()
        establishRequest()
        launchObservers()
    }

    protected open fun initialize() {}

    protected open fun assembleViews() {}

    protected open fun constructListeners() {}

    protected open fun establishRequest() {}

    protected open fun launchObservers() {}

    protected fun <T> StateFlow<UIState<T>>.spectateUiState(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,

        gatherLoading: ((UIState<T>) -> Unit)? = null,
        errorInvoked: ((error: String) -> Unit),
        ifSuccess: ((data: T) -> Unit)
    ) {
        safeFlowGather(lifecycleState) {
            collect {
                gatherLoading?.invoke(it)
                when (it) {
                    is UIState.Idle -> {}
                    is UIState.Loading ->
                        gatherLoading?.invoke(it)

                    is UIState.Error -> errorInvoked.invoke(it.error)
                    is UIState.Success -> ifSuccess.invoke(it.data)
                }
            }
        }
    }

    protected fun safeFlowGather(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        gather: suspend () -> Unit,
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
                gather()
            }
        }
    }

    protected fun <T> UIState<T>.assembleViewVisibility(
        group: ConstraintLayout,
        loader: CircularProgressIndicator,
        navigationSucceed: Boolean = false,
    ) {
        fun displayLoader(isDisplayed: Boolean) {
            group.isVisible = !isDisplayed
            loader.isVisible = isDisplayed
        }
        when (this) {
            is UIState.Idle -> {

            }

            is UIState.Loading -> {
                displayLoader(true)
            }

            is UIState.Error -> {
                displayLoader(false)
            }

            is UIState.Success -> {
                if (navigationSucceed) {
                    displayLoader(true)
                } else {
                    displayLoader(false)
                }
            }
        }
    }

    protected fun <T : Any> Flow<PagingData<T>>.spectatePaging(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        success: suspend (data: PagingData<T>) -> Unit,
    ) {
        safeFlowGather(lifecycleState) {
            collectLatest {
                success(it)
            }
        }
    }

    protected fun NetworkError.displayApiErrorsInTextInputLayouts(vararg inputs: TextInputLayout) {
        if (this is NetworkError.Api) {
            for (input in inputs) {
                error[input.tag].also { error ->
                    if (error == null) {
                        input.isErrorEnabled = false
                    } else {
                        input.error = error.joinToString()
                        this.error.remove(input.tag)
                    }
                }
            }
        }
    }
}