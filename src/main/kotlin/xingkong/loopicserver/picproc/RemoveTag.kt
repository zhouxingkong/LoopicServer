package xingkong.loopicserver.picproc


import xingkong.loopicserver.picproc.tagImg.TagManager
import java.util.*

object RemoveTag {
    @JvmStatic
    fun main(args: Array<String>) {
        //        String[] nameSplitDot = "asjgno.ddd".split(".");
        //        System.out.println(nameSplitDot.length+":"+ nameSplitDot[0]);
        val `in` = Scanner(System.`in`)
        val tm = TagManager()
        //        in.useDelimiter("\n");
        while (true) {
            println("---------------------------------")
            val dir = `in`.next()
            val tag = `in`.next()
            tm.removeTag(dir, tag)

        }
    }
}
