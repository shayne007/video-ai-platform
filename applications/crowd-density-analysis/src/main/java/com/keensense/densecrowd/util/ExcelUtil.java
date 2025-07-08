package com.keensense.densecrowd.util;

import javax.servlet.http.*;
import java.io.*;
import java.net.*;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 17:25 2019/6/15
 * @Version v0.1
 */
public class ExcelUtil {
    public static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
        OutputStream toClient = null;
        try {
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream; charset=utf-8");
            // 如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(file.getName(), "UTF-8"));
            toClient.write(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                toClient.flush();
                toClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                File f = new File(file.getPath());
                f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return response;
    }
}
