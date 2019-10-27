package xingkong.loopicserver.picproc.tagImg

import java.io.File

/*图片标签管理*/
class TagManager {
    var img_index = 0
    var file_num = 0
    internal var dir: String = ""
    internal var files: Array<String>? = null
    internal lateinit var imglist: MutableList<String>
    internal var indexs: IntArray? = null

    fun getFileList(base: String) {
        dir = "$base/"
        val file = File(dir)
        files = file.list()
        imglist = ArrayList()
        file_num = 0
        val files = file.listFiles() // 该文件目录下文件全部放入数组
        if (files != null) {
            for (i in files.indices) {
                if (files[i].isDirectory) { // 判断是文件还是文件夹

                } else {
                    file_num++
                    imglist.add(files[i].name)
                }
            }

        }
        //        imglist = java.util.Arrays.asList(files);
    }


    fun addTagToName(name: String?, tag: String): String {
        if (name == null || name.length < 2) return ""
        /**/
        val nameSplitDot = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (nameSplitDot.size < 1) return ""
        val suffix = nameSplitDot[nameSplitDot.size - 1]    //文件後綴

        val nameSplitLine = nameSplitDot[0].split(DIV.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (nameSplitLine.size < 1) return ""

        for (s in nameSplitLine) {
            if (tag == s) {
                return nameSplitDot[0] + "." + suffix
            }
            //            System.out.println(s+":"+tag);
        }
        return nameSplitDot[0] + DIV + tag + "." + suffix

    }

    fun removeTagFromName(name: String?, tag: String): String {
        if (name == null || name.length < 2) return ""
        /**/
        val nameSplitDot = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (nameSplitDot.size < 1) return ""
        val suffix = nameSplitDot[nameSplitDot.size - 1]    //文件後綴

        val nameSplitLine = nameSplitDot[0].split(DIV.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (nameSplitLine.size < 1) return ""

        var outputName = nameSplitLine[0]
        for (i in 1 until nameSplitLine.size) {
            if (tag != nameSplitLine[i]) {
                outputName = outputName + DIV + nameSplitLine[i]
            }
            //            System.out.println(s+":"+tag);
        }
        return "$outputName.$suffix"

    }

    fun clearTagFromName(name: String?): String {
        if (name == null || name.length < 1) return ""
        /**/
        val nameSplitDot = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (nameSplitDot.size < 1) return ""
        val suffix = nameSplitDot[nameSplitDot.size - 1]    //文件後綴

        val nameSplitLine = nameSplitDot[0].split(DIV.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (nameSplitLine.size < 1) return ""

        val outputName = nameSplitLine[0]
        return "$outputName.$suffix"
    }


    fun renameFile(fromPath: String, toPath: String) {
        val file = File(fromPath)
        val ret = file.renameTo(File(toPath))
        if (!ret) {
            println("Error!!$fromPath   ->    $toPath")
        }
    }

    fun doAddTag(dir: String, tag: String) {
        img_index = 0
        for (i in 0 until file_num) {

            val oldName = "" + imglist[img_index]
            val newName = addTagToName(oldName, tag)

            //重命名文件
            renameFile(dir + oldName, dir + newName)

            img_index++
        }
    }

    fun doRemoveTag(dir: String, tag: String) {
        img_index = 0
        for (i in 0 until file_num) {

            val oldName = "" + imglist[img_index]
            val newName = removeTagFromName(oldName, tag)

            //重命名文件
            renameFile(dir + oldName, dir + newName)

            img_index++
        }
    }

    fun doClearTag(dir: String) {
        img_index = 0
        for (i in 0 until file_num) {
            val oldName = "" + imglist[img_index]
            val newName = clearTagFromName(oldName)
            //重命名文件
            renameFile(dir + oldName, dir + newName)

            img_index++
        }
    }

    fun addTag(dir: String, tag: String) {
        getFileList(dir)
        doAddTag("$dir/", tag)
    }

    fun removeTag(dir: String, tag: String) {
        getFileList(dir)
        doRemoveTag("$dir/", tag)
    }

    fun clearTag(dir: String) {
        getFileList(dir)
        doClearTag("$dir/")
    }

    /**
     * 自动添加一些标签
     *
     * @param tags
     */
    fun autoAddTag(tags: MutableList<String>) {
        if (!tags.contains("黑夜")) {
            tags.add("白天")
        }
    }

    fun doAddTagByDir(tags: MutableList<String>, strPath: String) {

        val dir = File(strPath)
        val fileName = dir.name
        val dirStr = dir.absolutePath
        tags.add(fileName)
        //autoAddTag(tags);
        clearTag(dirStr)
        for (t in tags) {
            if (ROOT_TAG != t) addTag(dirStr, t)
        }
        val files = dir.listFiles() // 该文件目录下文件全部放入数组
        if (files != null) {
            for (i in files.indices) {
                if (files[i].isDirectory) { // 判断是文件还是文件夹
                    doAddTagByDir(tags, files[i].absolutePath) // 获取文件绝对路径
                }

            }

        }
        tags.removeAt(tags.size - 1)
    }

    fun addTagByDir(dir: String) {
        doAddTagByDir(ArrayList(), dir)
    }

    companion object {
        val DIV = "-"
        val ROOT_TAG = "tag_dir"
    }
}
