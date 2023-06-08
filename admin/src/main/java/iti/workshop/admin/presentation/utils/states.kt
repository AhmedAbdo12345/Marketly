package iti.workshop.admin.presentation.utils

sealed class DataListResponseState<T> {
    class OnSuccess<T>(var data: T) : DataListResponseState<T>()
    class OnError<T>(var message: String) : DataListResponseState<T>()
    class OnLoading<T> : DataListResponseState<T>()
    class OnNothingData<T> : DataListResponseState<T>()
}


sealed class DataResponseState<T> {
    class OnSuccess<T>(var data: T) : DataResponseState<T>()
    class OnError<T>(var message: String) : DataResponseState<T>()
    class OnLoading<T> : DataResponseState<T>()
}
