package ui.MultiDimensionalList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MultiDimensionalListView(viewModel: MultiDimensionalListViewModel, stageLevel: Int = 0) {
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
        MultiDimensionalListView(i, stageLevel + 1)
    }
}