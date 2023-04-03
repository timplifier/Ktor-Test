package com.timplifier.ktortest.presentation.base

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.PagingData
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.timplifier.ktortest.presentation.ui.state.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFlowFragment(
    @LayoutRes layoutId: Int,
    @IdRes private val navHostFragmentId: Int
) : Fragment(layoutId) {

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(navHostFragmentId) as NavHostFragment
        val navController = navHostFragment.navController
        setupNavigation(navController)
    }

    protected open fun setupNavigation(navController: NavController) {}

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
            is UIState.Idle -> {}

            is UIState.Loading ->
                displayLoader(true)

            is UIState.Error ->
                displayLoader(false)

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
}