package com.example.test_ylino.core.ui

sealed interface UiEvent {
    data class Navigate(val route: String) : UiEvent
    data class ShowSnackbar(val message: String) : UiEvent
}
