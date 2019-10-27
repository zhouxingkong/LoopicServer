package xingkong.loopicserver.picproc;


import xingkong.loopicserver.picproc.tagImg.TagManager;

import java.util.Scanner;

public class ClearTag {
    public static void main(String[] args) {
//        String[] nameSplitDot = "asjgno.ddd".split(".");
//        System.out.println(nameSplitDot.length+":"+ nameSplitDot[0]);
        Scanner in = new Scanner(System.in);
        TagManager tm = new TagManager();
//        in.useDelimiter("\n");
        while (true) {
            System.out.println("---------------------------------");
            String dir = in.next();
            tm.clearTag(dir);
        }
    }
}
