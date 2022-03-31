package ui.MultiDimensionalList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import base.UiState

class MultiDimensionalList(
    name: String,
    data:  Long,
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