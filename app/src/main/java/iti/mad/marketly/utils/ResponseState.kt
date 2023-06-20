package iti.mad.marketly.utils

sealed class ResponseState<T> {
    data class OnSuccess<T>(var response: T) : ResponseState<T>()
    data class OnError<T>(var message: String) : ResponseState<T>()
     data class OnLoading<T>(var loading: Boolean) : ResponseState<T>()
}