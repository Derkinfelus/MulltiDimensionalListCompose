package ui

import base.BaseViewModel
import kotlin.random.Random

class MultiDimensionalListViewModel :
    BaseViewModel<MultiDimensionalListInternalEvent, MultiDimensionalListState>() {
    override fun createInitialState(): State {
        return State(
            MultiDimensionalListState.MultiDimensionalList("Undefined element", 0),
            ChangeTableState.Adding()
        )
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnAddClicked -> tableStateToAdd()
            is Event.OnChangeClicked -> onChange()
            is Event.OnDeleteClicked -> onDelete()
            is Event.OnSubmitClicked -> onSubmit()
        }
    }

    fun initState(name: String, data: Int, parent: MultiDimensionalListViewModel?) {
        val state = currentState.listState as MultiDimensionalListState.MultiDimensionalList
        state.let {
            it.name = name
            it.data = data
            it.parent = parent
        }
    }

    fun initLowerDimensions() {
        val primaryListsAmount = Random.nextInt(0, 4)
        var counter = 1

        for (i: Int in 0..primaryListsAmount) {
            val secondaryListAmount = Random.nextInt(0, 3)
            val primaryList = onAdd("$counter-th primary list", counter)
            counter++
            for (j: Int in 0..secondaryListAmount) {
                primaryList.onAdd("$counter-th secondary list", counter)
                counter++
            }
        }
    }

    private fun tableStateToAdd() {
        setState {
            copy(
                tableState = ChangeTableState.Adding()
            )
        }
    }

    private fun tableStateToChange() {
        setState {
            copy(
                tableState = ChangeTableState.Changing()
            )
        }
    }

    private fun onAdd(name: String, data: Int) : MultiDimensionalListViewModel{
        val toAdd = MultiDimensionalListViewModel().also { it.initState(name, data, this )}
        val state = currentState.listState as MultiDimensionalListState.MultiDimensionalList
        state.lowerDimension.add(toAdd)

        return toAdd
    }

    private fun onChange() {
        println("onchange invoked")
        when (currentState.tableState) {
            is ChangeTableState.Adding -> {
                val name = (currentState.tableState as ChangeTableState.Adding).name
                val data = (currentState.tableState as ChangeTableState.Adding).data

                setState {
                    copy(
                        tableState = ChangeTableState.Changing(name, data)
                    )
                }
            }
        }
    }

    private fun onDelete() {
        val state = currentState.listState as MultiDimensionalListState.MultiDimensionalList
        val parentState = state.parent?.currentState!!.listState as MultiDimensionalListState.MultiDimensionalList
        val res = parentState.lowerDimension.remove(this)
        println("deleting element ${this.currentState.listState}, $res")
    }

    private fun onSubmit() {
        when (val shit = currentState.tableState) {
            is ChangeTableState.Adding -> {
                onAdd(shit.name, shit.data)
            }
            is ChangeTableState.Changing -> {
                val tmp2 = currentState.tableState as ChangeTableState.Changing
                onChange(tmp2.name, tmp2.data)
            }
        }
    }
}