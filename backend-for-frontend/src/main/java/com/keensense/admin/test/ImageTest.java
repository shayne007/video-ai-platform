package com.keensense.admin.test;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageTest {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        //new一个URL对象
        URL url = new URL("http://172.16.1.68:8082/20191114/17/bg/human_1005015411-000454_st1573722501956_end1573722503146.jpg");
        //打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        BufferedImage image = ImageIO.read(inStream);
        Graphics g = image.getGraphics();
        g.setColor(Color.RED);//画笔颜色
        g.drawRect(100, 100, 100, 100);//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
        ImageIO.write(image, "jpeg", imOut);
        InputStream is = new ByteArrayInputStream(bs.toByteArray());
        byte[] data = readInputStream(is);
        BASE64Encoder encode = new BASE64Encoder();
        String base64 = encode.encode(data).replaceAll("\\r|\\n", "");
        System.out.println(System.currentTimeMillis() - startTime);
    }

    private static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}