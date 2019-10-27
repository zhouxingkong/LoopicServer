package xingkong.loopicserver.picproc.tagImg;


/**
 * 输出文本的一行配置
 */
public class OutputDesc {
    String picFileName = "#";   //图片文件名
    String soundTag = "#";     //声音文件标签
    String soundMode = "#";     //音频播放模式
    String text = "#";      //文字描述

    @Override
    public String toString() {
        String ret = picFileName + " " + soundTag + " " + soundMode + " " + text;
        return ret;
    }

    public String getPicFileName() {
        return picFileName;
    }

    public OutputDesc setPicFileName(String picFileName) {
        this.picFileName = picFileName;
        return this;
    }

    public String getSoundTag() {
        return soundTag;
    }

    public OutputDesc setSoundTag(String soundTag) {
        this.soundTag = soundTag;
        return this;
    }

    public String getSoundMode() {
        return soundMode;
    }

    public OutputDesc setSoundMode(String soundMode) {
        this.soundMode = soundMode;
        return this;
    }

    public String getText() {
        return text;
    }

    public OutputDesc setText(String text) {
        this.text = text;
        return this;
    }
}
