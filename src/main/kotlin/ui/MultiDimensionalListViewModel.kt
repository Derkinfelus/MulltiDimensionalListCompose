package ui

import androidx.compose.runtime.currentRecomposeScope
import base.BaseViewModel
import kotlin.random.Random

class MultiDimensionalListViewModel :
    BaseViewModel<MultiDimensionalListInternalEvent, MultiDimensionalListState>() {
    override fun createInitialState(): MultiDimensionalListState {
        setExternalEvent(
            ExternalEvent.ListEvent(
                MultiDimensionalListExternalEvent.OnAddClicked
            )
        )

        return MultiDimensionalListState(
            MultiDimensionalList("Undefined element", 0)
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
                        MultiDimensionalListExternalEvent.OnChangeClicked(state.name, state.data)
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
                currentState.multiDimensionalList.name = "$counter-th primaryList"
                currentState.multiDimensionalList.data = counter
                currentState.multiDimensionalList.parent = this
            }
            counter++
            for (j: Int in 0..secondaryListAmount) {
                val secondaryList = MultiDimensionalListViewModel().apply{
                    currentState.multiDimensionalList.name = "$counter-th primaryList"
                    currentState.multiDimensionalList.data = counter
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
                            currentState.multiDimensionalList.name = event.name
                            currentState.multiDimensionalList.data = event.data
                            currentState.multiDimensionalList.parent = this
                        }
                        currentState.multiDimensionalList.lowerDimension.add(newElem)
                    }
                    is ChangeTableExternalEvent.OnChangeSubmitClicked -> {
                        currentState.multiDimensionalList.name = event.newName
                        currentState.multiDimensionalList.data = event.newData
                    }
                }
            }
        }
    }
}