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
import xingkong.loopicserver.picproc.tagImg.ConfigFileManager
import xingkong.loopicserver.service.storage.StorageService

@Controller
class PicControler : ApplicationRunner {


    @Autowired
    internal var storageService: StorageService? = null

    internal var tagMap: MutableMap<String, String> = mutableMapOf()
    internal var configFileManager: ConfigFileManager = ConfigFileManager()


    /**
     * Callback used to run the bean.
     * @param args incoming application arguments
     * @throws Exception on error
     */
    override fun run(args: ApplicationArguments?) {
        configFileManager.systemInit("D:/mass/config/use")
    }

    @GetMapping(value = ["/loopic/{cmd}/{num}/{pictag}"])
    fun getPic(request: ServerHttpRequest, response: ServerHttpResponse,
               @PathVariable(value = "cmd") cmd: String,
               @PathVariable(value = "num") num: String, @PathVariable(value = "pictag") tag: String): Mono<Void> {
        println("$num,$tag")

        if ("change".equals(cmd)) {  //更换图片
            tagMap.remove(num);
        }
        if (!tagMap.containsKey(num)) {    //已经获取过了
            var path = configFileManager.pickOne(tag)
            tagMap[num] = path
        }
        val filePath = tagMap.get(num);
        println(filePath)

        //加载文件resource
        val index = filePath!!.lastIndexOf("\\")
        val dir = filePath.substring(0, index)
        val filename = filePath.substring(index + 1)
        val resource = storageService!!.loadAsResource(dir, filename)

        val zeroCopyResponse = response as ZeroCopyHttpOutputMessage
        //response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
        response.getHeaders().contentType = MediaType.IMAGE_JPEG
        val file = resource.getFile()
        return zeroCopyResponse.writeWith(file, 0, file.length())
    }

    @GetMapping(value = ["/loopicserver/{cmd}/{num}"])
    fun getPicWithServerConfig(request: ServerHttpRequest, response: ServerHttpResponse,
                               @PathVariable(value = "cmd") cmd: String,
                               @PathVariable(value = "num") num: String): Mono<Void> {
        if ("change".equals(cmd)) {  //更换图片
            tagMap.remove(num);
        }
        if (!tagMap.containsKey(num)) {    //已经获取过了
            val path = configFileManager.pickOneWithServerConfig(num.toInt())
            tagMap[num] = path
        }
        val filePath = tagMap.get(num);
        println(filePath)

        //加载文件resource
        val index = filePath!!.lastIndexOf("\\")
        val dir = filePath.substring(0, index)
        val filename = filePath.substring(index + 1)
        val resource = storageService!!.loadAsResource(dir, filename)

        val zeroCopyResponse = response as ZeroCopyHttpOutputMessage
        //response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
        response.getHeaders().contentType = MediaType.IMAGE_JPEG
        val file = resource.file
        return zeroCopyResponse.writeWith(file, 0, file.length())
    }

    @GetMapping(value = ["/text/xingkong1313113"])
    @ResponseBody
    fun getText(): Mono<MutableList<String>> {
        println("获取文本")
        return Mono.just(configFileManager.getTextList())
    }

    @PostMapping(value = ["/changepic/{num}"])
    @ResponseBody
    fun changePic(@PathVariable(value = "num") num: String): Mono<String> {
        tagMap.remove(num);
        println("换图$num")
        return Mono.just("ok")
    }

    /**
     * 清空服务端所有缓存
     */
    @PostMapping(value = ["/erasecache"])
    @ResponseBody
    fun eraseCache(): Mono<String> {
        tagMap.clear()
        println("清空缓存")
        return Mono.just("ok")
    }

}