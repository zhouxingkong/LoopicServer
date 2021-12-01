package xingkong.loopicserver.module.utils


object TagUtil {
    val DIV = "-"

    fun splitTag(rawTag:String):List<String>{
        return rawTag.split(DIV.toRegex()).dropLastWhile { it.isEmpty() }.toList()
    }
}