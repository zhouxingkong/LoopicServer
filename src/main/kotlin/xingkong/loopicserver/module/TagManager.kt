package xingkong.loopicserver.module


object TagManager {


    val picDatabase
        get() = ConfigFileManager.totalList
    val excludeTags
        get() = ConfigFileManager.excludeTags

    val picFilter = PicNameFilter()

    /**
     *
     */
    fun getFileListByTag(tag:List<String>):List<String>{
        val pickTag = mutableListOf<String>().apply { addAll(tag) }
        var filteredList = picFilter.filter(picDatabase, pickTag, excludeTags).map {
            it.file.absolutePath
        }

        while (filteredList.isEmpty() && pickTag.size > 1) {
            pickTag.removeAt(pickTag.size-1)
            filteredList = picFilter.filter(picDatabase, pickTag, excludeTags).map {
                it.file.absolutePath
            }
        }

        return filteredList
    }
}