package xingkong.loopicserver.picproc.tagImg.match


import xingkong.loopicserver.picproc.tagImg.TagedFile
import java.util.*

/**
 * 这种匹配方法给根路径赋予更大的权重，
 */
class MatchDirFirst : MatchBase() {

    /**
     * 计算匹配分的函数；使用先验标签匹配法
     *
     * @param tagedFile
     * @param inputTag
     */
    override fun computeMp(tagedFile: TagedFile, inputTag: List<String>) {
        tagedFile.mp = 0.0
        /*加入随机事件，避免结果过于单调*/
        val rand = -LAMDA + Random().nextFloat() * 2f * LAMDA
        for (t in tagedFile.tags) {
            tagedFile.mp = tagedFile.mp * 2
            if (!inputTag.contains(t)) {
                tagedFile.mp = tagedFile.mp + 1.0
            }
        }
        tagedFile.mp = tagedFile.mp + rand   //使用随机数来shuffle
    }

    companion object {
        val LAMDA = 4.0f
    }
}
