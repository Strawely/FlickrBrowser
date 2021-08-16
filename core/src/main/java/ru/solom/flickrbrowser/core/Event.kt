package ru.solom.flickrbrowser.core

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
