package xingkong.loopicserver.picproc;


import xingkong.loopicserver.picproc.tagImg.TagManager;

import java.util.Scanner;

public class AddTagByDir {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        TagManager tm = new TagManager();
        System.out.println("---------------------------------");
        String dir = in.next();
        tm.addTagByDir(dir);

    }
}
