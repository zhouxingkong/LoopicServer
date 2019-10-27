package xingkong.loopicserver.picproc.tagImg

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class ImageCpy {
    //    public int file_num = 0;
    //    int[] indexs;
    internal var currIndex = 0
    internal var letters = getNameRoot(8)

    internal var duplicate: MutableMap<String, String> = HashMap()


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    fun copyFile(oldPath: String, newPath: String) {
        //        System.out.println(oldPath + "-->" + newPath);
        try {
            var bytesum = 0
            var byteread = 0
            val oldfile = File(oldPath)
            if (oldfile.exists()) { //文件存在时
                val inStream = FileInputStream(oldPath) //读入原文件
                val fs = FileOutputStream(newPath)
                val buffer = ByteArray(1444)
                val length: Int
                byteread = inStream.read(buffer)
                while (byteread != -1) {
                    bytesum += byteread //字节数 文件大小
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

    /**
     * @param fr
     * @param toDir
     * @param num
     */
    fun copyRandImage(fr: FilterResult, toDir: String, num: Int): String {
        var num = num
        val filelist = fr.filteredList
        //        int[] indexs = fr.getRandomIndexs();
        val numLimit = fr.res
        var offset = fr.consumeIndex

        if (num > numLimit) num = numLimit
        var f: TagedFile
        var fromPath = ""
        var toPath = ""

        var numbers = ""
        var suf = ""
        var toFileName = ""
        var i = 0
        while (i < num) {
            //            f = filelist.get(indexs[i + offset]);
            f = filelist[i + offset]   //由于已经进行了随机数shuffle操作，所以不需要用随机数索引
            fromPath = f.absolutePath

            val fileName = f.name
            /*防止图片重复*/
            if (i + offset + 1 < filelist.size && duplicate.containsKey(fileName)) {
                offset++
                i--
                i++
                continue
            }

            numbers = "" + currIndex
            currIndex++
            while (numbers.length < 6) {
                numbers = "0$numbers"
            }
            suf = getSuffix(f.name)
            toFileName = "$letters$numbers.$suf"
            toPath = "$toDir/$toFileName"
            copyFile(fromPath, toPath)

            duplicate[fileName] = "have"
            i++
        }
        fr.consumeIndex = offset + num

        return toFileName
    }

    companion object {

        //后缀
        fun getSuffix(filename: String): String {
            val dix = filename.lastIndexOf('.')
            return if (dix < 0) {
                ""
            } else {
                filename.substring(dix + 1)
            }
        }

        fun getNameRoot(l: Int): String {
            var str = ""
            for (i in 0 until l) {
                str = str + (Math.random() * 26 + 'a'.toDouble()).toChar()
            }
            return str
        }
    }
}
