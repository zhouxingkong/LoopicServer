package xingkong.loopicserver.picproc.tagImg.match;


import xingkong.loopicserver.picproc.tagImg.TagedFile;

import java.util.List;
import java.util.Random;

/**
 * 这种匹配方法给根路径赋予更大的权重，
 */
public class MatchDirFirst extends MatchBase {
    public static final float LAMDA = 4.0f;

    /**
     * 计算匹配分的函数；使用先验标签匹配法
     *
     * @param tagedFile
     * @param inputTag
     */
    @Override
    public void computeMp(TagedFile tagedFile, List<String> inputTag) {
        tagedFile.mp = 0;
        /*加入随机事件，避免结果过于单调*/
        float rand = -LAMDA + new Random().nextFloat() * 2 * LAMDA;
        for (String t : tagedFile.tags) {
            tagedFile.mp *= 2;
            if (!inputTag.contains(t)) {
                tagedFile.mp += 1.0;
            }
        }
        tagedFile.mp += rand;   //使用随机数来shuffle
    }
}
