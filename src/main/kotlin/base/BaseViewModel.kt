package base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : UiEvent, State : UiState> {
    var coroutineScope = CoroutineScope(Dispatchers.Main)
    private val initialState : State by lazy { createInitialState() }
    abstract fun createInitialState() : State

    val currentState: State
        get() = uiState.value

    private val _uiState : MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _event : MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()


    init {
        subscribeInternalEvents()
    }

    private fun subscribeInternalEvents() {
        coroutineScope.launch {
            event.collect {
                handleInternalEvent(it)
            }
        }
    }

    fun<T: UiEvent> subscribeExternalEvents(externalEvent: MutableSharedFlow<T>) {
        coroutineScope.launch {
            externalEvent.collect {
                handleExternalEvent(it)
            }
        }
    }

    abstract fun<T: UiEvent> handleExternalEvent(externalEvent: T)

    abstract fun handleInternalEvent(event : Event)

    fun setInternalEvent(event : Event) {
        val newEvent = event
        coroutineScope.launch { _event.emit(newEvent) }
    }


    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }
}