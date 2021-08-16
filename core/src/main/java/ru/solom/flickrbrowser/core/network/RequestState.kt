package ru.solom.flickrbrowser.core.network

sealed class RequestState<T> {
    class Success<T>(val data: T) : RequestState<T>()
    class Failure<T>(val throwable: Throwable) : RequestState<T>()
    class Loading<T> : RequestState<T>()
}
