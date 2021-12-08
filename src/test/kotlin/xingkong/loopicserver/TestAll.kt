package xingkong.loopicserver

import org.junit.jupiter.api.Test

class TestAll {

    @Test
    fun testSubstring() {
        var str: String = "abcde"
        str = str.substring(1)
        print(str)
    }

    @Test
    fun testSet() {
        val key = setOf<String>("佐伊","美少女")
        val map = mapOf<Set<String>,String>(key to "aaa")
        val g = setOf<String>("佐伊","美少女")
        println("${map[g]}")
    }
}