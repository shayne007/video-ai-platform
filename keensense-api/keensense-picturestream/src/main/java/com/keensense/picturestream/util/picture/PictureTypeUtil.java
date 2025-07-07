package com.keensense.picturestream.util.picture;

import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.png.PNGImageReader;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.ByteArrayInputStream;
import java.util.Iterator;

/**
 * @Description: 图片类型工具类
 * @Author: wujw
 * @CreateDate: 2019/5/27 15:02
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class PictureTypeUtil {

    private PictureTypeUtil() {
    }

    /**
     * 获取文件的mimeType
     *
     * @param bytes 图片字符数组
     * @return String
     */
    public static String getMimeType(byte[] bytes) {
        String type = readType(bytes);
        return "data:" + type + ";base64,";
    }

    /**
     * 读取文件类型
     *
     * @param bytes 图片字符数组
     * @return 文件类型
     */
    private static String readType(byte[] bytes) {
        String type = "";

        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             MemoryCacheImageInputStream mcis = new MemoryCacheImageInputStream(bais)){
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
        } catch (Exception e) {
            log.error("readType failed", e);
        }
        return type;
    }

}
