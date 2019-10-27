package xingkong.loopicserver.service.storage.error

import java.util.*

/**
 * 通用的错误定义类
 */
object ErrorDefine {


    /*错误定义*/
    val RESPOND_SUCCESS = 0    //没空间了
    val RESPOND_ERROR_FILE_DIR = -1    //无效的文件系统路径
    val RESPOND_ERROR_STORAGE_SIZE = -2    //没空间了
    val RESPOND_ERROR_FILE_OPS = -3    //服务端文件操作错误
    val RESPOND_ERROR_MKDIR_EXIST = -4    //文件夹已存在
    val RESPOND_ERROR_DELETE_UNEXIST = -5    //要删除的文件不存在
    val RESPOND_ERROR_FILE_EXIST = -6    //部分文件已存在
    val RESPOND_ERROR_COPY_SUBDIR = -7    //目标文件是源文件的子目录
    val RESPOND_ERROR_DELETE_FILE = -8    //删除文件失败
    val RESPOND_ERROR_UPLOAD = -9    //上传文件失败
    val RESPOND_ERROR_MOVE = -10    //复制文件错误

    /*错误描述*/
    var comment: MutableMap<Int, String>


    init {
        comment = HashMap()
        comment[RESPOND_SUCCESS] = "成功"
        comment[RESPOND_ERROR_FILE_DIR] = "无效的文件路径"
        comment[RESPOND_ERROR_STORAGE_SIZE] = "存储空间不足"
        comment[RESPOND_ERROR_FILE_OPS] = "服务端文件操作错误"
        comment[RESPOND_ERROR_MKDIR_EXIST] = "文件夹已存在"
        comment[RESPOND_ERROR_DELETE_UNEXIST] = "文件不存在"
        comment[RESPOND_ERROR_FILE_EXIST] = "部分文件已存在"
        comment[RESPOND_ERROR_COPY_SUBDIR] = "目标文件是源文件的子目录"
        comment[RESPOND_ERROR_DELETE_FILE] = "删除文件失败"
        comment[RESPOND_ERROR_UPLOAD] = "上传文件失败"
        comment[RESPOND_ERROR_MOVE] = "复制文件错误"
    }
}
