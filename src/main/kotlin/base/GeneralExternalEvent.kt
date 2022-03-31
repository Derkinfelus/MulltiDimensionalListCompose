package base

import ui.EditingTableEvents.EditingTableExternalEvent
import ui.MultiDimensionalList.MultiDimensionalListExternalEvent

sealed class ExternalEvent: UiEvent {
    data class TableEvent(val event: EditingTableExternalEvent): ExternalEvent()
    data class ListEvent(val event: MultiDimensionalListExternalEvent): ExternalEvent()
}