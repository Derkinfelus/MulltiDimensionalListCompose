package ui

import base.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow


class EditingTableViewModel :
    BaseViewModel<EditingTableInternalEvent, EditingTableState>() {
    private val subscriptions = mutableMapOf<MultiDimensionalListViewModel, Job>()

    fun subscribeExternalEvent(externalEvent: SharedFlow<ExternalEvent>, viewModel: MultiDimensionalListViewModel) {
        val job = super.subscribeExternalEvent(externalEvent)
        subscriptions[viewModel] = job
    }

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
        println("External table")
        when (externalEvent) {
            is ExternalEvent.ListEvent -> {
                when (val event = externalEvent.event) {
                    is MultiDimensionalListExternalEvent.OnChangeClicked -> {
                        println(event.name)
                        currentState.table.name = event.name
                        currentState.table.data = event.data
                    }
                }
            }
        }
    }
}