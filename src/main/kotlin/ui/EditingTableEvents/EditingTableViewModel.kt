package ui

import base.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import ui.EditingTableEvents.EditingTable
import ui.EditingTableEvents.EditingTableExternalEvent
import ui.EditingTableEvents.EditingTableInternalEvent
import ui.EditingTableEvents.EditingTableState
import ui.MultiDimensionalList.MultiDimensionalListExternalEvent
import ui.MultiDimensionalList.MultiDimensionalListViewModel


class EditingTableViewModel :
    BaseViewModel<EditingTableInternalEvent, EditingTableState>() {
    private val subscriptions = mutableMapOf<MultiDimensionalListViewModel, Job>()

    override fun createInitialState(): EditingTableState {
        return EditingTableState(
            EditingTable(
                "", 0
            )
        )
    }

    override fun handleInternalEvent(event: EditingTableInternalEvent) {
        when (event) {
            is EditingTableInternalEvent.OnSubmitClicked -> {
                setExternalEvent(
                    ExternalEvent.TableEvent(
                        EditingTableExternalEvent.OnSubmitClicked(
                            currentState.table.name,
                            currentState.table.data
                        )
                    )
                )
            }
        }
    }

    override fun handleExternalEvent(externalEvent: ExternalEvent) {
        when (externalEvent) {
            is ExternalEvent.ListEvent -> {
                when (val event = externalEvent.event) {
                    is MultiDimensionalListExternalEvent.OnChangeClicked -> {
                        currentState.table.name = event.name
                        currentState.table.data = event.data
                    }

                    is MultiDimensionalListExternalEvent.OnDeleteClicked -> {
                        subscriptions[event.toUnsubscribe]?.cancel()
                        subscriptions.remove(event.toUnsubscribe)
                    }
                }
            }
        }
    }
}