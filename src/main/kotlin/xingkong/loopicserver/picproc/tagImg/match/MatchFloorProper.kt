package xingkong.loopicserver.picproc.tagImg.match


import xingkong.loopicserver.picproc.tagImg.TagedFile
import java.util.*

/**
 * 每一层给赋予一定的概率
 */
class MatchFloorProper : MatchBase() {

    /**
     * 计算匹配分的函数；使用先验标签匹配法
     *
     * @param tagedFile
     * @param inputTag
     */
    override fun computeMp(tagedFile: TagedFile, inputTag: List<String>) {
        val floorDis = tagedFile.tags.size - inputTag.size
        val rand = Random().nextDouble() * (PROPOR_STEP * (floorDis - 1) + 1.0f)
        tagedFile.mp = rand   //使用随机数来shuffle
    }

    companion object {

        val PROPOR_STEP = 100.0f
    }
}
