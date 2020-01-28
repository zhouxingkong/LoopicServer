package xingkong.loopicserver.picproc.multiDir

import java.io.File
import java.util.*
import java.util.stream.Collectors

class SingleDirRename {
    var img_index = 0
    var file_num = 0
    internal var dir: String = ""
    internal var files: Array<File>? = null
    internal lateinit var imglist: List<*>

    internal lateinit var indexs: IntArray

    fun getFileList(base: String) {
        dir = base/*+lists[file_index]+"/"*/ + File.separator
        val file = File(dir)
        files = file.listFiles()
        file_num = files!!.size
        val fileList = ArrayList(Arrays.asList(*files!!))
        imglist = fileList.stream().map { f -> f.absolutePath.replace("\\", "/") }.collect(Collectors.toList())

        //        imglist = java.util.Arrays.asList(files);
    }

    fun getFileListByDir(base: String) {
        dir = base/*+lists[file_index]+"/"*/ + File.separator
        val fileList = getFileList(ArrayList(), dir)


        //        File file = new File(dir);
        //        files = file.listFiles();
        file_num = fileList.size
        imglist = fileList.stream().map { f -> f.absolutePath.replace("\\", "/") }.collect(Collectors.toList())
    }

    fun genLinearList() {
        indexs = IntArray(file_num)
        for (j in 0 until file_num) {
            indexs[j] = j    //给索引数组赋值
        }
    }

    fun genRandomList() {
        genLinearList()
        val random = Random()
        for (j in 0 until file_num) {
            val temp = indexs[j]
            val newindx = random.nextInt(file_num - 1)
            indexs[j] = indexs[newindx]  //交换位置
            indexs[newindx] = temp
        }
    }

    fun doRename(pre: String, offset: Int) {
        for (i in 0 until file_num) {

            val path = /*dir + */"" + imglist[img_index]

            val split_by_dir = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var last_part = ""
            last_part = split_by_dir[split_by_dir.size - 1]

            val split_by_dot = last_part.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val suffix = split_by_dot[split_by_dot.size - 1]
            var numStr = "" + (indexs[i] + offset)
            for (j in 0..5) {
                if (numStr.length < 6) {
                    numStr = "0$numStr"
                }
            }
            var outputDir = ""
            for (j in 0 until split_by_dir.size - 1) {
                outputDir += split_by_dir[j] + "/"
            }
            outputDir += "/"
            outputDir += pre
            outputDir += numStr
            outputDir += "."
            outputDir += suffix
            //            System.out.println(outputDir);
            //重命名文件
            val file = File(path)
            val ret = file.renameTo(File(outputDir))
            if (!ret) {
                println("Error!!$path   ->    $outputDir")
            } else {
//                println("success!!$path   ->    $outputDir")
            }

            img_index++
        }
    }

    fun renameLinear(pre: String, offset: Int) {
        genLinearList()
        doRename(pre, offset)

    }

    fun renameRand(pre: String, offset: Int) {
        genRandomList()
        doRename(pre, offset)

    }

    companion object {


        fun getNameRoot(l: Int): String {
            var str = ""
            for (i in 0 until l) {
                str = str + (Math.random() * 26 + 'a'.toDouble()).toChar()
            }
            return str
        }

        fun getFileList(filelist: MutableList<File>, strPath: String): List<File> {

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
                        filelist.add(files[i])
                    }
                }

            }
            return filelist
        }
    }

}
