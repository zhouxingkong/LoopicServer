package xingkong.loopicserver.service.storage

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

/**
 * 文件系统服务
 */
@Service
class FileSystemStorageService
/**
 * 获取存储路径
 *
 * @param properties
 */
@Autowired
constructor(properties: StorageProperties) : StorageService {

    private val rootLocation: Path

    private val rootLocationString: String
    internal var logger = LoggerFactory.getLogger(FileSystemStorageService::class.java)


    init {
        this.rootLocationString = properties.location
        this.rootLocation = Paths.get(rootLocationString)
    }

    private fun countSize(fileArr: Array<File>?, size: Int): Int {
        var size = size
        if (null == fileArr || fileArr.size <= 0) {//部分文件夹 无权限访问，返回null

            return size
        }
        for (file in fileArr) {
            if (file.isFile) {
                size += file.length().toInt()
            }
            if (file.isDirectory) {
                countSize(file.listFiles(), size)
            }
        }
        return size
    }

    /**
     * 创建根目录()
     *
     * @param username
     */
    override fun createRootDir(username: String) {
        val path = "$rootLocationString/$username"
        val f = File(path)
        if (!f.exists() || !f.isDirectory) {
            f.mkdirs()
        }
    }

    /**
     * 保存文件
     *
     * @param filePart
     * @param subDir
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun store(filePart: FilePart, subDir: String) {
        val filename = rootLocationString + subDir + "/" + filePart.filename()
        val file = File(filename)
        /*是否覆盖*/
        if (file.exists()) {
            return
        }

        filePart.transferTo(file)

        /*下面这坨烂屎代码不能用*/
        //        Path path = file.toPath();
        //        Path tempFile = Files.createFile(path);
        //        AsynchronousFileChannel channel =
        //                AsynchronousFileChannel.open(tempFile, StandardOpenOption.WRITE);
        //        DataBufferUtils.write(filePart.content(), channel, 0).subscribe();
    }

    /**
     * 读取路径下的所有文件
     *
     * @return
     */
    override fun loadAll(subDir: String, maxDepth: Int): Stream<Path> {
        val currentPath = Paths.get(rootLocationString + subDir)
        try {
            return Files.walk(currentPath, maxDepth)
                    .filter { path -> path != currentPath }
            //                    .map(this.rootLocation::relativize);    //获取相对于root的子路径
        } catch (e: IOException) {
            throw StorageException("Failed to read stored files", e)
        }

    }

    override fun load(subDir: String, filename: String): Path {
        val currentPath = Paths.get(rootLocationString + subDir)
        return currentPath.resolve(filename)
    }

    override fun loadAsResource(subDir: String, filename: String): Resource {
        try {
            val file = load(subDir, filename)
            val resource = UrlResource(file.toUri())
            return if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException(
                        "Could not read file: $filename")

            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not read file: $filename", e)
        }

    }

    /**
     * 初始化:创建根路径
     */
    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }

    }


}
