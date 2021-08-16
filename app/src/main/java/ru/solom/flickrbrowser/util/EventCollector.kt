package ru.solom.flickrbrowser.util

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import ru.solom.flickrbrowser.core.Event

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
