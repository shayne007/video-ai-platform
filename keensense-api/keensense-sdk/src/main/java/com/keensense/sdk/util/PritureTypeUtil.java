package com.keensense.sdk.util;

import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.png.PNGImageReader;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PritureTypeUtil {

    /**
     * 获取文件的mimeType
     */
    public static String getMimeType(byte[] bytes) {
        String type = readType(bytes);
        return "data:" + type + ";base64,";
    }

    /**
     * 读取文件类型
     */
    private static String readType(byte[] bytes) {
        String type = "";
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            MemoryCacheImageInputStream mcis = new MemoryCacheImageInputStream(bais)) {
            Iterator itr = ImageIO.getImageReaders(mcis);
            while (itr.hasNext()) {
                ImageReader reader = (ImageReader) itr.next();
                if (reader instanceof GIFImageReader) {
                    type = "image/gif";
                } else if (reader instanceof JPEGImageReader) {
                    type = "image/jpeg";
                } else if (reader instanceof PNGImageReader) {
                    type = "image/png";
                } else if (reader instanceof BMPImageReader) {
                    type = "application/x-bmp";
                }
            }
        } catch (IOException e) {
            log.error("readType error", e);
        }
        return type;
    }

    /**
     * 标示一致性比较
     *
     * @param buf 待检测标示
     * @param markBuf 标识符字节数组
     * @return 返回false标示标示不匹配
     */
    private static boolean compare(byte[] buf, byte[] markBuf) {
        for (int i = 0; i < markBuf.length; i++) {
            byte b = markBuf[i];
            byte a = buf[i];
            if (a != b) {
                return false;
            }
        }
        return true;
    }

    /**
     * 读取文件类型
     */
    public static String readImageType(byte[] bytes) {
        String type = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            MemoryCacheImageInputStream mcis = new MemoryCacheImageInputStream(bais)) {
            Iterator itr = ImageIO.getImageReaders(mcis);
            while (itr.hasNext()) {
                ImageReader reader = (ImageReader) itr.next();
                if (reader instanceof GIFImageReader) {
                    type = "gif";
                } else if (reader instanceof JPEGImageReader) {
                    type = "jpg";
                } else if (reader instanceof PNGImageReader) {
                    type = "png";
                } else if (reader instanceof BMPImageReader) {
                    type = "bmp";
                }
            }
        } catch (IOException e) {
            log.error("readImageType error", e);
        }
        return type;
    }
}
