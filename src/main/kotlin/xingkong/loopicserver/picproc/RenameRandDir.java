package xingkong.loopicserver.picproc;


import xingkong.loopicserver.picproc.multiDir.SubDirImages;

import java.util.Scanner;

public class RenameRandDir {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("------------------------------------");
        in.useDelimiter("\n");
        String from = in.next();
        String to = in.next();
        SubDirImages sb = new SubDirImages();
        sb.copyRandImage(from, to, 30);

    }
}
