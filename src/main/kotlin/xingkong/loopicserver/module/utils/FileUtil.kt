package xingkong.loopicserver.module.utils

import xingkong.loopicserver.module.bean.TagedFile
import java.io.File

object FileUtil {

    fun getFileList(filelist: MutableList<TagedFile>, strPath: String,tag:MutableList<String> = mutableListOf()): List<TagedFile> {
        val dir = File(strPath)
        val files = dir.listFiles() // 该文件目录下文件全部放入数组
        if (files != null) {
            for (i in files.indices) {
                if (files[i].isDirectory) { // 判断是文件还是文件夹
                    tag.add(files[i].name)
                    getFileList(filelist, files[i].absolutePath,tag) // 获取文件绝对路径
                } else {
                    filelist.add(TagedFile(files[i],tag))   //构造TagedFile时就加好了标签
                }
            }
        }
        if(tag.isNotEmpty()) tag.removeAt(tag.size-1)
        return filelist
    }

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