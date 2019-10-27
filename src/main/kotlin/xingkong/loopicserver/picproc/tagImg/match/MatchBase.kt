package xingkong.loopicserver.picproc.tagImg.match


import xingkong.loopicserver.picproc.tagImg.TagedFile
import java.util.*


open class MatchBase {

    /**
     * 默认实现:使用完全随机数
     *
     * @param tagedFile
     * @param inputTag
     */
    open fun computeMp(tagedFile: TagedFile, inputTag: List<String>) {
        /*默认方法，使用完全随机数*/
        val rand = Random().nextDouble()
        tagedFile.mp = rand   //使用随机数来shuffle
    }

}
