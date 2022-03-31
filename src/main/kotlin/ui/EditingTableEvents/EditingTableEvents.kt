package ui.EditingTableEvents

import base.UiEvent

sealed class EditingTableInternalEvent: UiEvent {
    object OnSubmitClicked: EditingTableInternalEvent()
}

sealed class EditingTableExternalEvent : UiEvent {
    data class OnSubmitClicked(val name: String, val data: Long): EditingTableExternalEvent()
}
