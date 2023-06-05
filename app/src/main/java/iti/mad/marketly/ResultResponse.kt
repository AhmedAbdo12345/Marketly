package iti.mad.marketly

sealed class ResultResponse<T> {
    data class OnSuccess<T>(var data: T) : ResultResponse<T>()
    data class OnError<T>(var message: String) : ResultResponse<T>()
    data class OnLoading<T>(var isLoading: Boolean) : ResultResponse<T>()
}