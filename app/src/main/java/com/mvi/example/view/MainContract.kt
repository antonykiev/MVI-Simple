package com.mvi.example.view

import com.mvi.example.core.UiEffect
import com.mvi.example.core.UiEvent
import com.mvi.example.core.UiState


class MainContract {

    sealed class Event : UiEvent {
        object OnRandomNumberClicked : Event()
        object OnShowToastClicked : Event()
    }

    data class State(
        val randomNumberState: RandomNumberState
    ) : UiState

    sealed class RandomNumberState {
        object Idle : RandomNumberState()
        object Loading : RandomNumberState()
        data class Success(val number : Int) : RandomNumberState()
    }

    sealed class Effect : UiEffect {

        object ShowToast : Effect()

    }

}