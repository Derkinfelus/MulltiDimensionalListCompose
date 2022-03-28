package base

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ui.ExternalEvent

abstract class BaseViewModel<Event : UiEvent, State : UiState> {
    var coroutineScope = CoroutineScope(Dispatchers.Main)
    private val initialState : State by lazy { createInitialState() }
    abstract fun createInitialState() : State

    val currentState: State
        get() = uiState.value

    private val _uiState : MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _internalEvent : MutableSharedFlow<Event> = MutableSharedFlow()
    val internalEvent = _internalEvent.asSharedFlow()

    private val _externalEventEmitter : MutableSharedFlow<ExternalEvent> = MutableSharedFlow()
    val externalEvent: SharedFlow<ExternalEvent> = _externalEventEmitter.asSharedFlow()

    private val subscriptions = mutableListOf<Job>()


    init {
        subscribeInternalEvents()
    }

    private fun subscribeInternalEvents() {
        coroutineScope.launch {
            internalEvent.collect {
                handleInternalEvent(it)
            }
        }
    }

    fun subscribeExternalEvent(externalEvent: SharedFlow<ExternalEvent>) {
         val event = coroutineScope.launch {
            externalEvent.collect {
                handleExternalEvent(it)
            }
        }

        subscriptions.add(event)
    }

    fun unsubscribeExternalEvents() {
        subscriptions.clear()
    }

    abstract fun handleExternalEvent(externalEvent: ExternalEvent)

    abstract fun handleInternalEvent(event : Event)

    fun setInternalEvent(event : Event) {
        val newEvent = event
        coroutineScope.launch { _internalEvent.emit(newEvent) }
    }

    protected fun setExternalEvent(event: ExternalEvent) {
        val newEvent = event
        coroutineScope.launch {
            _externalEventEmitter.emit(newEvent)
        }
    }


    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }
}