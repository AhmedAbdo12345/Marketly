package iti.workshop.admin.presentation.utils

sealed class DataResponseState<T> {
    class OnSuccess<T>(var data: T) : DataResponseState<T>()
    class OnError<T>(var message: String) : DataResponseState<T>()
    class OnLoading<T> : DataResponseState<T>()
    class OnNothingData<T> : DataResponseState<T>()
}
