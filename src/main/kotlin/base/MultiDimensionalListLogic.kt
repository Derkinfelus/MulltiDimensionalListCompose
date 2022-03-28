package base

class MultiDimensionalList(
    var name: String,
    var data: Int,
    var parent: MultiDimensionalList? = null
)
{
    var lowerDimension: MutableList<MultiDimensionalList> = mutableListOf()


    fun add(
        name: String,
        data: Int
    ) : MultiDimensionalList {
        val toAdd = MultiDimensionalList(name, data, this)
        lowerDimension.add(toAdd)

        return toAdd
    }

    fun delete(element: MultiDimensionalList) {
        lowerDimension.remove(element)
    }

    fun change(
        newName: String,
        newData: Int
    ) {
        name = newName
        data = newData
    }
}


class MultiDimensionalListModel()
{
    var root = MultiDimensionalList("root", 0)


    fun addElement(
        parentElement: MultiDimensionalList,
        name: String,
        data: Int
    ) : MultiDimensionalList {
        return parentElement.add(name, data)
    }

    fun changeElement(
        element: MultiDimensionalList,
        newName: String,
        newData: Int,
    ) {
        element.apply {
            name = newName
            data = newData
        }
    }

    fun deleteElement(element: MultiDimensionalList) {
        element.parent?.delete(element)
    }

    fun initializeData() {
        root = MultiDimensionalList("root", 0)
        for (i: Int in 1..10) {
            val primaryListId = (i - 1) * 11 + 1
            val primaryList = addElement(root, "$primaryListId-th element", primaryListId)
            for (j: Int in 1..10) {
                val secondaryListId = primaryListId + j
                addElement(primaryList, "$secondaryListId-th element", secondaryListId)
            }
        }
    }

    fun recursivePrint(element: MultiDimensionalList?) {
        if (element == null)
            return
        else {
            val name = element.name
            val data = element.data
            println("$name, $data")
            for (i in element.lowerDimension)
                recursivePrint(i)
        }
    }
}