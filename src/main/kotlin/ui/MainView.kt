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
import kotlinx.coroutines.flow.collect
import ui.*


@Composable
@Preview
fun TableRender(viewModel: ChangeTableViewModel) {
    val state by remember { mutableStateOf(viewModel.currentState) }
    var name: String = viewModel.currentState.changeTable.name
    var data: Int = viewModel.currentState.changeTable.data
    val label = when (state.changeTable.tableType) {
        is EditingType.Adding -> "Adding"
        is EditingType.Changing -> "Changing"
    }

    Text(label)
    TextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Name") },
    )
    TextField(
        value = data.toString(),
        onValueChange = { data = it.toInt() },
        label = { Text("Data") },
    )
    Button({ viewModel.setInternalEvent(ChangeTableInternalEvent.OnSubmitClicked) }) {
        Text("Submit")
    }
}

@Composable
@Preview
fun RecursiveListRender(viewModel: MultiDimensionalListViewModel) {
    val state by remember { mutableStateOf(viewModel.currentState) }

    MaterialTheme {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(state.multiDimensionalList.name)
            Spacer(Modifier.width(10.dp))

            Button({ viewModel.setInternalEvent(MultiDimensionalListInternalEvent.OnAddClicked) }) {
                Text("Add")
            }
            Spacer(Modifier.width(10.dp))

            Button({ viewModel.setInternalEvent(MultiDimensionalListInternalEvent.OnChangeClicked) }) {
                Text("Change")
            }
            Spacer(Modifier.width(10.dp))

            Button({ viewModel.setInternalEvent(MultiDimensionalListInternalEvent.OnDeleteClicked) }) {
                Text("Delete")
            }
        }
    }

    if (state.multiDimensionalList.lowerDimension.isEmpty()) {
        return
    }
    for (i in state.multiDimensionalList.lowerDimension) {
        RecursiveListRender(i)
    }
}

@Preview
@Composable
fun app() {
    MaterialTheme {
        val listViewModel = remember {
            MultiDimensionalListViewModel().apply {
                currentState.multiDimensionalList.name = "Root"
                initLowerDimensions()
            }
        }
        val tableViewModel = remember {
            ChangeTableViewModel(listViewModel.externalEvent)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                TableRender(tableViewModel)
                RecursiveListRender(listViewModel)
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        app()
    }
}