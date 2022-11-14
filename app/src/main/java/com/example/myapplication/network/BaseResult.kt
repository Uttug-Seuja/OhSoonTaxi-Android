package com.example.myapplication.network

sealed class BaseResult<out R> {

    data class Success<out T>(val data: T) : BaseResult<T>()
    data class Error(val exception: Exception) : BaseResult<Nothing>()
    object Loading : BaseResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
            else -> ({}).toString()
        }
    }
}

inline fun <T> BaseResult<T>.onSuccess(action: (T) -> Unit): BaseResult<T> {
    if (this is BaseResult.Success) action(data)
    return this
}

inline fun <T> BaseResult<T>.onError(action: (Exception) -> Unit): BaseResult<T> {
    if (this is BaseResult.Error) action(exception)
    return this
}