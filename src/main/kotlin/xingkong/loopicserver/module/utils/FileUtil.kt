package xingkong.loopicserver.module.utils

import xingkong.loopicserver.module.ConfigFileManager
import xingkong.loopicserver.module.bean.SceneInfo
import xingkong.loopicserver.module.bean.TagedFile
import java.io.*

object FileUtil {

    fun getFileList(
            filelist: MutableList<TagedFile>,
            strPath: String,
            tag:MutableList<String> = mutableListOf()
    ): List<TagedFile> {
        val dir = File(strPath)
        val files = dir.listFiles() // 该文件目录下文件全部放入数组
        if (files != null) {
            for (i in files.indices) {
                if (files[i].isDirectory) { // 判断是文件还是文件夹
                    val fileName = files[i].name
                    val newTags = files[i].name.split("-")
                    newTags.forEach {
                        if (tag.contains(it)) {
                            println("BUG!!:${files[i].absolutePath}")
                        }
                    }
                    tag.addAll(newTags)
                    if (fileName.endsWith("story")) {     //个体故事
                        getListForSingleStory(files[i], files[i].name)
                    } else {   //批量图
                        getFileList(filelist, files[i].absolutePath, tag) // 获取文件绝对路径
                        if (tag.isNotEmpty()) {
                            newTags.forEach {
                                tag.removeAt(tag.lastIndexOf(it))
                            }
                        }
                    }
                } else {
                    filelist.add(TagedFile(files[i],tag))   //构造TagedFile时就加好了标签
                }
            }
        }
        return filelist
    }

    fun getListForSingleStory(root:File,tag:String){
        val pathList = root.listFiles().filter { it.isFile }.map { it.absolutePath }

        val scene = SceneInfo().apply {
            rawTag = tag
            picTag = mutableListOf(tag)
            soundTag = ""
            text = ""
            picPath = pathList
        }
        ConfigFileManager.singleStorys.add(scene)
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

    fun readInput(f:File,inputReader:(reader: BufferedReader)->Unit){
        try{
            val fr = InputStreamReader(FileInputStream(f),"UTF-8")
            val bf = BufferedReader(fr)
            inputReader.invoke(bf)
            bf.close()
            fr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readInput(path:String,inputReader:(reader: BufferedReader)->Unit){
        try{
            val fr = InputStreamReader(FileInputStream(path),"UTF-8")
            val bf = BufferedReader(fr)
            inputReader.invoke(bf)
            bf.close()
            fr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}