package xingkong.loopicserver.module.match


import xingkong.loopicserver.module.bean.TagedFile
import java.util.*


open class MatchBase {

    /**
     * 默认实现:使用完全随机数
     *
     * @param tagedFile
     * @param inputTag
     */
    open fun computeMp(tagedFile: TagedFile, inputTag: List<String>):Double {
        /*默认方法，使用完全随机数*/
        val rand = Random().nextDouble()
        return rand
    }

}
