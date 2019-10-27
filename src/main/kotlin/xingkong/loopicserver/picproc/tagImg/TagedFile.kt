package xingkong.loopicserver.picproc.tagImg

import java.io.File
import java.util.*

class TagedFile(var file: File) {
    var mp = 1000.0     //匹配分，越小说明和目标标签越近
    var tags: MutableList<String>
    val name: String
        get() = file.name

    val absolutePath: String
        get() = file.absolutePath

    init {
        tags = ArrayList()
        generateTags()
    }

    fun generateTags() {
        val name = file.name
        if (name == null || name.length < 2) return
        /**/
        val nameSplitDot = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (nameSplitDot.size < 1) return
        val suffix = nameSplitDot[nameSplitDot.size - 1]    //文件後綴

        val nameSplitLine = nameSplitDot[0].split(DIV.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (nameSplitLine.size < 1) return

        for (s in nameSplitLine) {
            tags.add(s)
        }
    }

    companion object {
        val DIV = "-"
    }


}
