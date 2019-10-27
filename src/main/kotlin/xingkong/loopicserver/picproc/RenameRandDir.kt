package xingkong.loopicserver.picproc


import xingkong.loopicserver.picproc.multiDir.SubDirImages
import java.util.*

object RenameRandDir {

    @JvmStatic
    fun main(args: Array<String>) {
        val `in` = Scanner(System.`in`)
        println("------------------------------------")
        `in`.useDelimiter("\n")
        val from = `in`.next()
        val to = `in`.next()
        val sb = SubDirImages()
        sb.copyRandImage(from, to, 30)

    }
}
