package xingkong.loopicserver.picproc.tagImg.match;


import xingkong.loopicserver.picproc.tagImg.TagedFile;

import java.util.List;
import java.util.Random;

public class MatchFloorProperSubnorm extends MatchBase {

    public static final float PROPOR_STEP = 3.0f;

    /**
     * 计算匹配分的函数；使用先验标签匹配法
     *
     * @param tagedFile
     * @param inputTag
     */
    @Override
    public void computeMp(TagedFile tagedFile, List<String> inputTag) {
        int inputTagSize = inputTag.size();
        float tagDis = 1.0f;
        for (int i = tagedFile.getTags().size() - 1; i > 0; i--) {
            String tag = tagedFile.getTags().get(i);
            if (inputTag.contains(tag)) {
                if (i < tagedFile.getTags().size() - 1) {
                    tagDis += PROPOR_STEP;
                }
                tagDis += PROPOR_STEP * (i - inputTagSize);
                break;
            }
        }

        float rand = new Random().nextFloat() * tagDis;
        tagedFile.setMp(rand);   //使用随机数来shuffle
    }
}
