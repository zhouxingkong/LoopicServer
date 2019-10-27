package xingkong.loopicserver.picproc.multiDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**/
public class SubDirImages {

    public int img_index = 0;
    public int file_num = 0;
    int[] indexs;
    String dir;
    String[] files;
    List imglist;


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

    //后缀
    public static String getSuffix(String filename) {
        int dix = filename.lastIndexOf('.');
        if (dix < 0) {
            return "";
        } else {
            return filename.substring(dix + 1);
        }
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

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        System.out.println(oldPath + "-->" + newPath);
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    public void copyRandImage(String fromDir, String toDir, int num) {
        List<File> filelist = SubDirImages.getFileList(new ArrayList<File>(), fromDir);
        file_num = filelist.size();
        if (num > file_num) num = file_num;
        genRandomList();
        File f;
        String fromPath = "";
        String toPath = "";

        String letters = SingleDirRename.getNameRoot(8);
        String numbers = "";
        String suf = "";
        System.out.println(num);
        for (int i = 0; i < num; i++) {
            f = filelist.get(indexs[i]);
            fromPath = f.getAbsolutePath();

            numbers = "" + i;
            while (numbers.length() < 6) {
                numbers = "0" + numbers;
            }
            suf = getSuffix(f.getName());

            toPath = toDir + "/" + letters + numbers + "." + suf;
            copyFile(fromPath, toPath);
        }

    }
}
