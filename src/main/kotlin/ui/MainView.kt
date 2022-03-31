import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.flow.collect
import ui.*


@Composable
@Preview
fun TableRender(viewModel: EditingTableViewModel) {
    val state = viewModel.currentState.table

    TextField(
        value = state.name,
        onValueChange = { state.name = it },
        label = { Text("Name") },
    )
    TextField(
        value = state.data.toString(),
        onValueChange = { state.data = it.toInt() },
        label = { Text("Data") },
    )
    Button({ viewModel.setInternalEvent(EditingTableInternalEvent.OnSubmitClicked) }) {
        Text("Submit")
    }
}

@Composable
@Preview
fun RecursiveListRender(viewModel: MultiDimensionalListViewModel, stageLevel: Int = 0) {
    val state = viewModel.currentState.list
    val addButtonColor = if (state.isAdding) Color.Yellow else Color.Blue
    val changeButtonColor = if (state.isChanging) Color.Yellow else Color.Blue

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (stageLevel != 0) {
            Spacer(Modifier.width((stageLevel * 40).dp))
            Text("*")
            Spacer(Modifier.width(10.dp))
        }

        Text(state.name)
        Spacer(Modifier.width(10.dp))

        Text(state.data.toString())
        Spacer(Modifier.width(10.dp))

        MaterialTheme(colors = lightColors(primary = addButtonColor)) {
            Button({ viewModel.setInternalEvent(MultiDimensionalListInternalEvent.OnAddClicked) }) {
                Text("Add")
            }
            Spacer(Modifier.width(10.dp))
        }

        MaterialTheme(colors = lightColors(primary = changeButtonColor)) {
            Button({ viewModel.setInternalEvent(MultiDimensionalListInternalEvent.OnChangeClicked) }, ) {
                Text("Change")
            }
            Spacer(Modifier.width(10.dp))
        }

        Button(
            { viewModel.setInternalEvent(MultiDimensionalListInternalEvent.OnDeleteClicked) },
        ) {
            Text("Delete")
        }
    }

    for (i in state.lowerDimension) {
        RecursiveListRender(i, stageLevel + 1)
    }
}

@Preview
@Composable
fun app() {
    val viewColors = lightColors(
        primary = Color.Blue
    )
    val tableViewModel = EditingTableViewModel()
    val listViewModel = MultiDimensionalListViewModel(tableViewModel).apply {
        currentState.list.name = "Root"
        initLowerDimensions(tableViewModel)
    }

    listViewModel.setInternalEvent(MultiDimensionalListInternalEvent.OnAddClicked)

    MaterialTheme(colors = viewColors) {
        LazyColumn(
            horizontalAlignment = Alignment.Start
        ) {
            item {
                TableRender(tableViewModel)
            }
            item {
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