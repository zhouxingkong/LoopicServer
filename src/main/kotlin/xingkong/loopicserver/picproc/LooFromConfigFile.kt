package xingkong.loopicserver.picproc


import xingkong.loopicserver.picproc.tagImg.ConfigFileManager
import java.util.*

object LooFromConfigFile {
    @JvmStatic
    fun main(args: Array<String>) {
        val `in` = Scanner(System.`in`)
        val cf = ConfigFileManager()
        println("---------------------------------")
        val dir = `in`.next()
        cf.LooFromConfigFile(dir)

    }
}
