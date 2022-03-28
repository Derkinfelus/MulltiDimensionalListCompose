package ui

import base.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow


class ChangeTableViewModel(defaultSub: SharedFlow<ExternalEvent>) :
    BaseViewModel<ChangeTableInternalEvent, ChangeTableState>() {
    var defaultSubscription: SharedFlow<ExternalEvent> = defaultSub

    override fun createInitialState(): ChangeTableState {
        subscribeExternalEvent(defaultSubscription)
        return ChangeTableState(
            ChangeTable(
                "", 0, EditingType.Adding
            )
        )
    }

    override fun handleExternalEvent(externalEvent: ExternalEvent) {
        when (externalEvent) {
            is ExternalEvent.ListEvent -> {
                when (val event = externalEvent.event) {
                    is MultiDimensionalListExternalEvent.OnAddClicked -> {
                        setState {
                            createInitialState()
                        }
                    }
                    is MultiDimensionalListExternalEvent.OnChangeClicked -> {
                        setState {
                            ChangeTableState(
                                currentState.changeTable.copy(
                                    name = event.newName,
                                    data = event.newData
                                )
                            )
                        }
                    }
                    is MultiDimensionalListExternalEvent.OnDeleteClicked -> {
                        unsubscribeExternalEvents()
                    }
                }
            }
            else -> {
                println("Unsupported subscribe type")
            }
        }
    }

    override fun handleInternalEvent(event: ChangeTableInternalEvent) {
        when (event) {
            is ChangeTableInternalEvent.OnSubmitClicked -> {
                when (currentState.changeTable.tableType) {
                    is EditingType.Adding -> {
                        val state = currentState.changeTable
                        setExternalEvent(
                            ExternalEvent.TableEvent(
                                ChangeTableExternalEvent.OnAddSubmitClicked(state.name, state.data)
                            )
                        )
                    }
                    is EditingType.Changing -> {
                        val state = currentState.changeTable
                        setExternalEvent(
                            ExternalEvent.TableEvent(
                                ChangeTableExternalEvent.OnChangeSubmitClicked(state.name, state.data)
                            )
                        )
                    }
                }
                setState { createInitialState() }
            }
        }
    }
}