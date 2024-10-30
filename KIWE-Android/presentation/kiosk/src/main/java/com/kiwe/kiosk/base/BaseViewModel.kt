package com.kiwe.kiosk.base

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import kotlin.coroutines.CoroutineContext

@Immutable
interface BaseState

interface BaseSideEffect

abstract class BaseViewModel<S : BaseState, SE : BaseSideEffect>(
    initialState: S,
) : ViewModel(),
    ContainerHost<S, SE> {
    override val container: Container<S, SE> =
        container(
            initialState = initialState,
            buildSettings = {
                this.exceptionHandler =
                    CoroutineExceptionHandler { coroutineContext, throwable ->
                        handleExceptionIntent(coroutineContext, throwable)
                    }
            },
        )

    abstract fun handleExceptionIntent(
        coroutineContext: CoroutineContext,
        throwable: Throwable,
    )
}
