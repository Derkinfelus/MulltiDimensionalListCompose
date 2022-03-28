package ui

import base.*


sealed class ChangeTable {
    sealed class EditingType {
        object Adding: EditingType()
        object Changing: EditingType()
    }
    data class TableData(var name: String, var data: Int, var TableType: EditingType): ChangeTable()
}

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