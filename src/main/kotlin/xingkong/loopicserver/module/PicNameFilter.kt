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
                    .peek { tagedFile -> matchBase.computeMp(tagedFile, tags) } //step2:使用标签匹配算法来排序文件的优先级
                    .sorted(Comparator.comparing<TagedFile, Double> { o -> o.mp })    //step3:将结果按照匹配分排序
                    .collect(Collectors.toList())
            //result.sort(Comparator.comparing(o -> o.mp));
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
            println(" ;mp=" + t.mp)
        }
    }

    var judgeExcludeList = mutableListOf<String>()
    var judgeMatchList = mutableListOf<String>()
    var judgeNotMatchList = mutableListOf<String>()
    fun judgeCandidate(f: TagedFile,
                       inputTag: List<String>,
                       excludeTag: List<String>): Boolean {
        //判断exclude标签
        judgeExcludeList.let{
            it.clear()
            it.addAll(f.tags)
            it.retainAll(excludeTag)
            it.removeAll(inputTag)
            if(it.isNotEmpty()) return false
        }

        //判断匹配标签
        judgeMatchList.let{
            it.clear()
            it.addAll(inputTag)
            it.removeAll(f.tags)
            if(it.isNotEmpty()) return false
        }

        //判断非标签
        judgeNotMatchList.let{
            it.clear()
            it.addAll(f.tags)
            val notTags = inputTag.filter { it.startsWith("!") }.map { it.substring(1) }
            it.retainAll(notTags)
            if(it.isNotEmpty()) return false
        }


//        f.tags.retainAll(excludeTag)
//        /*step1:排除标签*/
//        for (ext in excludeTag) {
//            val fileTagMach = f.tags.contains(ext)
//            var inputTagMach = false
//            for (`in` in inputTag) {
//                if (`in`.contains(ext)) {
//                    inputTagMach = true
//                    break
//                }
//            }
//            if (!inputTagMach && fileTagMach) {
//                return false
//            }
//        }

        /*step2: 验证标签*/
//        for (curr in inputTag) {
//            var t = curr
//            var stat = true
//            if (t.startsWith("!")) {
//                stat = false
//                t = t.substring(1)
//            }
//
//            var output = !stat //当前这条tag是否满足
//
//            for (ft in f.tags) {
//                if (tagMatch(ft,t)) {
//                    if (stat) {
//                        output = true
//                        break
//                    } else {
//                        return false
//                    }
//                }
//            }
//
//            if (!output) {
//                return false
//            }
//        }
        return true
    }

    fun tagMatch(target:String,cond:String):Boolean{
//        return target.matches(cond.toRegex())
        return target == cond
    }


}
