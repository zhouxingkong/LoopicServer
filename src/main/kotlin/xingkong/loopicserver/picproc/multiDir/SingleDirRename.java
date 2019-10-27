package xingkong.loopicserver.picproc.multiDir;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SingleDirRename {
    public int img_index = 0;
    public int file_num = 0;
    String dir;
    File[] files;
    List imglist;

    int[] indexs;


    public static String getNameRoot(int l) {
        String str = "";
        for (int i = 0; i < l; i++) {
            str = str + (char) (Math.random() * 26 + 'a');
        }
        return str;
    }

    public static List<File> getFileList(List<File> filelist, String strPath) {

        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
//                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(filelist, files[i].getAbsolutePath()); // 获取文件绝对路径
                } else {
//                    String strFileName = files[i].getAbsolutePath();
//                    System.out.println("---" + strFileName);
                    filelist.add(files[i]);
                }
            }

        }
        return filelist;
    }

    public void getFileList(String base) {
        dir = base/*+lists[file_index]+"/"*/ + File.separator;
        File file = new File(dir);
        files = file.listFiles();
        file_num = files.length;
        ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(files));
        imglist = fileList.stream().map((f) -> f.getAbsolutePath().replace("\\", "/")).collect(Collectors.toList());

//        imglist = java.util.Arrays.asList(files);
    }

    public void getFileListByDir(String base) {
        dir = base/*+lists[file_index]+"/"*/ + File.separator;
        List<File> fileList = getFileList(new ArrayList<File>(), dir);


//        File file = new File(dir);
//        files = file.listFiles();
        file_num = fileList.size();
        imglist = fileList.stream().map((f) -> f.getAbsolutePath().replace("\\", "/")).collect(Collectors.toList());
    }

    public void genLinearList() {
        indexs = new int[file_num];
        for (int j = 0; j < file_num; j++) {
            indexs[j] = j;    //给索引数组赋值
        }
    }

    public void genRandomList() {
        genLinearList();
        Random random = new Random();
        for (int j = 0; j < file_num; j++) {
            int temp = indexs[j];
            int newindx = random.nextInt(file_num - 1);
            indexs[j] = indexs[newindx];  //交换位置
            indexs[newindx] = temp;
        }
    }

    public void doRename(String pre, int offset) {
        for (int i = 0; i < file_num; i++) {

            String path = /*dir + */"" + imglist.get(img_index);

            String[] split_by_dir = path.split("/");
            String last_part = "";
            last_part = split_by_dir[split_by_dir.length - 1];

            String[] split_by_dot = last_part.split("\\.");

            String suffix = split_by_dot[split_by_dot.length - 1];
            String numStr = "" + (indexs[i] + offset);
            for (int j = 0; j < 6; j++) {
                if (numStr.length() < 6) {
                    numStr = "0" + numStr;
                }
            }
            String outputDir = "";
            for (int j = 0; j < split_by_dir.length - 1; j++) {
                outputDir += split_by_dir[j] + "/";
            }
            outputDir += "/";
            outputDir += pre;
            outputDir += numStr;
            outputDir += ".";
            outputDir += suffix;
//            System.out.println(outputDir);
            //重命名文件
            File file = new File(path);
            boolean ret = file.renameTo(new File(outputDir));
            if (!ret) {
                System.out.println("Error!!" + path + "   ->    " + outputDir);
            } else {
                System.out.println("success!!" + path + "   ->    " + outputDir);
            }

            img_index++;
        }
    }

    public void renameLinear(String pre, int offset) {
        genLinearList();
        doRename(pre, offset);

    }

    public void renameRand(String pre, int offset) {
        genRandomList();
        doRename(pre, offset);

    }

}
