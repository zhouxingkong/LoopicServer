package xingkong.loopicserver.module.utils

import java.util.*

object ListUtil {

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
}