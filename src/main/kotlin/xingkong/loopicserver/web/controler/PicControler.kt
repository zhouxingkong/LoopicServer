package xingkong.loopicserver.web.controler

import ch.qos.logback.core.util.FileUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.http.MediaType
import org.springframework.http.ZeroCopyHttpOutputMessage
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Mono
import xingkong.loopicserver.module.ConfigFileManager
import xingkong.loopicserver.service.storage.StorageService
import java.io.File

@Controller
class PicControler : ApplicationRunner {

    @Autowired
    internal var storageService: StorageService? = null

    internal var tagMap: MutableMap<String, String> = mutableMapOf()


    /**
     * Callback used to run the bean.
     * @param args incoming application arguments
     * @throws Exception on error
     */
    override fun run(args: ApplicationArguments?) {
        ConfigFileManager.systemInit("D:/mass/config")
    }


    @GetMapping(value = ["/pic/{story}/{scene}/{index}"])
    fun showPic(request: ServerHttpRequest, response: ServerHttpResponse,
                @PathVariable(value = "story") story: String,
                @PathVariable(value = "scene") scene: String,
                @PathVariable(value = "index") index: String
    ): Mono<Void>? {
        val path = ConfigFileManager.getPic(story.toInt(),scene.toInt(),index.toInt())
        if (path.startsWith("error")) {
            println(path)
            return null
        }

        val fileIndex = path.lastIndexOf("\\")
        val dir = path.substring(0, fileIndex)
        val filename = path.substring(fileIndex + 1)
        val resource = storageService!!.loadAsResource(dir, filename)

        val zeroCopyResponse = response as ZeroCopyHttpOutputMessage
        //response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
        response.getHeaders().contentType = MediaType.IMAGE_JPEG
        val file = resource.file
        return zeroCopyResponse.writeWith(file, 0, file.length())

    }

    @GetMapping(value = ["/rmpic/{story}/{scene}/{index}"])
    @ResponseBody
    fun rmPic(request: ServerHttpRequest, response: ServerHttpResponse,
                @PathVariable(value = "story") story: String,
                @PathVariable(value = "scene") scene: String,
                @PathVariable(value = "index") index: String
    ): Mono<String>? {
        val path = ConfigFileManager.getPic(story.toInt(),scene.toInt(),index.toInt())
        if (path.startsWith("error")) {
            println(path)
            return null
        }
        val sourceFile = File(path)
        if(!sourceFile.exists()) return Mono.just("file not find")
        val fileIndex = path.lastIndexOf("\\")
        val dir = path.substring(0, fileIndex)
        val filename = path.substring(fileIndex + 1)

        val dstDir = File("${dir}${File.separator}rmpic")
        if(!dstDir.exists()){
            dstDir.mkdir()
        }
        val dstFile = File("${dstDir}${File.separator}${filename}")
        try {
            val ret = sourceFile.renameTo(dstFile)
        }catch (e:Throwable){
            return Mono.just("${e.message}")
        }

        return Mono.just("success")
    }

    @GetMapping(value = ["/story/list"])
    @ResponseBody
    fun getStoryList(): Mono<List<String>> {
        return Mono.just(ConfigFileManager.getStoryList())
    }

    @GetMapping(value = ["/story/scene/all"])
    @ResponseBody
    fun getAllSceneList(): Mono<List<List<String>>> {
        return Mono.just(ConfigFileManager.getAllSceneList())
    }

    @GetMapping(value = ["/scene/list/{story}"])
    @ResponseBody
    fun getSceneList(@PathVariable(value = "story") story: String): Mono<List<String>> {
        return Mono.just(ConfigFileManager.getSceneList(story.toInt()))
    }

    @GetMapping(value = ["/text/{story}"])
    @ResponseBody
    fun getTextList(@PathVariable(value = "story") story: String): Mono<List<String>> {
        return Mono.just(ConfigFileManager.getTextList(story.toInt()))
    }

    @GetMapping(value = ["/alltext/"])
    @ResponseBody
    fun getAllTextList(): Mono<List<List<String>>> {
        return Mono.just(ConfigFileManager.getAllTextList())
    }

    @PostMapping(value = ["/clearcache"])
    @ResponseBody
    fun clearCache(): Mono<String> {
        ConfigFileManager.clear()
        println("清空缓存")
        return Mono.just("ok")
    }

}