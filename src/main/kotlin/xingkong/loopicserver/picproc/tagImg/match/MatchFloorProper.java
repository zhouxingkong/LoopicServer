package xingkong.loopicserver.picproc.tagImg.match;


import xingkong.loopicserver.picproc.tagImg.TagedFile;

import java.util.List;
import java.util.Random;

/**
 * 每一层给赋予一定的概率
 */
public class MatchFloorProper extends MatchBase {

    public static final float PROPOR_STEP = 100.0f;

    /**
     * 计算匹配分的函数；使用先验标签匹配法
     *
     * @param tagedFile
     * @param inputTag
     */
    @Override
    public void computeMp(TagedFile tagedFile, List<String> inputTag) {
        int floorDis = tagedFile.tags.size() - inputTag.size();
        float rand = new Random().nextFloat() * (PROPOR_STEP * (floorDis - 1) + 1.0f);
        tagedFile.mp = rand;   //使用随机数来shuffle
    }
}
