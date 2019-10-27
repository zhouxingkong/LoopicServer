package xingkong.loopicserver.service.storage

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("storage")
class StorageProperties {

    internal var logger = LoggerFactory.getLogger(StorageProperties::class.java)

    /**
     * Folder location for storing files
     */
    var location = "upload-dir"
        get() {
            logger.info("文件路径$field")
            return field
        }

}
