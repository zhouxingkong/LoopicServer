package xingkong.loopicserver.module

import xingkong.loopicserver.module.bean.SceneInfo
import xingkong.loopicserver.module.bean.StoryInfo
import xingkong.loopicserver.module.bean.TagedFile
import xingkong.loopicserver.module.utils.FileUtil
import xingkong.loopicserver.module.utils.TagUtil
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.ArrayList

object ConfigFileManager {

    var targetDir: String = ""
    var storyListDir:String = ""

    internal lateinit var excludeTags: MutableList<String>

    internal lateinit var totalList: List<TagedFile>
    val storys = mutableListOf<StoryInfo>()

    fun getPic(storyId:Int,sceneId:Int,ser:Int):String{
        val picList = storys.getOrNull(storyId)?.sceneList?.getOrNull(sceneId)?.picPath
        return picList?.get(ser)?:"error"
    }

    fun getStoryList():List<String>{
        return storys.map { it.rootPath }
    }

    fun getSceneList(storyId:Int):List<String>{
        return storys.getOrNull(storyId)?.sceneList?.map{it.rawTag} ?: listOf<String>()
    }

    fun getTextList(storyId:Int):List<String>{
        return storys.getOrNull(storyId)?.sceneList?.map { it.text } ?: listOf<String>()
    }


    fun systemInit(dir: String) {
        val name = "$dir/index.csv"
        try {
            val fr = FileReader(name)
            val bf = BufferedReader(fr)
            var str: String?
            /*第一行:读取源文件路径*/
            str = bf.readLine()
            if (str != null) {
                str = str.replace(",".toRegex(), "")
                val fileList = ArrayList<TagedFile>()
                val srcDir: String = str.trim().replace("\\", "/")
                println("源文件:$srcDir")
                totalList = FileUtil.getFileList(fileList, "/Users/xingkong/loo/tag_dir")  //获取总共的文件列表
                println("源文件个数:" + totalList.size)
            }

            /*第二行:目标文件路径*/
            str = bf.readLine()
            if (str != null) {
                targetDir = str.replace(",".toRegex(), "")
                println("目标文件:$targetDir")
                val picDir = File("$targetDir/imgs")
                if (!picDir.exists()) {
                    picDir.mkdirs()
                }
            }

            /*第三行:读取排除文件路径*/
            str = bf.readLine()
            if (str != null) {
                val exceptDir = str.replace(",".toRegex(), "")
                loadExceptTags(exceptDir)
            }

            /*第三行:读取排除文件路径*/
            str = bf.readLine()
            if (str != null) {
                storyListDir = str.replace(",".toRegex(), "")
            }
            loadStoryList(storyListDir)


            bf.close()
            fr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * 加载故事列表
     */
    fun loadStoryList(dir:String){
        val file = File(dir)
        if(file.exists()){
            storys.clear()
            file.listFiles()
                    .filter { it.isFile }.mapIndexed { index, file ->
                        makeStoryInfo(index,file)
                    }.toList()
        }
    }

    fun makeStoryInfo(index:Int,f:File){
        val storyInfo = StoryInfo().apply {
            id = index
            rootPath = f.absolutePath
        }
        try{
            val fr = FileReader(f)
            val bf = BufferedReader(fr)


            var str = bf.readLine()
            while (str != null) {

                if (str.length < 1) continue

                if (str.startsWith("include:")) {
                    str = str.replace(",", "")
                    str = str.replace("include:", "")
                    parseSubTagFile(storyInfo,f.absolutePath + "/sub/" + str + ".csv")
                    continue;
                }

                if (str.endsWith(",")) {
                    str = str.substring(0, str.length - 1)
                }
                buildTagOrder(storyInfo,str)
                str = bf.readLine()
            }

            storys.add(storyInfo)

            bf.close()
            fr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun parseSubTagFile(storyInfo:StoryInfo,name: String) {
        try {
            val fr = FileReader(name)
            val bf = BufferedReader(fr)
            var str: String?

            // 按行读取字符串
            str = bf.readLine()
            while (str != null && str.length > 0) {

//                if(str.startsWith("include:")){
//                    str=str.replace(",","")
//                    str=str.replace("include:","")
//                    parseSubTagFile(dir+"/sub/"+str+".csv")
//                    continue;
//                }
                if (str.endsWith(",")) {
                    str = str.substring(0, str.length - 1)
                }
                buildTagOrder(storyInfo,str)
                str = bf.readLine()
            }

            bf.close()
            fr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun buildTagOrder(storyInfo:StoryInfo,rawTag: String) {
        val rawTag = rawTag.trim().trimStart()
        if (rawTag.startsWith("#") || rawTag.isEmpty()) { //忽略注释
            return
        }
        val nameSplitSpace = rawTag.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()    //空格分开不同的部分
        if (nameSplitSpace.size < 2) return
        val sceneInfo = SceneInfo()

        /*配图*/
        sceneInfo.rawTag = nameSplitSpace[0]
        sceneInfo.picTag.addAll(TagUtil.splitTag(nameSplitSpace[0]))
        sceneInfo.picPath = TagManager.getFileListByTag(sceneInfo.picTag)

        /*配音频*/
        if (nameSplitSpace.size > 2) {
            val soundTag = nameSplitSpace[1]
            val soundMode = nameSplitSpace[2]
//            outputDesc.setSoundTag(soundTag)
//            outputDesc.setSoundMode(soundMode)
        }

        /*配文字*/
        if (nameSplitSpace.size > 3) {
            val text = nameSplitSpace[3]
            sceneInfo.text = text
//            outputDesc.setText(text)
//            if (text.length > 1) {  //存在描述就把描述放进来
//                textList.add(text)
//            } else {      //不存在描述就放空值
//                textList.add("")
//            }
        }

        storyInfo.sceneList.add(sceneInfo)

    }

    /**
     * 读取排除标签的配置文件
     *
     * @param dir
     * @throws IOException
     */
    @Throws(IOException::class)
    fun loadExceptTags(dir: String) {
        excludeTags = ArrayList()
        val fr = FileReader(dir)
        val bf = BufferedReader(fr)
        var str: String?

        /*读取源文件路径*/
        str = bf.readLine()
        while (str != null && str.length > 0) {
            excludeTags.add(str)
            str = bf.readLine()
        }

        println("排除配置个数:" + excludeTags.size)

    }
}