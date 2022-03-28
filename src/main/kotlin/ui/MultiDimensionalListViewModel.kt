package ui

import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.mutableStateOf
import base.BaseViewModel
import kotlin.random.Random

class MultiDimensionalListViewModel :
    BaseViewModel<MultiDimensionalListInternalEvent, MultiDimensionalListState>() {
    override fun createInitialState(): MultiDimensionalListState {
        return MultiDimensionalListState(
            MultiDimensionalList(mutableStateOf("Undefined element"), mutableStateOf(0))
        )
    }

    override fun createInitialExternalEvent(): ExternalEvent {
        return ExternalEvent.ListEvent(
            MultiDimensionalListExternalEvent.OnAddClicked
        )
    }

    override fun handleInternalEvent(event: MultiDimensionalListInternalEvent) {
        when (event) {
            is MultiDimensionalListInternalEvent.OnAddClicked -> {
                setExternalEvent(
                    ExternalEvent.ListEvent(
                        MultiDimensionalListExternalEvent.OnAddClicked
                    )
                )
            }
            is MultiDimensionalListInternalEvent.OnChangeClicked -> {
                val state = currentState.multiDimensionalList
                setExternalEvent(
                    ExternalEvent.ListEvent(
                        MultiDimensionalListExternalEvent.OnChangeClicked(state.name.value, state.data.value)
                    )
                )
            }
            is MultiDimensionalListInternalEvent.OnDeleteClicked -> {
                val state = currentState.multiDimensionalList
                setExternalEvent(
                    ExternalEvent.ListEvent(
                        MultiDimensionalListExternalEvent.OnDeleteClicked
                    )
                )

                state.parent?.currentState?.multiDimensionalList?.lowerDimension?.remove(this) ?: run {
                    println("Impossible to delete root element")
                }
            }
        }
    }

    fun initLowerDimensions() {
        val primaryListsAmount = Random.nextInt(0, 4)
        var counter = 1

        for (i: Int in 0..primaryListsAmount) {
            val secondaryListAmount = Random.nextInt(0, 3)
            val primaryList = MultiDimensionalListViewModel().apply {
                currentState.multiDimensionalList.name = mutableStateOf("$counter-th primaryList")
                currentState.multiDimensionalList.data = mutableStateOf(counter)
                currentState.multiDimensionalList.parent = this
            }
            currentState.multiDimensionalList.lowerDimension.add(primaryList)
            counter++
            for (j: Int in 0..secondaryListAmount) {
                val secondaryList = MultiDimensionalListViewModel().apply{
                    currentState.multiDimensionalList.name = mutableStateOf("$counter-th primaryList")
                    currentState.multiDimensionalList.data = mutableStateOf(counter)
                    currentState.multiDimensionalList.parent = primaryList
                }
                primaryList.currentState.multiDimensionalList.lowerDimension.add(secondaryList)
                counter++
            }
        }
    }

    override fun handleExternalEvent(externalEvent: ExternalEvent) {
        when (externalEvent) {
            is ExternalEvent.TableEvent -> {
                when (val event = externalEvent.event) {
                    is ChangeTableExternalEvent.OnAddSubmitClicked -> {
                        val newElem = MultiDimensionalListViewModel().apply {
                            currentState.multiDimensionalList.name.value = event.name
                            currentState.multiDimensionalList.data.value = event.data
                            currentState.multiDimensionalList.parent = this
                        }
                        currentState.multiDimensionalList.lowerDimension.add(newElem)
                    }
                    is ChangeTableExternalEvent.OnChangeSubmitClicked -> {
                        currentState.multiDimensionalList.name.value = event.newName
                        currentState.multiDimensionalList.data.value = event.newData
                    }
                }
            }
        }
    }
}