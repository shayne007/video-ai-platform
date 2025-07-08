package com.keensense.admin.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	/**
	 * 压缩文件列表中的文件
	 *
	 * @param files
	 * @param outputStream
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static void zipFile(List files, ZipOutputStream outputStream) throws IOException, ServletException {
	    try {
	        int size = files.size();
	        //压缩列表中的文件
	        for (int i = 0; i < size; i++) {
	            File file = (File) files.get(i);
	            try {
	                zipFile(file, outputStream);
	            } catch (Exception e) {
	                continue;
	            }
	        }
	    } catch (Exception e) {
	        throw e;
	    }
	}

	/**
	 * 将文件写入到zip文件中
	 *
	 * @param inputFile
	 * @param outputstream
	 * @throws Exception
	 */
	public static void zipFile(File inputFile, ZipOutputStream outputstream) throws IOException, ServletException {
	    try {
	        if (inputFile.exists()) {
	            if (inputFile.isFile()) {
	                FileInputStream inStream = new FileInputStream(inputFile);
	                BufferedInputStream bInStream = new BufferedInputStream(inStream);
	                ZipEntry entry = new ZipEntry(inputFile.getName());
	                outputstream.putNextEntry(entry);

	                final int MAX_BYTE = 10 * 1024 * 1024;    //最大的流为10M
	                long streamTotal = 0;                      //接受流的容量
	                int streamNum = 0;                      //流需要分开的数量
	                int leaveByte = 0;                      //文件剩下的字符数
	                byte[] inOutbyte;                          //byte数组接受文件的数据

	                streamTotal = bInStream.available();                        //通过available方法取得流的最大字符数
	                streamNum = (int) Math.floor(streamTotal / MAX_BYTE);    //取得流文件需要分开的数量
	                leaveByte = (int) streamTotal % MAX_BYTE;                //分开文件之后,剩余的数量

	                if (streamNum > 0) {
	                    for (int j = 0; j < streamNum; ++j) {
	                        inOutbyte = new byte[MAX_BYTE];
	                        //读入流,保存在byte数组
	                        bInStream.read(inOutbyte, 0, MAX_BYTE);
	                        outputstream.write(inOutbyte, 0, MAX_BYTE);  //写出流
	                    }
	                }
	                //写出剩下的流数据
	                inOutbyte = new byte[leaveByte];
	                bInStream.read(inOutbyte, 0, leaveByte);
	                outputstream.write(inOutbyte);
	                outputstream.closeEntry();     //Closes the current ZIP entry and positions the stream for writing the next entry
	                bInStream.close();    //关闭
	                inStream.close();
	                inputFile.delete();
	            }
	        } else {
	            throw new ServletException("文件不存在！");
	        }
	    } catch (IOException e) {
	        throw e;
	    }
	}

	/**
	 * 下载打包的文件
	 *
	 * @param file
	 * @param response
	 */
	public static void downloadZip(File file, HttpServletResponse response) {
	    try {
	        // 以流的形式下载文件。
	        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
	        byte[] buffer = new byte[fis.available()];
	        fis.read(buffer);
	        fis.close();
	        // 清空response
	        response.reset();

	        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();
	        file.delete();        //将生成的服务器端文件删除
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
}
