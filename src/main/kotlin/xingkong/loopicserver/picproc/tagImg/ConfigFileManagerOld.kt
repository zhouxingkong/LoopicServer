package xingkong.loopicserver.picproc.tagImg

import xingkong.loopicserver.module.PicNameFilter
import xingkong.loopicserver.module.bean.TagedFile
import xingkong.loopicserver.module.utils.FileUtil
import xingkong.loopicserver.module.utils.ListUtil
import xingkong.loopicserver.module.utils.TagUtil
import java.io.*
import java.util.*

/*管理配置文件*/
class ConfigFileManagerOld {
    var picFilter: PicNameFilter
    var imageCpy: ImageCpy
    var resultStorage: FilterResultStorage
    var targetDir: String = ""
    var exceptDir: String = ""
    var tmpDir: String = ""    //缓存路径
    internal lateinit var outputFileWriter: BufferedWriter

    internal var textList: MutableList<String> = mutableListOf()
    internal var tagList: MutableList<String> = mutableListOf()
    internal lateinit var totalList: List<TagedFile>
    internal lateinit var excludeTags: MutableList<String>
    internal var tagOrder: MutableList<String> = mutableListOf()
    internal var redirectFile: MutableMap<String, String> = mutableMapOf()    //重定向文件列表,用于尺寸缩放

    init {
        picFilter = PicNameFilter()
        imageCpy = ImageCpy()
        resultStorage = FilterResultStorage()
    }

    internal val isEncoded = false

//    fun initOutputFile(path: String) {
//        try {
//            val writeName = File(path) // 相对路径，如果没有则要建立一个新的output.txt文件
//            writeName.createNewFile() // 创建新文件,有同名的文件的话直接覆盖
//
//            val writer = FileWriter(writeName)
//            outputFileWriter = BufferedWriter(writer)
//
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//    }


