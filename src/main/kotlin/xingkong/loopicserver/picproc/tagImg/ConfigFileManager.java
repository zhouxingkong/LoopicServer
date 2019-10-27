package xingkong.loopicserver.picproc.tagImg;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*管理配置文件*/
public class ConfigFileManager {
    public static final String DIV = "-";
    public PicNameFilter picFilter;
    public ImageCpy imageCpy;
    public FilterResultStorage resultStorage;
    public String targetDir;
    public String exceptDir;
    BufferedWriter outputFileWriter;


    List<TagedFile> totalList;
    List<String> excludeTags;


    public ConfigFileManager() {
        picFilter = new PicNameFilter();
        imageCpy = new ImageCpy();
        resultStorage = new FilterResultStorage();
    }

    public void initOutputFile(String path) {
        try {
            File writeName = new File(path); // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖

            FileWriter writer = new FileWriter(writeName);
            outputFileWriter = new BufferedWriter(writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeOutputLine(OutputDesc outputDesc) {

        try {
            outputFileWriter.write(outputDesc + "\r\n"); // \r\n即为换行
            outputFileWriter.flush(); // 把缓存区内容压入文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成随机列表
     *
     * @param file_num
     * @return
     */
    public int[] genRandomList(int file_num) {
        if (file_num < 2) {
            return new int[]{0};
        }
        int[] indexs = new int[file_num];
        for (int j = 0; j < file_num; j++) {
            indexs[j] = j;    //给索引数组赋值
        }
        Random random = new Random();
        for (int j = 0; j < file_num; j++) {
            int temp = indexs[j];
            int newindx = random.nextInt(file_num - 1);
            indexs[j] = indexs[newindx];  //交换位置
            indexs[newindx] = temp;
        }

        return indexs;
    }

    public String makeKey(String[] tags) {
        String out = tags[0];
        for (int i = 1; i < tags.length; i++) {
            out = out + DIV + tags[i];
        }
        return out;
    }

    /**
     * 读取排除标签的配置文件
     *
     * @param dir
     * @throws IOException
     */
    public void loadExceptTags(String dir) throws IOException {
        excludeTags = new ArrayList<String>();
        FileReader fr = new FileReader(dir);
        BufferedReader bf = new BufferedReader(fr);
        String str;
        /*读取源文件路径*/
        while ((str = bf.readLine()) != null) {
            excludeTags.add(str);
        }

        System.out.println("排除配置个数:" + excludeTags.size());

    }

    /*解析一行配置文件*/
    public void parseOneLine(String line) {
        String[] nameSplitSpace = line.split(",");    //空格分开不同的部分
        if (nameSplitSpace.length < 2) return;
        int picNum = 1;   //总共图片数量

        OutputDesc outputDesc = new OutputDesc();

        /*配图*/
        String picKey = nameSplitSpace[0];     //storage的键
        String picName = "#";
        if (!picKey.equals("#")) {
            picName = processPicture(picKey, picNum);
        }
        if (picName == null || picName.length() < 1) {
            picName = "#";
        }
        outputDesc.setPicFileName(picName);

        /*配音频*/
        if (nameSplitSpace.length > 2) {
            String soundTag = nameSplitSpace[1];
            String soundMode = nameSplitSpace[2];
            outputDesc.setSoundTag(soundTag);
            outputDesc.setSoundMode(soundMode);
        }

        /*配文字*/
        if (nameSplitSpace.length > 3) {
            String text = nameSplitSpace[3];
            outputDesc.setText(text);
        }

        /*输出文件*/
        writeOutputLine(outputDesc);
    }

    /**
     * 处理一张图片的输出
     *
     * @param key
     * @param picNum
     */
    public String processPicture(String key, int picNum) {
        String[] tags = key.split(DIV);
        if (tags.length < 1) return "#";

        List<TagedFile> filteredList = null;
        int[] indexs;
        FilterResult fr = null;
        int storagedNum = 0;
        while (filteredList == null || filteredList.size() < picNum) {
            storagedNum = resultStorage.searchResultNum(key);   //搜索缓存
            if (storagedNum >= picNum) {
                fr = resultStorage.get(key);
                break;
            } else {
                /**/
                filteredList = picFilter.filter(totalList, tags, excludeTags, picNum);    //过滤
                /**/
                int fileNum = filteredList.size();
                if (fileNum < picNum && tags.length > 1) {
                    key = key.substring(0, key.lastIndexOf(DIV));
                    tags = key.split(DIV);
                } else {
                    indexs = genRandomList(fileNum);
                    fr = new FilterResult(filteredList, indexs);
                    resultStorage.put(key, fr);  //缓存结果
                    break;
                }

            }

            //todo ：shuffle

        }

        /*拷贝文件*/
        String dstName = imageCpy.copyRandImage(fr, targetDir + "/imgs", picNum);
        System.out.println("tag=" + key + "resultNum = " + fr.getFilteredList().size());
        return dstName;
    }

    public void parseConfigFile(String dir) {
        String name = dir + "/" + "loo.csv";
//        ArrayList<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(name);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            /*第一行:读取源文件路径*/
            if ((str = bf.readLine()) != null) {
                str = str.replaceAll(",", "");
                System.out.println("源文件:" + str);
                List<TagedFile> fileList = new ArrayList<TagedFile>();
                totalList = PicNameFilter.getFileList(fileList, "D:/mass/tag_dir");  //获取总共的文件列表
                System.out.println("源文件个数:" + totalList.size());
            }
            /*第二行:目标文件路径*/
            if ((str = bf.readLine()) != null) {
                targetDir = str.replaceAll(",", "");
                System.out.println("目标文件:" + targetDir);
                File picDir = new File(targetDir + "/imgs");
                if (!picDir.exists()) {
                    picDir.mkdirs();
                }
            }

            /*第三行:读取排除文件路径*/
            if ((str = bf.readLine()) != null) {
                exceptDir = str.replaceAll(",", "");
                loadExceptTags(exceptDir);
            }

            initOutputFile(targetDir + "/index.txt");

            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                if (str.endsWith(",")) {
                    str = str.substring(0, str.length() - 1);
                }
                System.out.println(str);
                parseOneLine(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LooFromConfigFile(String dir) {

        parseConfigFile(dir);
    }

}
