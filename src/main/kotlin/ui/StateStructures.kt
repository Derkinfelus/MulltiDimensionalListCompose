package ui

import androidx.compose.runtime.*
import base.*

class EditingTable(name: String, data: Int) {
    var name by mutableStateOf(name)
    var data by mutableStateOf(data)
}

data class EditingTableState(
    val table: EditingTable
) : UiState


class MultiDimensionalList(
    name: String,
    data:  Int,
    var parent: MultiDimensionalListViewModel? = null
){
    var name by mutableStateOf(name)
    var data by mutableStateOf(data)
    var isAdding by mutableStateOf(false)
    var isChanging by mutableStateOf(false)
    val lowerDimension = mutableStateListOf<MultiDimensionalListViewModel>()
}

data class MultiDimensionalListState(
    val list: MultiDimensionalList,
) : UiState