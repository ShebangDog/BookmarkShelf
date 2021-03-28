package dog.shebang.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class LifecycleStateFlow private constructor(
    private val mutableStateFlow: MutableStateFlow<Lifecycle.State>
) : LifecycleEventObserver, StateFlow<Lifecycle.State> by mutableStateFlow {

    constructor() : this(MutableStateFlow(Lifecycle.State.INITIALIZED))

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        mutableStateFlow.value = source.lifecycle.currentState
    }

    suspend fun waitUntilAtLeast(state: Lifecycle.State) {
        if (value.isAtLeast(state)) return
        first { it.isAtLeast(state) }
    }
}

fun <T> Flow<T>.bufferUntilAtLeast(
    lifecycleStateFlow: LifecycleStateFlow,
    state: Lifecycle.State,
    capacity: Int = Channel.BUFFERED
): Flow<T> {

    return buffer(capacity)
        .onEach { lifecycleStateFlow.waitUntilAtLeast(state) }
}

fun <T> Flow<T>.bufferUntilStarted(
    lifecycleStateFlow: LifecycleStateFlow,
    capacity: Int = Channel.BUFFERED
): Flow<T> {

    return bufferUntilAtLeast(
        lifecycleStateFlow,
        Lifecycle.State.STARTED,
        capacity
    )
}
