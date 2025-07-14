package com.keensense.schedule.utils;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author memory_fu
 */
public class IoUtil {
    
    /**
     * 按行读取流数据(根据流)
     */
    public static List<String> readStream(InputStream inStream) throws IOException {
        
        List<String> result = Lists.newArrayList();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
        String line;
        while ((line = bufferedReader.readLine()) != null && StringUtils.isNotBlank(line)) {
            result.add(line);
        }
        
        bufferedReader.close();
        return result;
    }
    
    /**
     * 按行读取流数据(根据文件路径)
     */
    public static List<String> readByFilePath(String filePath) throws IOException {
        
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        
        return readStream(inputStream);
    }
    
    /**
     * 获取文件全路径(根据目录)
     */
    public static List<String> getFileByCatalog(String catalog) {
        
        List<String> allFile = Lists.newArrayList();
        
        if (StringUtils.isEmpty(catalog)) {
            return allFile;
        }
        File file = new File(catalog);
        File[] files = file.listFiles();
        if (null != files) {
            for (File file1 : files) {
                String path = file1.getPath();
                allFile.add(path);
            }
        }
        return allFile;
    }
    
    /**
     * 删除文件(根据文件全路径)
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }
    
    /**
     * 向指定文件写内容，文件不存在自动创建
     *
     * @param content 写文件内容
     * @param path 写文件路径,/结尾
     * @param fileName 写文件名
     * @param isOverwrite true: 覆盖 false:末尾追加
     */
    public static void writeFile(String content, String path, String fileName, boolean isOverwrite)
        throws IOException {
        File file = new File(path + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file, !isOverwrite);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(content);
        bufferedWriter.close();
        
    }
    
    /**
     * 文件复制
     *
     * @param sourcePicPath 源文件
     * @param targetPicPath 目标文件
     */
    public static byte[] copy(String sourcePicPath, String targetPicPath) throws Exception {
        byte[] bytes = getBytes(sourcePicPath);
        writeBytes(bytes, targetPicPath);
        return bytes;
    }
    
    /**
     * 获取文件字节数组
     */
    private static byte[] getBytes(String sourcePath) throws IOException {
        FileInputStream fis = new FileInputStream(new File(sourcePath));
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        fis.close();
        return bytes;
    }
    
    /**
     * 字节数组写文件
     */
    private static void writeBytes(byte[] bytes, String targetPath) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(targetPath));
        fos.write(bytes);
        fos.close();
    }

//    public static void main(String[] args) throws IOException {
//
//        File file = new File("C:\\Users\\memory_fu\\Desktop\\111.txt");
//        FileInputStream stream = new FileInputStream(file);
//        List<String> list = readStream(stream);
//
//        for (String str : list) {
//            String[] split = str.split(" ");
//            HashMap<String, Integer> map = Maps.newHashMap();
//            for (String string : split) {
//                Integer integer = map.get(string);
//                if (integer == null) {
//                    map.put(string, 1);
//                } else {
//                    map.put(string, ++integer);
//                }
//            }
//            Collection<Integer> values = map.values();
//            for (Integer integer : values) {
//                if (integer > 1) {
//                    writeFile(str + "\n", "C:\\Users\\memory_fu\\Desktop\\", "1111.txt", false);
//                }
//            }
//
//
//        }
//    }
}
