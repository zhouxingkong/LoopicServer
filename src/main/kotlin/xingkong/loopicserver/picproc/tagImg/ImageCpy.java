package xingkong.loopicserver.picproc.tagImg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageCpy {
    //    public int file_num = 0;
//    int[] indexs;
    int currIndex = 0;
    String letters = getNameRoot(8);

    Map<String, String> duplicate = new HashMap<String, String>();

    //后缀
    public static String getSuffix(String filename) {
        int dix = filename.lastIndexOf('.');
        if (dix < 0) {
            return "";
        } else {
            return filename.substring(dix + 1);
        }
    }

    public static String getNameRoot(int l) {
        String str = "";
        for (int i = 0; i < l; i++) {
            str = str + (char) (Math.random() * 26 + 'a');
        }
        return str;
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
//        System.out.println(oldPath + "-->" + newPath);
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
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * @param fr
     * @param toDir
     * @param num
     */
    public String copyRandImage(FilterResult fr, String toDir, int num) {
        List<TagedFile> filelist = fr.getFilteredList();
//        int[] indexs = fr.getRandomIndexs();
        int numLimit = fr.getRes();
        int offset = fr.getConsumeIndex();

        if (num > numLimit) num = numLimit;
        TagedFile f;
        String fromPath = "";
        String toPath = "";

        String numbers = "";
        String suf = "";
        String toFileName = "";
        for (int i = 0; i < num; i++) {
//            f = filelist.get(indexs[i + offset]);
            f = filelist.get(i + offset);   //由于已经进行了随机数shuffle操作，所以不需要用随机数索引
            fromPath = f.getAbsolutePath();

            String fileName = f.getName();
            /*防止图片重复*/
            if (i + offset + 1 < filelist.size() && duplicate.containsKey(fileName)) {
                offset++;
                i--;
                continue;
            }

            numbers = "" + currIndex;
            currIndex++;
            while (numbers.length() < 6) {
                numbers = "0" + numbers;
            }
            suf = getSuffix(f.getName());
            toFileName = letters + numbers + "." + suf;
            toPath = toDir + "/" + toFileName;
            copyFile(fromPath, toPath);

            duplicate.put(fileName, "have");
        }
        fr.setConsumeIndex(offset + num);

        return toFileName;
    }
}
