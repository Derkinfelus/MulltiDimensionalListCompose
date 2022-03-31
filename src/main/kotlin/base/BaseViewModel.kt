package base

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

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

    private val _externalEvent: MutableSharedFlow<ExternalEvent> = MutableSharedFlow()
    val externalEvent : SharedFlow<ExternalEvent> = _externalEvent.asSharedFlow()


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

    fun subscribeExternalEvent(externalEvent: SharedFlow<ExternalEvent>): Job {
         val res = coroutineScope.launch {
            externalEvent.collect {
                handleExternalEvent(it)
            }
        }

        return res
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
            _externalEvent.emit(newEvent)
        }
    }
}