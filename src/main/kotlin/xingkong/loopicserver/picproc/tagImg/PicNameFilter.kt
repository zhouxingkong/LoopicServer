package xingkong.loopicserver.picproc.tagImg


import xingkong.loopicserver.picproc.tagImg.match.MatchBase
import xingkong.loopicserver.picproc.tagImg.match.MatchFloorProperSubnorm
import java.io.File
import java.util.*
import java.util.stream.Collectors

class PicNameFilter {

    /**
     * 引入策略模式，将不同标签匹配策略封装到子类中
     */
    internal var matchBase: MatchBase = MatchFloorProperSubnorm()

    fun filter(inputList: List<TagedFile>, tags: Array<String>, excludeTag: List<String>, num: Int): List<TagedFile> {
        var result: List<TagedFile>? = null
        val inputTag = ArrayList(Arrays.asList(*tags))
        while (result == null) {
            result = inputList.stream()
                    .filter { f: TagedFile -> judgeCandidate(f, inputTag, excludeTag) }    //step1: 找出所有全部包含目标标签组的文件集合
                    .peek { tagedFile -> matchBase.computeMp(tagedFile, inputTag) } //step2:使用标签匹配算法来排序文件的优先级
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

    fun judgeCandidate(f: TagedFile, inputTag: List<String>, excludeTag: List<String>): Boolean {
        /*step1:排除标签*/
        for (ext in excludeTag) {
            val fileTagMach = f.tags.contains(ext)
            var inputTagMach = false
            for (`in` in inputTag) {
                if (`in`.contains(ext)) {
                    inputTagMach = true
                    break
                }
            }
            if (!inputTagMach && fileTagMach) {
                return false
            }
        }

        /*step2: 验证标签*/
        for (curr in inputTag) {
            var t = curr
            var stat = true
            if (t.startsWith("!")) {
                stat = false
                t = t.substring(1)
            }

            var output = !stat //当前这条tag是否满足

            for (ft in f.tags) {
                if (ft.matches(t.toRegex())) {
                    if (stat) {
                        output = true
                        break
                    } else {
                        return false
                    }
                }
            }

            if (!output) {
                return false
            }
        }
        return true
    }

    companion object {

        fun getFileList(filelist: MutableList<TagedFile>, strPath: String): List<TagedFile> {

            val dir = File(strPath)
            val files = dir.listFiles() // 该文件目录下文件全部放入数组
            if (files != null) {
                for (i in files.indices) {
                    //                String fileName = files[i].getName();
                    if (files[i].isDirectory) { // 判断是文件还是文件夹
                        getFileList(filelist, files[i].absolutePath) // 获取文件绝对路径
                    } else {
                        //                    String strFileName = files[i].getAbsolutePath();
                        //                    System.out.println("---" + strFileName);
                        filelist.add(TagedFile(files[i]))
                    }
                }

            }
            return filelist
        }
    }

    //    public boolean judgeCandidate(TagedFile f, List<String> inputTag) {
    //        for (String t : inputTag) {
    //            /**/
    //            String[] subTags = t.split("\\|");
    //            boolean output = false; //当前这条tag是否满足
    //            for (String sub : subTags) {
    //                boolean stat = true;
    //
    //                if (sub.startsWith("!")) {
    //                    stat = false;
    //                    sub = sub.substring(1);
    //                }
    //                if (stat ^ (!f.tags.contains(sub))) {
    //                    output = true;
    //                    break;
    //                }
    //
    //            }
    //
    //            if (!output) {
    //                return false;
    //            }
    //
    //        }
    //        return true;
    //    }

}
