package xingkong.loopicserver.module.match


import xingkong.loopicserver.module.bean.TagedFile
import java.util.*

class MatchFloorProperSubnorm : MatchBase() {

    /**
     * 计算匹配分的函数；使用先验标签匹配法
     *
     * @param tagedFile
     * @param inputTag
     */
    override fun computeMp(tagedFile: TagedFile, inputTag: List<String>) {
        val inputTagSize = inputTag.size
        var tagDis = 1.0f
        for (i in tagedFile.tags.size - 1 downTo 1) {
            val tag = tagedFile.tags[i]
            if (inputTag.contains(tag)) {
                if (i < tagedFile.tags.size - 1) {
                    tagDis += PROPOR_STEP
                }
                tagDis += PROPOR_STEP * (i - inputTagSize)
                break
            }
        }

        val rand = Random().nextDouble() * tagDis
        tagedFile.mp = rand   //使用随机数来shuffle
    }

    companion object {

        val PROPOR_STEP = 3.0f
    }
}
