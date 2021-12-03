package xingkong.loopicserver.module.match

import xingkong.loopicserver.module.bean.TagedFile
import java.util.*

class MatchTable  : MatchBase() {
    val matchTable = listOf(100,110,120,130,140,145,150,155,160,165,170)

    override fun computeMp(tagedFile: TagedFile, inputTag: List<String>):Double  {
        var floorDis = tagedFile.tags.size - inputTag.size
        floorDis = if(floorDis<0) 0 else floorDis
        val factor = if(floorDis>=matchTable.size) matchTable[matchTable.size-1] else matchTable[floorDis]
        val rand = Random().nextDouble() * factor
        return rand   //使用随机数来shuffle
    }
}