package xingkong.loopicserver.picproc.tagImg


/**
 * 输出文本的一行配置
 */
class OutputDesc {
    internal var picFileName = "#"   //图片文件名
    internal var soundTag = "#"     //声音文件标签
    internal var soundMode = "#"     //音频播放模式
    internal var text = "#"      //文字描述

    override fun toString(): String {
        return "$picFileName $soundTag $soundMode $text"
    }

    fun getPicFileName(): String {
        return picFileName
    }

    fun setPicFileName(picFileName: String): OutputDesc {
        this.picFileName = picFileName
        return this
    }

    fun getSoundTag(): String {
        return soundTag
    }

    fun setSoundTag(soundTag: String): OutputDesc {
        this.soundTag = soundTag
        return this
    }

    fun getSoundMode(): String {
        return soundMode
    }

    fun setSoundMode(soundMode: String): OutputDesc {
        this.soundMode = soundMode
        return this
    }

    fun getText(): String {
        return text
    }

    fun setText(text: String): OutputDesc {
        this.text = text
        return this
    }
}
