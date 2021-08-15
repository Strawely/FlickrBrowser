package ru.solom.flickrbrowser.util

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

/**
 * Used as a wrapper for data that is exposed via a StateFlow that represents an event.
 */
class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun consume(consumer: (T) -> Unit) {
        if (!hasBeenHandled) {
            hasBeenHandled = true
            consumer.invoke(content)
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

class EventCollector<T>(private val onEvent: (content: T) -> Unit) : FlowCollector<Event<T>?> {
    override suspend fun emit(value: Event<T>?) {
        val content = value?.getContentIfNotHandled() ?: return
        onEvent(content)
    }
}

inline fun <T> StateFlow<Event<T>?>.collectEvent(
    scope: LifecycleCoroutineScope,
    crossinline onChanged: (T) -> Unit
) = scope.launchWhenResumed {
    collect { value ->
        val content = value?.getContentIfNotHandled() ?: return@collect
        onChanged(content)
    }
}
