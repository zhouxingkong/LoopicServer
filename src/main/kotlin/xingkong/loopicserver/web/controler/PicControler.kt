package xingkong.loopicserver.web.controler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ZeroCopyHttpOutputMessage
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono
import xingkong.loopicserver.service.storage.StorageService

@Controller
class PicControler {
    @Autowired
    internal var storageService: StorageService? = null


    @GetMapping(value = ["/loopic"])
    fun hello(request: ServerHttpRequest, response: ServerHttpResponse): Mono<Void> {
//        var id= idrequest.getParameter("imgID")
        val dir = "/loopic"
        val filename = "0001.jpg"
        val resource = storageService!!.loadAsResource(dir, filename)


        val zeroCopyResponse = response as ZeroCopyHttpOutputMessage
        //response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
        response.getHeaders().contentType = MediaType.IMAGE_JPEG
        val file = resource.getFile()
        return zeroCopyResponse.writeWith(file, 0, file.length())
    }

}