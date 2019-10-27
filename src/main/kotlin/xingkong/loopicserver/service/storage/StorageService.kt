package xingkong.loopicserver.service.storage

import org.springframework.core.io.Resource
import org.springframework.http.codec.multipart.FilePart

import java.io.IOException
import java.nio.file.Path
import java.util.stream.Stream

interface StorageService {

    fun init()

    fun createRootDir(username: String)
    @Throws(IOException::class)
    fun store(file: FilePart, subDir: String)

    fun loadAll(subDir: String, maxDepth: Int): Stream<Path>

    fun load(subDir: String, filename: String): Path

    fun loadAsResource(subDir: String, filename: String): Resource


}
