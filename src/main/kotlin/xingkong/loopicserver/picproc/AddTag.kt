package xingkong.loopicserver.picproc


import xingkong.loopicserver.picproc.tagImg.TagManager
import java.util.*

/*添加标签*/
object AddTag {
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
            tm.addTag(dir, tag)
        }
    }
}
