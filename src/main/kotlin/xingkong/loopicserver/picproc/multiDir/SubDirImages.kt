package xingkong.loopicserver.picproc.multiDir

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

/**/
class SubDirImages {

    var img_index = 0
    var file_num = 0
    internal lateinit var indexs: IntArray
    internal var dir: String? = null
    internal var files: Array<String>? = null
    internal var imglist: List<*>? = null

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

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    fun copyFile(oldPath: String, newPath: String) {
        println("$oldPath-->$newPath")
        try {
            var bytesum = 0
            var byteread = 0
            val oldfile = File(oldPath)
            if (oldfile.exists()) { //文件存在时
                val inStream = FileInputStream(oldPath) //读入原文件
                val fs = FileOutputStream(newPath)
                val buffer = ByteArray(1444)
                byteread = inStream.read(buffer)
                while (byteread != -1) {
                    bytesum += byteread //字节数 文件大小
                    println(bytesum)
                    fs.write(buffer, 0, byteread)
                    byteread = inStream.read(buffer)
                }
                inStream.close()
            }
        } catch (e: Exception) {
            println("复制单个文件操作出错")
            e.printStackTrace()

        }

    }

    fun copyRandImage(fromDir: String, toDir: String, num: Int) {
        var num = num
        val filelist = SubDirImages.getFileList(ArrayList(), fromDir)
        file_num = filelist.size
        if (num > file_num) num = file_num
        genRandomList()
        var f: File
        var fromPath = ""
        var toPath = ""

        val letters = SingleDirRename.getNameRoot(8)
        var numbers = ""
        var suf = ""
        println(num)
        for (i in 0 until num) {
            f = filelist[indexs[i]]
            fromPath = f.absolutePath

            numbers = "" + i
            while (numbers.length < 6) {
                numbers = "0$numbers"
            }
            suf = getSuffix(f.name)

            toPath = "$toDir/$letters$numbers.$suf"
            copyFile(fromPath, toPath)
        }

    }

    companion object {


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

        //后缀
        fun getSuffix(filename: String): String {
            val dix = filename.lastIndexOf('.')
            return if (dix < 0) {
                ""
            } else {
                filename.substring(dix + 1)
            }
        }
    }
}
