package xingkong.loopicserver.module.bean

/**
 * 一个场景
 */
class SceneInfo {
    var rawTag = ""
    var picTag = mutableListOf<String>()
    var soundTag = ""
    var text = ""

    //匹配到的
    var picPath = listOf<String>()
}