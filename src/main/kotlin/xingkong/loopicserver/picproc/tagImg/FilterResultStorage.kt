package xingkong.loopicserver.picproc.tagImg

import java.util.*

/*存储filter的结果*/
class FilterResultStorage {
    internal var resultHash: HashMap<String, FilterResult>

    init {
        resultHash = HashMap()
    }

    fun searchResultNum(key: String): Int {
        var ret = -1
        if (resultHash.containsKey(key)) {
            val fr = resultHash[key]
            ret = fr?.res!!
        }
        return ret
    }

    operator fun get(key: String): FilterResult? {
        return if (resultHash.containsKey(key)) {
            resultHash[key]
        } else null
    }

    fun put(key: String, value: FilterResult) {
        resultHash[key] = value
    }

}
