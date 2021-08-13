package ru.solom.flickrbrowser.util

sealed class RequestState<T> {
    class Success<T>(val data: T) : RequestState<T>()
    class Failure<T>(val throwable: Throwable) : RequestState<T>()
    class Loading<T>() : RequestState<T>()
}