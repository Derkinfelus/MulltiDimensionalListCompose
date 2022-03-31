package ui.MultiDimensionalList

import base.BaseViewModel
import base.ExternalEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import ui.*
import ui.EditingTableEvents.EditingTableExternalEvent
import kotlin.random.Random

class MultiDimensionalListViewModel(private val defaultTable: EditingTableViewModel) :
    BaseViewModel<MultiDimensionalListInternalEvent, MultiDimensionalListState>() {

    private var currentSubscription : Job = Job()

    init {
        currentSubscription.cancel()
        defaultTable.subscribeExternalEvent(this.externalEvent)
    }


    private fun switchSubscription() {
        if (currentSubscription.isActive)
            currentSubscription.cancel()
        else
            currentSubscription = coroutineScope.launch {
                defaultTable.externalEvent.collect {
                    handleExternalEvent(it)
                }
            }
    }

    override fun createInitialState(): MultiDimensionalListState {
        return MultiDimensionalListState(
            MultiDimensionalList("Undefined element", 0)
        )
    }

    override fun handleInternalEvent(event: MultiDimensionalListInternalEvent) {
        when (event) {
            is MultiDimensionalListInternalEvent.OnAddClicked -> {
                currentState.list.isChanging = false
                currentState.list.isAdding = !currentState.list.isAdding
                if (currentState.list.isAdding and !currentSubscription.isActive or !currentState.list.isAdding)
                    switchSubscription()


                setExternalEvent(
                    ExternalEvent.ListEvent(
                        MultiDimensionalListExternalEvent.OnAddClicked
                    )
                )
            }

            is MultiDimensionalListInternalEvent.OnChangeClicked -> {

                currentState.list.isAdding = false
                currentState.list.isChanging = !currentState.list.isChanging

                if (currentState.list.isChanging and !currentSubscription.isActive or !currentState.list.isChanging)
                    switchSubscription()
                if (currentState.list.isChanging){
                    setExternalEvent(
                        ExternalEvent.ListEvent(
                            MultiDimensionalListExternalEvent.OnChangeClicked(
                                currentState.list.name,
                                currentState.list.data
                            )
                        )
                    )
                }
            }

            is MultiDimensionalListInternalEvent.OnDeleteClicked -> {
                setExternalEvent(
                    ExternalEvent.ListEvent(
                        MultiDimensionalListExternalEvent.OnDeleteClicked(this)
                    )
                )
                currentState.list.parent?.currentState?.list?.lowerDimension?.remove(this)
            }
        }
    }

    override fun handleExternalEvent(externalEvent: ExternalEvent) {
        when (externalEvent) {
            is ExternalEvent.TableEvent -> {
                when (val event = externalEvent.event) {
                    is EditingTableExternalEvent.OnSubmitClicked -> {
                        if (currentState.list.isAdding) {
                            val newElem = MultiDimensionalListViewModel(defaultTable).also {
                                it.currentState.list.name = event.name
                                it.currentState.list.data = event.data
                                it.currentState.list.parent = this
                            }
                            currentState.list.lowerDimension.add(newElem)
                        }
                        else {
                            currentState.list.name = event.name
                            currentState.list.data = event.data
                        }
                    }
                }
            }
        }
    }

    fun initLowerDimensions() {
        val primaryListsAmount = Random.nextInt(0, 4)
        var counter : Long = 1

        for (i: Int in 0..primaryListsAmount) {
            val secondaryListAmount = Random.nextInt(0, 3)
            val primaryList = MultiDimensionalListViewModel(defaultTable).also {
                it.currentState.list.name = "$counter-th primary list"
                it.currentState.list.data = counter
                it.currentState.list.parent = this
            }
            currentState.list.lowerDimension.add(primaryList)
            counter++
            for (j: Int in 0..secondaryListAmount) {
                val secondaryList = MultiDimensionalListViewModel(defaultTable).apply{
                    currentState.list.name = "$counter-th secondary list"
                    currentState.list.data = counter
                    currentState.list.parent = primaryList
                }
                primaryList.currentState.list.lowerDimension.add(secondaryList)
                counter++
            }
        }
    }
}