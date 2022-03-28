package ui

import base.*


class ChangeTableViewModel : BaseViewModel<ChangeTableInternalEvent, ChangeTableState>() {
    override fun createInitialState(): ChangeTableState {
        return ChangeTableState(
            ChangeTable.TableData(
                "", 0, ChangeTable.EditingType.Adding
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
                            ChangeTableState(ChangeTable.TableData(
                                event.newName, event.newData, ChangeTable.EditingType.Changing)
                            )
                        }
                    }
                    is MultiDimensionalListExternalEvent.OnDeleteClicked -> {

                    }
                }
            }
        }
    }

    override fun handleInternalEvent(event: ChangeTableInternalEvent) {
        TODO("Not yet implemented")
    }
}