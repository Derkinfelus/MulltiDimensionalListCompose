package ui

import base.UiEvent

sealed class MultiDimensionalListInternalEvent: UiEvent {
    object OnAddClicked: MultiDimensionalListInternalEvent()
    object OnChangeClicked: MultiDimensionalListInternalEvent()
    object OnDeleteClicked: MultiDimensionalListInternalEvent()
}

sealed class MultiDimensionalListExternalEvent: UiEvent {
    object OnAddClicked: MultiDimensionalListExternalEvent()
    data class OnChangeClicked(val newName: String, val newData: Int): MultiDimensionalListExternalEvent()
    object OnDeleteClicked: MultiDimensionalListExternalEvent()
}

sealed class ChangeTableInternalEvent: UiEvent {
    object OnSubmitClicked: ChangeTableInternalEvent()
}

sealed class ChangeTableExternalEvent: UiEvent {
    data class OnAddSubmitClicked(val name: String, val data: Int): ChangeTableExternalEvent()
    data class OnChangeSubmitClicked(val newName: String, val newData: Int): ChangeTableExternalEvent()
}

sealed class ExternalEvent: UiEvent {
    data class TableEvent(val event: ChangeTableExternalEvent): ExternalEvent()
    data class ListEvent(val event: MultiDimensionalListExternalEvent): ExternalEvent()
}