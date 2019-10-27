package xingkong.loopicserver.picproc.tagImg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*图片标签管理*/
public class TagManager {
    public static final String DIV = "-";
    public static final String ROOT_TAG = "tag_dir";
    public int img_index = 0;
    public int file_num = 0;
    String dir;
    String[] files;
    List imglist;
    int[] indexs;

    public void getFileList(String base) {
        dir = base/*+lists[file_index]+"/"*/ + "/";
        File file = new File(dir);
        files = file.list();
        imglist = new ArrayList();
        file_num = 0;
        File[] files = file.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) { // 判断是文件还是文件夹

                } else {
                    file_num++;
                    imglist.add(files[i].getName());
                }
            }

        }
//        imglist = java.util.Arrays.asList(files);
    }


    public String addTagToName(String name, String tag) {
        if (name == null || name.length() < 2) return "";
        /**/
        String[] nameSplitDot = name.split("\\.");
        if (nameSplitDot.length < 1) return "";
        String suffix = nameSplitDot[nameSplitDot.length - 1];    //文件後綴

        String[] nameSplitLine = nameSplitDot[0].split(DIV);
        if (nameSplitLine.length < 1) return "";

        for (String s : nameSplitLine) {
            if (tag.equals(s)) {
                return nameSplitDot[0] + "." + suffix;
            }
//            System.out.println(s+":"+tag);
        }
        return nameSplitDot[0] + DIV + tag + "." + suffix;

    }

    public String removeTagFromName(String name, String tag) {
        if (name == null || name.length() < 2) return "";
        /**/
        String[] nameSplitDot = name.split("\\.");
        if (nameSplitDot.length < 1) return "";
        String suffix = nameSplitDot[nameSplitDot.length - 1];    //文件後綴

        String[] nameSplitLine = nameSplitDot[0].split(DIV);
        if (nameSplitLine.length < 1) return "";

        String outputName = nameSplitLine[0];
        for (int i = 1; i < nameSplitLine.length; i++) {
            if (!tag.equals(nameSplitLine[i])) {
                outputName = outputName + DIV + nameSplitLine[i];
            }
//            System.out.println(s+":"+tag);
        }
        return outputName + "." + suffix;

    }

    public String clearTagFromName(String name) {
        if (name == null || name.length() < 1) return "";
        /**/
        String[] nameSplitDot = name.split("\\.");
        if (nameSplitDot.length < 1) return "";
        String suffix = nameSplitDot[nameSplitDot.length - 1];    //文件後綴

        String[] nameSplitLine = nameSplitDot[0].split(DIV);
        if (nameSplitLine.length < 1) return "";

        String outputName = nameSplitLine[0];
        return outputName + "." + suffix;
    }


    public void renameFile(String fromPath, String toPath) {
        File file = new File(fromPath);
        boolean ret = file.renameTo(new File(toPath));
        if (!ret) {
            System.out.println("Error!!" + fromPath + "   ->    " + toPath);
        }
    }

    public void doAddTag(String dir, String tag) {
        img_index = 0;
        for (int i = 0; i < file_num; i++) {

            String oldName = "" + imglist.get(img_index);
            String newName = addTagToName(oldName, tag);

            //重命名文件
            renameFile(dir + oldName, dir + newName);

            img_index++;
        }
    }

    public void doRemoveTag(String dir, String tag) {
        img_index = 0;
        for (int i = 0; i < file_num; i++) {

            String oldName = "" + imglist.get(img_index);
            String newName = removeTagFromName(oldName, tag);

            //重命名文件
            renameFile(dir + oldName, dir + newName);

            img_index++;
        }
    }

    public void doClearTag(String dir) {
        img_index = 0;
        for (int i = 0; i < file_num; i++) {
            String oldName = "" + imglist.get(img_index);
            String newName = clearTagFromName(oldName);
            //重命名文件
            renameFile(dir + oldName, dir + newName);

            img_index++;
        }
    }

    public void addTag(String dir, String tag) {
        getFileList(dir);
        doAddTag(dir + "/", tag);
    }

    public void removeTag(String dir, String tag) {
        getFileList(dir);
        doRemoveTag(dir + "/", tag);
    }

    public void clearTag(String dir) {
        getFileList(dir);
        doClearTag(dir + "/");
    }

    /**
     * 自动添加一些标签
     *
     * @param tags
     */
    public void autoAddTag(List<String> tags) {
        if (!tags.contains("黑夜")) {
            tags.add("白天");
        }
    }

    public void doAddTagByDir(List<String> tags, String strPath) {

        File dir = new File(strPath);
        String fileName = dir.getName();
        String dirStr = dir.getAbsolutePath();
        tags.add(fileName);
        //autoAddTag(tags);
        clearTag(dirStr);
        for (String t : tags) {
            if (!ROOT_TAG.equals(t)) addTag(dirStr, t);
        }
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    doAddTagByDir(tags, files[i].getAbsolutePath()); // 获取文件绝对路径
                }

            }

        }
        tags.remove(tags.size() - 1);
    }

    public void addTagByDir(String dir) {
        doAddTagByDir(new ArrayList<String>(), dir);
    }
}
