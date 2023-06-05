package iti.mad.marketly

sealed class ResultResponse<T> {
    data class OnSuccess<T>(var response: T) : ResultResponse<T>()
    data class OnError<T>(var message: String) : ResultResponse<T>()
     class OnLoading<T>() : ResultResponse<T>()
}