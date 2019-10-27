package xingkong.loopicserver.picproc;


import xingkong.loopicserver.picproc.multiDir.SingleDirRename;

import java.util.Scanner;

public class RenameRand {
    public RenameRand() {

    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        SingleDirRename r = new SingleDirRename();

        String m = in.next();
        String pre = in.next();
        int offset = in.nextInt();
        System.out.println("Begin!");
        r.getFileList(m);
        r.renameRand(pre, offset);
        System.out.println("Success!!");

    }

}
