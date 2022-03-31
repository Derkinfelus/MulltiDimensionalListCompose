package ui.MultiDimensionalList

import base.UiEvent

sealed class MultiDimensionalListInternalEvent : UiEvent {
    object OnAddClicked : MultiDimensionalListInternalEvent()
    object OnChangeClicked : MultiDimensionalListInternalEvent()
    object OnDeleteClicked : MultiDimensionalListInternalEvent()
}

sealed class MultiDimensionalListExternalEvent : UiEvent {
    object OnAddClicked : MultiDimensionalListExternalEvent()
    data class OnChangeClicked(val name: String, val data: Long) : MultiDimensionalListExternalEvent()
    data class OnDeleteClicked(val toUnsubscribe: MultiDimensionalListViewModel) : MultiDimensionalListExternalEvent()
}