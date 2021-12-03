package xingkong.loopicserver.module.bean

import java.io.File
import java.util.*

class TagedFile(var file: File,val tags:List<String>) {
//    var mp = 100.0     //匹配分，越小说明和目标标签越近
//    var tags = mutableListOf<String>()
    val name: String
        get() = file.name
    val absolutePath: String
        get() = file.absolutePath

//    init {
//        tags.addAll(inputTag)
//    }
}
