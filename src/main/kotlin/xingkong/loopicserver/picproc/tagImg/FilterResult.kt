package xingkong.loopicserver.picproc.tagImg

class FilterResult(var filteredList: List<TagedFile>, var randomIndexs: IntArray) {
    var consumeIndex = 0

    val res: Int
        get() = filteredList.size - consumeIndex
}
