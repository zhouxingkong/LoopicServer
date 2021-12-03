package xingkong.loopicserver.module


import xingkong.loopicserver.module.bean.TagedFile
import xingkong.loopicserver.module.match.MatchBase
import xingkong.loopicserver.module.match.MatchFloorProperSubnorm
import xingkong.loopicserver.module.match.MatchTable
import java.util.*
import java.util.stream.Collectors

class PicNameFilter {

    /**
     * 引入策略模式，将不同标签匹配策略封装到子类中
     */
    internal var matchBase: MatchBase = MatchTable()


    fun filter(inputList: List<TagedFile>,
               tags: List<String>,
               excludeTag: List<String>
    ): List<TagedFile> {
        var result: List<TagedFile>? = null
        while (result == null) {
            result = inputList.stream()
                    .filter { f: TagedFile -> judgeCandidate(f, tags, excludeTag) }    //step1: 找出所有全部包含目标标签组的文件集合
                    .map { tagedFile -> Pair(matchBase.computeMp(tagedFile, tags),tagedFile)  } //step2:使用标签匹配算法来排序文件的优先级
                    .sorted(Comparator.comparing<Pair<Double,TagedFile>, Double> { it.first })    //step3:将结果按照匹配分排序
                    .map { it.second }
                    .collect(Collectors.toList())
            /*打印结果*/
            //            printResult(result, inputTag);
        }

        return result
    }


    fun printResult(result: List<TagedFile>, inputTag: List<String>) {
        println("------------------------")
        print("目标标签:")
        for (t in inputTag) {
            print("-$t")
        }
        println(" ")
        for (t in result) {
            for (s in t.tags) {
                print("-$s")
            }
//            println(" ;mp=" + t.mp)
        }
    }

    fun judgeCandidate(f: TagedFile,
                       inputTag: List<String>,
                       excludeTag: List<String>): Boolean {
        val judgeList = mutableListOf<String>()
        //判断exclude标签
        judgeList.let{
            it.addAll(f.tags)
            it.retainAll(excludeTag)
            it.removeAll(inputTag)
            if(it.isNotEmpty()) return false
        }

        //判断匹配标签
        judgeList.let{
            it.clear()
            it.addAll(inputTag)
            it.removeAll(f.tags)
            if(it.isNotEmpty()) return false
        }

        //判断非标签
        judgeList.let{
            it.clear()
            it.addAll(f.tags)
            val notTags = inputTag.filter { it.startsWith("!") }.map { it.substring(1) }
            it.retainAll(notTags)
            if(it.isNotEmpty()) return false
        }

        return true
    }

}
