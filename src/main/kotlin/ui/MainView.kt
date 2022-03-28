import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.*


@Composable
@Preview
fun TableRender(viewModel: MultiDimensionalListViewModel) {
    val onSubmit: () -> Unit =  { viewModel.setEvent(Event.OnSubmitClicked) }
    val label: String
    val name: String
    val data: Int
    val state by remember { mutableStateOf(viewModel.uiState)}

    when (val shit = state.value.tableState) {
        is ChangeTableState.Changing -> {
            label = "Changing"
            name = shit.name
            data = shit.data
        }
        is ChangeTableState.Adding -> {
            label = "Adding"
            name = shit.name
            data = shit.data
        }
    }

    var curName by remember { mutableStateOf(name) }
    var curData by remember { mutableStateOf(data) }

    Text(label)
    TextField(
        value = curName,
        onValueChange = { curName = it },
        label = { Text("Name") },
    )
    TextField(
        value = curData.toString(),
        onValueChange = { curData = it.toInt() },
        label = { Text("Data") },
    )
    Button(onSubmit) {
        Text("Submit")
    }
}

@Composable
@Preview
fun RecursiveListRender(viewModel: MultiDimensionalListViewModel) {
    val onAdd: () -> Unit = { viewModel.setEvent(Event.OnAddClicked) }
    val onChange: () -> Unit = { viewModel.setEvent(Event.OnChangeClicked) }
    val onDelete: () -> Unit = { viewModel.setEvent(Event.OnDeleteClicked) }
    val state by remember { mutableStateOf(viewModel.currentState) }

    when (val shit = state.listState) {
        is MultiDimensionalListState.MultiDimensionalList -> {
            MaterialTheme {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(shit.name)
                    Spacer(Modifier.width(10.dp))

                    Button(onAdd) {
                        Text("Add")
                    }

                    Spacer(Modifier.width(10.dp))
                    Button(onChange) {
                        Text("Change")
                    }

                    Spacer(Modifier.width(10.dp))
                    Button(onDelete) {
                        Text("Delete")
                    }
                }
            }

            if (shit.lowerDimension.isEmpty()) {
                return
            }
            for (i in shit.lowerDimension) {
                RecursiveListRender(i)
            }
        }
    }
}

@Preview
@Composable
fun app() {
    MaterialTheme {
        val viewModel = remember {
            MultiDimensionalListViewModel().apply {
                initState("Root", 0, null)
                initLowerDimensions()
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                TableRender(viewModel)
                RecursiveListRender(viewModel)
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        app()
    }
}