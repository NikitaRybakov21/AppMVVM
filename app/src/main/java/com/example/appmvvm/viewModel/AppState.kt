package com.example.appmvvm.viewModel

sealed class AppState {
    data class Success(val dataString : String) : AppState()
    data class Error(val error : Throwable) : AppState()
}