package com.keensense.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName：FileUtil
 * @Description： <p> 文件处理工具类 </p>
 * @Author： - Jason
 * @CreatTime：2019/8/5 - 15:29
 * @Modify By：
 * @ModifyTime： 2019/8/5
 * @Modify marker：
 * @version V1.0
*/
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static final char SLASH = '/';

    public static final char BACKSLASH = '\\';

    /**
     * file对象移动目录
     * @param imageFile 文件对象
     * @param dirPath   要移动到的目录
     * @return 返回文件的新地址
     */
    public static String moveFiletoDir(File imageFile, String dirPath,String replaceDir) throws IOException {
        String fileName = imageFile.getName();
        /**
         *  获取项目的路径 + 拼接得到文件要保存的位置 - 获取图片的扩展名
         *
         */
        String extensionName = org.apache.commons.lang.StringUtils.substringAfter(fileName, ".");
        /**
         * 新的图片文件名 = 获取时间戳+"."图片扩展名
         */
        String newFileName = System.currentTimeMillis() + "." + extensionName;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String filePath = dirPath + "/" + newFileName;
        /**
         * 打开输入流
         */
        FileInputStream fis;
        /**
         * 打开输出流
         */
        FileOutputStream fos;
        fis = new FileInputStream(imageFile);
        fos = new FileOutputStream(filePath);
        /**
         * 读取和写入信息
         */
        int len;
        while ((len = fis.read()) != -1) {
            fos.write(len);
        }
        /**
         * 关闭流  先开后关  后开先关
         * 后开先关
         * 先开后关
         */
        fos.close();
        fis.close();
        return filePath.replace(replaceDir,"");
    }


    /**
     * 获取文件里的字符串
     * @param file
     * @return
     * @throws IOException
     */
    public static String txt2String(File file) throws IOException {
        StringBuilder result = new StringBuilder();

        /**
         * 构造一个BufferedReader类来读取文件
         */
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s;
        /**
         * 使用readLine方法，一次读一行
         */
        while((s = br.readLine())!=null){
            result.append(System.lineSeparator()+s);
        }
        br.close();
        return result.toString();
    }

    /**
     * 获取流内容转化为List
     * @param is
     * @return
     * @throws IOException
     */
    public static List<String> text2List(InputStream is) throws IOException {
        List<String> result = new ArrayList<String>();
        InputStreamReader read = new InputStreamReader(is, "utf-8");
        BufferedReader reader = new BufferedReader(read);
        String line;
        while ((line = reader.readLine()) != null) {
            result.add(line);
        }
        read.close();
        return result;
    }

    /**
     * 递归获取文件下的所有文件
     * @param listFile
     * @param paramFile
     * @return
     */
    public static  List<File> getAllFile(List<File> listFile, File paramFile) {
        /**
         *  是文件，添加到文件列表中，本次调用结束，返回文件列表
         */
        if (paramFile.isFile()) {
            if(isImage(paramFile)) {
                listFile.add(paramFile);
            }
            return listFile;
        } else {
            /**
             * 是目录
             * 得到目录下的子文件数组
             */
            File[] localFiles = paramFile.listFiles();
            /**
             * 目录不为空
             */
            if (localFiles != null) {
                /**
                 * 遍历子文件数组
                 *      调用该函数本身
                 */
                for (File localFile : localFiles) {
                    getAllFile(listFile, localFile);
                }
            }
            /**
             * 为空目录，本次调用结束，返回文件列表
             */
            return listFile;
        }
    }

    /**
     * 递归删除所有空文件
     * @param listFile
     * @param paramFile
     * @return
     */
    public static void deleteNullDir(File[] listFile, File paramFile) {
        /**
         * 得到目录下的子文件数组
         */
        File[] localFiles = paramFile.listFiles();
        /**
         *  目录不为空
         */
        if (localFiles != null) {
            /**
             * 遍历子文件数组
             *      是否为目录且子文件的数量是否为0
             *              调用该函数本身
             */
            for (File localFile : localFiles) {
                if(localFile.isDirectory()&&localFile.listFiles().length>0){
                    deleteNullDir(listFile, localFile);
                }else{
                    if(isImage(localFile)) {
                        localFile.delete();
                    }
                }
            }
        }
    }

    /**
     * 删除目录
     * @param dir
     * @throws IOException
     */
    public static void deleteDir(File dir) throws IOException {

        if (dir.isFile()) {
            logger.error("=== 删除异常:IOException -> BadInputException: not a directory.");
            throw new IOException("IOException -> BadInputException: not a directory.");
        }
        logger.info("=== deleteDir : dir :{} ===",dir.getAbsolutePath());
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    file.delete();
                } else {
                    deleteDir(file);
                }
            }
        }
        dir.delete();
    }

    /**
     * 流转字符串
     * @param is
     * @return
     */
    public static String streamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        //new一个StringBuffer用于字符串拼接
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            //当输入流内容读取完毕时
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            //记得关闭流数据 节约内存消耗
            is.close();
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取标准的绝对路径
     * @param file 文件
     * @return 绝对路径
     */
    public static String getAbsolutePath(File file) {
        if (file == null) {
            return null;
        }

        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }


    /**
     * 创建任务目录
     */
    public static void mkDir(String taskDir){
        File file = new File(taskDir);
        if(!file.mkdir()){
            file.mkdir();
        }
    }



    /**
     * 通过url读取网络图片
     * @param
     * @param
     */
    public static void dowloadFileFromUrl(String imageUrl, String destPath){
        if(null==imageUrl||imageUrl.equals("")){
            return;
        }
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url ;
        byte[] buf = new byte[2048];
        int size;
        try{
            // 替换中文
            StringBuilder imageUrlBuffer = new StringBuilder();
            for(int i=0; i<imageUrl.length();i++){
                char a = imageUrl.charAt(i);
                if (a>127){
                    //将中文UTF-8编码
                    imageUrlBuffer.append(URLEncoder.encode(String.valueOf(a), "utf-8"));
                } else{
                    imageUrlBuffer.append(a);
                }
            }
            url = new URL(imageUrlBuffer.toString());
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            int responseCode = httpUrl.getResponseCode();
            if(HttpURLConnection.HTTP_OK == responseCode
                    || HttpURLConnection.HTTP_CREATED == responseCode || HttpURLConnection.HTTP_ACCEPTED == responseCode){
                bis = new BufferedInputStream(httpUrl.getInputStream());
            }else{
                bis = new BufferedInputStream(httpUrl.getErrorStream());
            }
            fos = new FileOutputStream(destPath);
            while ((size = bis.read(buf)) != -1){
                fos.write(buf, 0, size);
            }
            fos.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }finally{
            try{
                fos.close();
                bis.close();
                httpUrl.disconnect();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件以及文件夹
     */
    public static void deleteFileOrDirector(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    // 把每个文件用这个方法进行迭代
                    deleteFileOrDirector(files[i]);
                }
                file.delete();// 删除文件夹
            }
        }
    }

    /**
     * 是否是图片信息
     * @param file
     * @return
     */
    public static  boolean isImage(File file){
        try {
            // 通过ImageReader来解码这个file并返回一个BufferedImage对象
            // 如果找不到合适的ImageReader则会返回null，我们可以认为这不是图片文件
            // 或者在解析过程中报错，也返回false
            Image image = ImageIO.read(file);
            return image != null;
        } catch(IOException ex) {
            return false;
        }
    }

    /**
     * 返回文件名
     * @param filePath 文件
     * @return 文件名
     * @since 4.1.13
     */
    public static String getName(String filePath) {
        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (isFileSeparator(filePath.charAt(len - 1))) {
            // 以分隔符结尾的去掉结尾分隔符
            len--;
        }

        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1;
                break;
            }
        }
        return filePath.substring(begin, len);
    }

    /**
     * 是否为文件分割符号
     * @param c
     * @return
     */
    public static boolean isFileSeparator(char c) {
        return SLASH == c || BACKSLASH == c;
    }


    public static void main(String[] args) {
        File file = new File("D://u2s/noSeatBelt/offlineUpload/20190605/2205433");
        if (!file.exists()){
            file.mkdirs();
        }
    }
}