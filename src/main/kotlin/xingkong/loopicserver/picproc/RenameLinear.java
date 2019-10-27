package xingkong.loopicserver.picproc;


import xingkong.loopicserver.picproc.multiDir.SingleDirRename;

import java.util.Scanner;

public class RenameLinear {
    public RenameLinear() {

    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        SingleDirRename r = new SingleDirRename();
        while (true) {
            r.img_index = 0;
            r.file_num = 0;
            System.out.println("------------------------------------");
            in.useDelimiter("\n");
            String m = in.next();
            String pre = r.getNameRoot(6);
//            String pre = "fengjing";
//            int offset = in.nextInt();
            int offset = 0;
            System.out.println("Begin!");
            r.getFileListByDir(m);
            r.renameLinear(pre, offset);
            System.out.println("Success!!");
        }
    }
}
