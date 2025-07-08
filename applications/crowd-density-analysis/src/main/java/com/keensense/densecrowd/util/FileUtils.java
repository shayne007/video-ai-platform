package com.keensense.densecrowd.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 文件工具类
 * @author admin
 *
 */
public class FileUtils
{
    /**
     * 通过url读取网络图片
     * @param fileUrl
     * @param destFilePath
     */
    public static void dowloadFileFromUrl(String imageUrl, String destPath) 
    {  
        FileOutputStream fos = null;  
        BufferedInputStream bis = null;  
        HttpURLConnection httpUrl = null;  
        URL url = null;  
        
        byte[] buf = new byte[2048];  
        int size = 0;  
        try 
        {  
            // 替换中文
            StringBuffer imageUrlBuffer = new StringBuffer();
            for(int i=0; i<imageUrl.length();i++){
                char a = imageUrl.charAt(i);
                if (a>127)
                {
                    //将中文UTF-8编码
                    imageUrlBuffer.append(URLEncoder.encode(String.valueOf(a), "utf-8"));
                } else
                {
                    imageUrlBuffer.append(String.valueOf(a));
                }
            }
            
            url = new URL(imageUrlBuffer.toString());
            
            httpUrl = (HttpURLConnection) url.openConnection();  
            httpUrl.connect();  
            
            int responseCode = httpUrl.getResponseCode();
            
            if(HttpURLConnection.HTTP_OK == responseCode || HttpURLConnection.HTTP_CREATED == responseCode || HttpURLConnection.HTTP_ACCEPTED == responseCode)
            {
            	bis = new BufferedInputStream(httpUrl.getInputStream());  
            }
            else
            {
            	bis = new BufferedInputStream(httpUrl.getErrorStream());  
            }
            
            
            //bis = new BufferedInputStream(httpUrl.getInputStream());  
            fos = new FileOutputStream(destPath);
            
            
            while ((size = bis.read(buf)) != -1) 
            {  
                fos.write(buf, 0, size);  
            }  
            fos.flush();  
        }
        catch (Exception e) 
        { 
            e.printStackTrace();
        } 
        finally 
        {  
            try 
            {
                if(fos!=null) {
                    fos.close();
                }
                if(bis != null) {
                    bis.close();
                }
                if(httpUrl != null) {
                    httpUrl.disconnect();
                }
            } 
            catch (Exception e)
            {  
                e.printStackTrace();
            }
        }
    }
    
    public static String getFileNameFromUrl(String  fileUrl)
    {
        return fileUrl;
        
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
	 * @Description: 检测磁盘空间使用情况
	 * @param limitValue 限制值(单位GB)
	 * @return 
	 * 		-1 - 未设置预警值，跳过检验返回
	 * 		0 - 正常
	 * 		1 - 磁盘可用空间低于预警值
	 * @ReturnType: Integer
	 */
	public static Integer checkDiskUsableSpace(Integer limitValue) {
		if(limitValue == null || limitValue <= 0) {
			return -1;
		}
		Integer result = 0;
		File file = new File("/u2s");
		if (file.exists()) {
			//计算可用空间
			long usableSpace = file.getUsableSpace() / (1024 * 1024 * 1024);
			if(usableSpace >= limitValue) {
				result = 0;
			} else {
				result = 1;
			}
		}
		return result;
	}

	public static String getFileString(String filePath){
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            InputStream inputStream = resource.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            List<String> list = IOUtils.readLines(inputStream);
            for (String string : list) {
                stringBuffer.append(string);
            }
            String modelFile = stringBuffer.toString();
            return  modelFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
