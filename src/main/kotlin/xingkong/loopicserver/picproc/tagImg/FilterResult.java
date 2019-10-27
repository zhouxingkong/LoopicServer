package xingkong.loopicserver.picproc.tagImg;

import java.util.List;

public class FilterResult {
    List<TagedFile> filteredList;
    int[] randomIndexs;
    int consumeIndex = 0;

    public FilterResult(List<TagedFile> filteredList, int[] randomIndexs) {
        this.filteredList = filteredList;
        this.randomIndexs = randomIndexs;
    }

    public List<TagedFile> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<TagedFile> filteredList) {
        this.filteredList = filteredList;
    }

    public int[] getRandomIndexs() {
        return randomIndexs;
    }

    public void setRandomIndexs(int[] randomIndexs) {
        this.randomIndexs = randomIndexs;
    }

    public int getRes() {
        return filteredList.size() - consumeIndex;
    }

    public int getConsumeIndex() {
        return consumeIndex;
    }

    public void setConsumeIndex(int consumeIndex) {
        this.consumeIndex = consumeIndex;
    }
}
