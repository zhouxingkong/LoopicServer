package xingkong.loopicserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LoopicServerApplication

fun main(args: Array<String>) {
    runApplication<LoopicServerApplication>(*args)
}
