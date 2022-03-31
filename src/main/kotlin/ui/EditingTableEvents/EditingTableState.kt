package ui.EditingTableEvents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import base.UiState

class EditingTable(name: String, data: Long) {
    var name by mutableStateOf(name)
    var data by mutableStateOf(data)
}

data class EditingTableState(
    val table: EditingTable
) : UiState
