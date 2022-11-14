package com.example.myapplication.network

/**
 * Response 역할을 함.
 * API 호출 후 결과를 마음대로 가공 가능
 *
 * */
internal inline fun <T> handleResult(transform: () -> T): BaseResult<T> = try {
    BaseResult.Success(transform.invoke())
} catch (e: Exception) {
    BaseResult.Error(e)
}