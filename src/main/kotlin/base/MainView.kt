import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.*
import ui.EditingTableEvents.EditingTableView
import ui.MultiDimensionalList.MultiDimensionalListInternalEvent
import ui.MultiDimensionalList.MultiDimensionalListView
import ui.MultiDimensionalList.MultiDimensionalListViewModel


@Preview
@Composable
fun app() {
    val viewColors = lightColors(
        primary = Color.Blue
    )
    val tableViewModel = EditingTableViewModel()
    val listViewModel = MultiDimensionalListViewModel(tableViewModel).apply {
        currentState.list.name = "Root"
        setInternalEvent(MultiDimensionalListInternalEvent.OnAddClicked)
        initLowerDimensions()
    }

    MaterialTheme(colors = viewColors) {
        LazyColumn(
            horizontalAlignment = Alignment.Start
        ) {
            item {
                EditingTableView(tableViewModel)
            }
            item {
                MultiDimensionalListView(listViewModel)
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        app()
    }
}