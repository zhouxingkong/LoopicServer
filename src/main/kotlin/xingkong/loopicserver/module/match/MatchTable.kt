package xingkong.loopicserver.module.match

import xingkong.loopicserver.module.bean.TagedFile
import java.util.*

class MatchTable  : MatchBase() {

    val matchTable = listOf(0,1,1,2,3,4)
    override fun computeMp(tagedFile: TagedFile, inputTag: List<String>) {
        val rand = Random().nextDouble()
        tagedFile.mp = rand   //使用随机数来shuffle
    }

    companion object {
        val LAMDA = 4.0f
    }
}