package xingkong.loopicserver.picproc.tagImg

import java.io.*
import java.util.*

/*管理配置文件*/
class ConfigFileManager {
    var picFilter: PicNameFilter
    var imageCpy: ImageCpy
    var resultStorage: FilterResultStorage
    var targetDir: String = ""
    var exceptDir: String = ""
    internal lateinit var outputFileWriter: BufferedWriter


    internal lateinit var totalList: List<TagedFile>
    internal lateinit var excludeTags: MutableList<String>

    init {
        picFilter = PicNameFilter()
        imageCpy = ImageCpy()
        resultStorage = FilterResultStorage()
    }

    fun initOutputFile(path: String) {
        try {
            val writeName = File(path) // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile() // 创建新文件,有同名的文件的话直接覆盖

            val writer = FileWriter(writeName)
            outputFileWriter = BufferedWriter(writer)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    fun writeOutputLine(outputDesc: OutputDesc) {

        try {
            outputFileWriter.write(outputDesc.toString() + "\r\n") // \r\n即为换行
            outputFileWriter.flush() // 把缓存区内容压入文件
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * 生成随机列表
     *
     * @param file_num
     * @return
     */
    fun genRandomList(file_num: Int): IntArray {
        if (file_num < 2) {
            return intArrayOf(0)
        }
        val indexs = IntArray(file_num)
        for (j in 0 until file_num) {
            indexs[j] = j    //给索引数组赋值
        }
        val random = Random()
        for (j in 0 until file_num) {
            val temp = indexs[j]
            val newindx = random.nextInt(file_num - 1)
            indexs[j] = indexs[newindx]  //交换位置
            indexs[newindx] = temp
        }

        return indexs
    }

    fun makeKey(tags: Array<String>): String {
        var out = tags[0]
        for (i in 1 until tags.size) {
            out = out + DIV + tags[i]
        }
        return out
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
            picName = processPicture(picKey, picNum)
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
    fun processPicture(key: String, picNum: Int): String {
        var key = key
        var tags = key.split(DIV.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (tags.size < 1) return "#"

        var filteredList: List<TagedFile>? = null
        val indexs: IntArray
        var fr: FilterResult? = null
        var storagedNum = 0
        while (filteredList == null || filteredList.size < picNum) {
            storagedNum = resultStorage.searchResultNum(key)   //搜索缓存
            if (storagedNum >= picNum) {
                fr = resultStorage.get(key)
                break
            } else {
                /**/
                filteredList = picFilter.filter(totalList, tags, excludeTags, picNum)    //过滤
                /**/
                val fileNum = filteredList!!.size
                if (fileNum < picNum && tags.size > 1) {
                    key = key.substring(0, key.lastIndexOf(DIV))
                    tags = key.split(DIV.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                } else {
                    indexs = genRandomList(fileNum)
                    fr = FilterResult(filteredList, indexs)
                    resultStorage.put(key, fr)  //缓存结果
                    break
                }

            }

            //todo ：shuffle

        }

        /*拷贝文件*/
        val dstName = imageCpy.copyRandImage(fr!!, "$targetDir/imgs", picNum)
        println("tag=" + key + "resultNum = " + fr.filteredList.size)
        return dstName
    }

    fun parseConfigFile(dir: String) {
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
                totalList = PicNameFilter.getFileList(fileList, "F:/mass/tag_dir")  //获取总共的文件列表
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
            if (str != null && str != null) {
                exceptDir = str.replace(",".toRegex(), "")
                loadExceptTags(exceptDir)
            }

            initOutputFile("$targetDir/index.txt")

            // 按行读取字符串
            str = bf.readLine()
            while (str != null && str.length > 0) {
                if (str.endsWith(",")) {
                    str = str.substring(0, str.length - 1)
                }
                println(str)
                parseOneLine(str)
                str = bf.readLine()
            }
            bf.close()
            fr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

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
                totalList = PicNameFilter.getFileList(fileList, "F:/mass/tag_dir")  //获取总共的文件列表
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
            if (str != null && str != null) {
                exceptDir = str.replace(",".toRegex(), "")
                loadExceptTags(exceptDir)
            }

            bf.close()
            fr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun pickOne(tag: String): String {
        var key = tag
        var tags = key.split(DIV.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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
                filteredList = picFilter.filter(totalList, tags, excludeTags, 1)    //过滤
                /**/
                val fileNum = filteredList!!.size
                if (fileNum < 1 && tags.size > 1) {
                    key = key.substring(0, key.lastIndexOf(DIV))
                    tags = key.split(DIV.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                } else {
                    indexs = genRandomList(fileNum)
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

        var f = filelist[offset]   //由于已经进行了随机数shuffle操作，所以不需要用随机数索引
        var fromPath = f.absolutePath
        val fileName = f.name
        //todo:防止图片重复
        if (fr!!.res > 0) {
            fr.consumeIndex = fr.consumeIndex + 1
        }

        return fromPath
    }


    fun LooFromConfigFile(dir: String) {

        parseConfigFile(dir)
    }

    companion object {
        val DIV = "-"
    }

}
