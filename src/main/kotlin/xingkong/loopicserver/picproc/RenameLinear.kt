package xingkong.loopicserver.picproc


import xingkong.loopicserver.picproc.multiDir.SingleDirRename
import java.util.*

class RenameLinear {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            val `in` = Scanner(System.`in`)
            val r = SingleDirRename()
            while (true) {
                r.img_index = 0
                r.file_num = 0
                println("------------------------------------")
                `in`.useDelimiter("\n")
                val m = `in`.next()
                val pre = SingleDirRename.getNameRoot(6)
                //            String pre = "fengjing";
                //            int offset = in.nextInt();
                val offset = 0
                println("Begin!")
                r.getFileListByDir(m)
                r.renameLinear(pre, offset)
                println("Success!!")
            }
        }
    }
}