    fun writeOutputLine(outputDesc: OutputDesc) {

        try {
            outputFileWriter.write(outputDesc.toString() + "\r\n") // \r\n即为换行
            outputFileWriter.flush() // 把缓存区内容压入文件
        } catch (e: IOException) {
            e.printStackTrace()
        }

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

    /*解析一行配置文件*/
    fun parseOneLine(line: String) {
        val nameSplitSpace = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()    //空格分开不同的部分
        if (nameSplitSpace.size < 2) return
        val picNum = 1   //总共图片数量

        val outputDesc = OutputDesc()

        /*配图*/
        val picKey = nameSplitSpace[0]     //storage的键
        var picName: String? = "#"
        if (picKey != "#") {
//            picName = processPicture(picKey, picNum)
            picName = ""
        }
        if (picName == null || picName.length < 1) {
            picName = "#"
        }
        outputDesc.setPicFileName(picName)

        /*配音频*/
        if (nameSplitSpace.size > 2) {
            val soundTag = nameSplitSpace[1]
            val soundMode = nameSplitSpace[2]
            outputDesc.setSoundTag(soundTag)
            outputDesc.setSoundMode(soundMode)
        }

        /*配文字*/
        if (nameSplitSpace.size > 3) {
            val text = nameSplitSpace[3]
            outputDesc.setText(text)
        }

        /*输出文件*/
        writeOutputLine(outputDesc)
    }

    /**
     * 处理一张图片的输出
     *
     * @param key
     * @param picNum
     */
//    fun processPicture(key: String, picNum: Int): String {
//        var key = key
//        var tags = key.split(DIV.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//        if (tags.size < 1) return "#"
//
//        var filteredList: List<TagedFile>? = null
//        val indexs: IntArray
//        var fr: FilterResult? = null
//        var storagedNum = 0
//        while (filteredList == null || filteredList.size < picNum) {
//            storagedNum = resultStorage.searchResultNum(key)   //搜索缓存
//            if (storagedNum >= picNum) {
//                fr = resultStorage.get(key)
//                break
//            } else {
//                /**/
//                filteredList = picFilter.filter(totalList, tags, excludeTags, picNum)    //过滤
//                /**/
//                val fileNum = filteredList!!.size
//                if (fileNum < picNum && tags.size > 1) {
//                    key = key.substring(0, key.lastIndexOf(DIV))
//                    tags = key.split(DIV.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                } else {
//                    indexs = ListUtil.genRandomList(fileNum)
//                    fr = FilterResult(filteredList, indexs)
//                    resultStorage.put(key, fr)  //缓存结果
//                    break
//                }
//
//            }
//        }
//
//        /*拷贝文件*/
//        val dstName = imageCpy.copyRandImage(fr!!, "$targetDir/imgs", picNum)
////        println("tag=" + key + "resultNum = " + fr.filteredList.size)
//        return dstName
//    }

    fun buildTagOrder(rawTag: String) {
        var rawTag = rawTag.trim().trimStart()

        if (rawTag.startsWith("#") || rawTag.length < 1) { //忽略注释
            return
        }
        val nameSplitSpace = rawTag.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()    //空格分开不同的部分
        if (nameSplitSpace.size < 2) return
        val picNum = 1   //总共图片数量

        val outputDesc = OutputDesc()
        tagList.add(encodeBase64(nameSplitSpace[0]))
        /*配图*/
        val picKey = nameSplitSpace[0]     //storage的键
        //todo:检查标签合法性

        tagOrder.add(picKey)
        outputDesc.setPicFileName(picKey)

        /*配音频*/
        if (nameSplitSpace.size > 2) {
            val soundTag = nameSplitSpace[1]
            val soundMode = nameSplitSpace[2]
            outputDesc.setSoundTag(soundTag)
            outputDesc.setSoundMode(soundMode)
        }

        /*配文字*/
        if (nameSplitSpace.size > 3) {
            val text = nameSplitSpace[3]
            outputDesc.setText(text)
            if (text.length > 1) {  //存在描述就把描述放进来

                textList.add(encodeBase64(text))
            } else {      //不存在描述就放空值
                textList.add("")
            }
        }

    }

    fun encodeBase64(data: String): String {
        if (!isEncoded) return data
        return String(Base64.getEncoder().encode(data.toByteArray()))
    }

    /**
     * 系统初始化，用于服务端访问
     */
    fun systemInit(dir: String) {
        val name = "$dir/loo.csv"
        //        ArrayList<String> arrayList = new ArrayList<>();
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
                exceptDir = str.replace(",".toRegex(), "")
                loadExceptTags(exceptDir)
            }

            //第四行:文件缩放保存地址
//            str = bf.readLine()
//            if (str != null) {
//                exceptDir = str.replace(",".toRegex(), "")
//                loadExceptTags(exceptDir)
//            }

            // 按行读取字符串
            str = bf.readLine()
            while (str != null) {

                if (str.length < 1) continue

                if (str.startsWith("include:")) {
                    str = str.replace(",", "")
                    str = str.replace("include:", "")
                    parseSubTagFile(dir + "/sub/" + str + ".csv")
                    continue;
                }

                if (str.endsWith(",")) {
                    str = str.substring(0, str.length - 1)
                }
                buildTagOrder(str)
                str = bf.readLine()
            }

            bf.close()
            fr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun parseSubTagFile(name: String) {
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
                buildTagOrder(str)
                str = bf.readLine()
            }

            bf.close()
            fr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun pickOneWithServerConfig(index: Int): String {
        var index = index
        if (index >= tagOrder.size) {
            index = 0
        }
        return pickOne(tagOrder[index])
    }

    fun pickOne(tag: String): String {
        var key = tag
        var tags = TagUtil.splitTag(key)
        if (tags.size < 1) return "#"

        var filteredList: List<TagedFile>? = null
        val indexs: IntArray
        var fr: FilterResult? = null
        var storagedNum = 0
        while (filteredList == null || filteredList.size < 1) {
            storagedNum = resultStorage.searchResultNum(key)   //搜索缓存
            if (storagedNum >= 1) {
                fr = resultStorage.get(key)
                break
            } else {
                /**/
                filteredList = picFilter.filter(totalList, tags, excludeTags)    //过滤
                /**/
                val fileNum = filteredList!!.size
                if (fileNum < 1 && tags.size > 1) {
                    key = key.substring(0, key.lastIndexOf(DIV))
                    tags = key.split(DIV.toRegex()).dropLastWhile { it.isEmpty() }
                } else {
                    indexs = ListUtil.genRandomList(fileNum)
                    fr = FilterResult(filteredList, indexs)
                    resultStorage.put(key, fr)  //缓存结果
                    break
                }

            }
            //todo ：shuffle
        }

        val filelist = fr!!.filteredList
        val numLimit = fr!!.res
        var offset = fr!!.consumeIndex

        if (offset >= filelist.size) return "error:$tag"
        var f = filelist[offset]   //由于已经进行了随机数shuffle操作，所以不需要用随机数索引
        var fromPath = f.absolutePath
        val fileName = f.name
        //todo:防止图片重复
        if (fr!!.res > 0) {
            fr.consumeIndex = fr.consumeIndex + 1
        }

        return fromPath
    }

    fun getTextList(): MutableList<String> {
        return textList
    }

    fun getChapterList(): MutableList<String> {
        return tagList
    }


    fun LooFromConfigFile(dir: String) {

//        parseConfigFile(dir)
    }

    companion object {
        val DIV = "-"
    }

}
