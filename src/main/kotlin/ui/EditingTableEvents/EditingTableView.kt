package ui.EditingTableEvents

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import ui.EditingTableViewModel


@Composable
fun EditingTableView(viewModel: EditingTableViewModel) {
    val state = viewModel.currentState.table


    TextField(
        value = state.name,
        onValueChange = { state.name = it },
        label = { Text("Name") }
    )
    TextField(
        value = state.data.toString(),
        onValueChange = { state.data = it.toLong() },
        label = { Text("Data") }
    )
    Button({ viewModel.setInternalEvent(EditingTableInternalEvent.OnSubmitClicked) }) {
        Text("Submit")
    }
}