package xingkong.loopicserver.module

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xingkong.loopicserver.module.bean.SceneInfo
import xingkong.loopicserver.module.bean.StoryInfo
import xingkong.loopicserver.module.bean.TagedFile
import xingkong.loopicserver.module.utils.FileUtil
import xingkong.loopicserver.module.utils.NetUtil
import xingkong.loopicserver.module.utils.TagUtil
import java.io.*
import java.net.Inet4Address
import java.util.ArrayList

object ConfigFileManager {

    var targetDir: String = ""
    var storyListDir:String = ""

    internal lateinit var excludeTags: MutableList<String>

    internal lateinit var totalList: List<TagedFile>
    val storys = mutableListOf<StoryInfo>()

    fun getPic(storyId:Int,sceneId:Int,ser:Int):String{
        val picList = storys.getOrNull(storyId)?.sceneList?.getOrNull(sceneId)?.picPath
        return picList?.let{
            val index = ser % it.size
            it[index]
        }?:"error"
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

    fun getAllTextList():List<List<String>>{
        return storys.map { it.sceneList.map { it.text } }
    }


    fun systemInit(dir: String) {
        val name = "$dir/index.csv"
        try {
            val fr = InputStreamReader(FileInputStream(name),"UTF-8")
            fr.encoding
            val bf = BufferedReader(fr)
            var str: String?
            /*第一行:读取源文件路径*/
            str = bf.readLine()
            if (str != null) {
                str = str.replace(",".toRegex(), "")
                val fileList = ArrayList<TagedFile>()
                val srcDir: String = str.trim().replace("\\", "/")
                println("源文件:$srcDir")
                totalList = FileUtil.getFileList(fileList, "E:/mass/tag_dir")  //获取总共的文件列表
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

            println("ip=${NetUtil.getIPAddress()}")

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
                    GlobalScope.launch(Dispatchers.IO) {
                        makeStoryInfo(index,file)
                    }
                }
        }
    }

    fun makeStoryInfo(index:Int,f:File){
        val storyInfo = StoryInfo().apply {
            id = index
            rootPath = f.absolutePath
        }
        try{
            val fr = InputStreamReader(FileInputStream(f),"UTF-8")
            val bf = BufferedReader(fr)


            var str = bf.readLine()
            while (str != null) {

                if (str.length < 1) continue

                if (str.startsWith("include:")) {
                    str = str.replace(",", "")
                    str = str.replace("include:", "")
                    parseSubTagFile(storyInfo,f.parentFile.absolutePath + "/sub/" + str + ".csv")
                    str = bf.readLine()
                    continue;
                }

                if (str.endsWith(",")) {
                    str = str.substring(0, str.length - 1)
                }
                buildTagOrder(storyInfo,str)
                str = bf.readLine()
            }

            storys.add(storyInfo)

            println("ready=${NetUtil.getIPAddress()}")
            bf.close()
            fr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun parseSubTagFile(storyInfo:StoryInfo,name: String) {
        try {
            val fr = InputStreamReader(FileInputStream(name),"UTF-8")
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
        print(">")
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
        while (str != null && str.isNotEmpty()) {
            if(!str.startsWith("[")) excludeTags.add(str)
            str = bf.readLine()
        }
        println("排除配置个数:" + excludeTags.size)
    }
}