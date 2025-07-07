package com.keensense.archive.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;
import sun.net.www.protocol.ftp.FtpURLConnection;

@Slf4j
public class DownloadImageUtil {
    
    public static String downloadImage(String picUrl) {
        byte[] picBy = null;
        URLConnection conn = null;
        InputStream inputStream = null;
        try {
            
            URL url = new URL(picUrl);
            conn = url.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            inputStream = conn.getInputStream();
            if (null != inputStream) {
                picBy = input2byte(inputStream);
            }
        } catch (Exception e) {
            log.error("http/ftp download error ", e);
        } finally {
            if (null != conn) {
                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection conn1 = (HttpURLConnection) conn;
                    conn1.disconnect();
                } else if (conn instanceof FtpURLConnection) {
                    FtpURLConnection conn1 = (FtpURLConnection) conn;
                    conn1.close();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream close failed", e);
                }
            }
        }
        
        if (null == picBy) {
            // 下载失败
            log.info(String.format("download picture failed, url = [%s]", picUrl));
        }
    
        String picStr = new BASE64Encoder().encode(picBy);
        return picStr;
    }
    
    
    public static byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }
}
