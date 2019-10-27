package xingkong.loopicserver.picproc.tagImg.match;


import xingkong.loopicserver.picproc.tagImg.TagedFile;

import java.util.List;
import java.util.Random;


public class MatchBase {

    /**
     * 默认实现:使用完全随机数
     *
     * @param tagedFile
     * @param inputTag
     */
    public void computeMp(TagedFile tagedFile, List<String> inputTag) {
        /*默认方法，使用完全随机数*/
        float rand = new Random().nextFloat();
        tagedFile.mp = rand;   //使用随机数来shuffle
    }

}
