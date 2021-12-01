package xingkong.loopicserver.web.controler

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
        ConfigFileManager.systemInit("/Users/xingkong/loo/config/use")
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

    @GetMapping(value = ["/story/list"])
    @ResponseBody
    fun getStoryList(): Mono<List<String>> {
//        println("获取文本")
        return Mono.just(ConfigFileManager.getStoryList())
    }

    @GetMapping(value = ["/scene/list/{story}"])
    @ResponseBody
    fun getSceneList(@PathVariable(value = "story") story: String): Mono<List<String>> {
//        println("获取文本")
        return Mono.just(ConfigFileManager.getSceneList(story.toInt()))
    }

    @GetMapping(value = ["/text/{story}"])
    @ResponseBody
    fun getTextList(@PathVariable(value = "story") story: String): Mono<List<String>> {
//        println("获取文本")
        return Mono.just(ConfigFileManager.getTextList(story.toInt()))
    }

//    /**
//     * 清空服务端所有缓存
//     */
//    @PostMapping(value = ["/erasecache"])
//    @ResponseBody
//    fun eraseCache(): Mono<String> {
//        tagMap.clear()
////        println("清空缓存")
//        return Mono.just("ok")
//    }

//    @GetMapping(value = ["/loopicserver/{cmd}/{num}", "/loopicserver/{cmd}/{num}/{ser}"])
//    fun getPicWithServerConfig(request: ServerHttpRequest, response: ServerHttpResponse,
//                               @PathVariable(value = "cmd") cmd: String,
//                               @PathVariable(value = "num") num: String): Mono<Void>? {
//        if ("change".equals(cmd)) {  //更换图片
//            tagMap.remove(num);
//        }
//        if (!tagMap.containsKey(num)) {    //已经获取过了
//            val path = configFileManager.pickOneWithServerConfig(num.toInt())
//            if (path.startsWith("error")) {
//                println(path)
//                return null
//            }
//            tagMap[num] = path
//        }
//        val filePath = tagMap.get(num);
////        println(filePath)
//
//        //加载文件resource
//        val index = filePath!!.lastIndexOf("\\")
//        val dir = filePath.substring(0, index)
//        val filename = filePath.substring(index + 1)
//        val resource = storageService!!.loadAsResource(dir, filename)
//
//        val zeroCopyResponse = response as ZeroCopyHttpOutputMessage
//        //response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
//        response.getHeaders().contentType = MediaType.IMAGE_JPEG
//        val file = resource.file
//        return zeroCopyResponse.writeWith(file, 0, file.length())
//    }

//    @GetMapping(value = ["/loopic/{cmd}/{num}/{pictag}"])
//    fun getPic(request: ServerHttpRequest, response: ServerHttpResponse,
//               @PathVariable(value = "cmd") cmd: String,
//               @PathVariable(value = "num") num: String, @PathVariable(value = "pictag") tag: String): Mono<Void>? {
////        println("$num,$tag")
//
//        if ("change".equals(cmd)) {  //更换图片
//            tagMap.remove(num);
//        }
//        if (!tagMap.containsKey(num)) {    //已经获取过了
//            val path = configFileManager.pickOne(tag)
//            if (path.startsWith("error")) {
//                println(path)
//                return null
//            }
//            tagMap[num] = path
//        }
//        val filePath = tagMap.get(num);
////        println(filePath)
//
//        //加载文件resource
//        val index = filePath!!.lastIndexOf("\\")
//        val dir = filePath.substring(0, index)
//        val filename = filePath.substring(index + 1)
//        val resource = storageService!!.loadAsResource(dir, filename)
//
//        val zeroCopyResponse = response as ZeroCopyHttpOutputMessage
//        //response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
//        response.getHeaders().contentType = MediaType.IMAGE_JPEG
//        val file = resource.getFile()
//        return zeroCopyResponse.writeWith(file, 0, file.length())
//    }

}