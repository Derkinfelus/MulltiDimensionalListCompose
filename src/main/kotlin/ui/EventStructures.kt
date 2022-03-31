package ui

import base.UiEvent

sealed class MultiDimensionalListInternalEvent : UiEvent {
    object OnAddClicked : MultiDimensionalListInternalEvent()
    object OnChangeClicked : MultiDimensionalListInternalEvent()
    object OnDeleteClicked : MultiDimensionalListInternalEvent()
}

sealed class MultiDimensionalListExternalEvent : UiEvent {
    object OnAddClicked : MultiDimensionalListExternalEvent()
    data class OnChangeClicked(val name: String, val data: Int) : MultiDimensionalListExternalEvent()
    object OnDeleteClicked : MultiDimensionalListExternalEvent()
    data class Unsubscribe(val toUnsubscribe: MultiDimensionalListViewModel) : MultiDimensionalListExternalEvent()
}

sealed class EditingTableInternalEvent: UiEvent {
    object OnSubmitClicked: EditingTableInternalEvent()
}

sealed class EditingTableExternalEvent : UiEvent {
    data class OnSubmitClicked(val name: String, val data: Int): EditingTableExternalEvent()
}

sealed class ExternalEvent: UiEvent {
    data class TableEvent(val event: EditingTableExternalEvent): ExternalEvent()
    data class ListEvent(val event: MultiDimensionalListExternalEvent): ExternalEvent()
}