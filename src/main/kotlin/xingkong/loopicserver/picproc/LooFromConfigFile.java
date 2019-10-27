package xingkong.loopicserver.picproc;


import xingkong.loopicserver.picproc.tagImg.ConfigFileManager;

import java.util.Scanner;

public class LooFromConfigFile {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ConfigFileManager cf = new ConfigFileManager();
        System.out.println("---------------------------------");
        String dir = in.next();
        cf.LooFromConfigFile(dir);

    }
}
