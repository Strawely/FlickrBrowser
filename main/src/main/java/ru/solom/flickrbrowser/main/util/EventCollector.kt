package ru.solom.flickrbrowser.main.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.solom.flickrbrowser.core.Event

class EventCollector<T>(private val onEvent: (content: T) -> Unit) : FlowCollector<Event<T>?> {
    override suspend fun emit(value: Event<T>?) {
        val content = value?.getContentIfNotHandled() ?: return
        onEvent(content)
    }
}

inline fun <T> StateFlow<Event<T>?>.collectEvent(
    scope: CoroutineScope,
    crossinline onChanged: (T) -> Unit
) = scope.launch {
    collect { value ->
        val content = value?.getContentIfNotHandled() ?: return@collect
        onChanged(content)
    }
}
