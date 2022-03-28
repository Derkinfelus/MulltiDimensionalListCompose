package ui

import base.*

sealed class EditingType {
    object Adding: EditingType()
    object Changing: EditingType()
}

data class ChangeTable(var name: String, var data: Int, var tableType: EditingType)

data class MultiDimensionalList(
    var name: String,
    var data: Int,
    var parent: MultiDimensionalListViewModel? = null
){ val lowerDimension = mutableListOf<MultiDimensionalListViewModel>() }

data class ChangeTableState(
    val changeTable: ChangeTable
) : UiState

data class MultiDimensionalListState(
    val multiDimensionalList: MultiDimensionalList
) : UiState