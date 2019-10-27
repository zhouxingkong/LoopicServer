package xingkong.loopicserver.picproc.tagImg;

import java.util.HashMap;

/*存储filter的结果*/
public class FilterResultStorage {
    HashMap<String, FilterResult> resultHash;

    public FilterResultStorage() {
        resultHash = new HashMap<String, FilterResult>();
    }

    public int searchResultNum(String key) {
        int ret = -1;
        if (resultHash.containsKey(key)) {
            FilterResult fr = resultHash.get(key);
            ret = fr.getRes();
        }
        return ret;
    }

    public FilterResult get(String key) {
        if (resultHash.containsKey(key)) {
            return resultHash.get(key);
        }
        return null;
    }

    public void put(String key, FilterResult value) {
        resultHash.put(key, value);
    }

}
