package com.example.myapplication.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

open class BaseViewModel : ViewModel() {

    protected val _exception = MutableSharedFlow<Exception>()
    val exception: SharedFlow<Exception>
        get() = _exception

    protected val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String>
        get() = _toastMessage

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        viewModelScope.launch(coroutineContext) {
            _exception.emit(throwable as Exception)
        }
    }

    protected val baseViewModelScope: CoroutineScope = viewModelScope + errorHandler
}