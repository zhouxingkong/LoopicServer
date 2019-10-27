package xingkong.loopicserver.picproc


import xingkong.loopicserver.picproc.tagImg.TagManager
import java.util.*

object AddTagByDir {
    @JvmStatic
    fun main(args: Array<String>) {
        val `in` = Scanner(System.`in`)
        val tm = TagManager()
        println("---------------------------------")
        val dir = `in`.next()
        tm.addTagByDir(dir)

    }
}
