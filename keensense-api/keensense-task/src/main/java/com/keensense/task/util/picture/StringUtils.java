package com.keensense.task.util.picture;

import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.JimiException;
import com.sun.jimi.core.JimiWriter;
import com.sun.jimi.core.options.JPGOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @Description: 莫名工具类
 * @Author: wujw
 * @CreateDate: 2019/5/27 15:02
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class StringUtils {

    private StringUtils() {}

    private static final byte[] EMPTY_ARRAY = new byte[0];

    /**
     * 获取图片格式函数
     *
     * @param input base64图片编码
     * @return String
     */
    public static String getExtension(byte[] input) {
        // 图片格式
        String format = "";

        try (ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(input))){
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (iter.hasNext()) {

                format = iter.next().getFormatName();
            }
        } catch (IOException e) {
            log.error("getExtension failed", e);
        }
        return format;
    }

    /**
     * 经测试 支持的格式 (png,gif) -->jpeg 不支持的格式(bmp,tif)
     *
     * @param picBy base64图片编码
     * @return byte[]
     */
    public static byte[] forJpg(byte[] picBy) {
        try (InputStream input = new ByteArrayInputStream(picBy);
             ByteArrayOutputStream ots = new ByteArrayOutputStream()){
            JPGOptions options = new JPGOptions();
            options.setQuality(100);
            ImageProducer image = Jimi.getImageProducer(input);
            JimiWriter writer = Jimi.createJimiWriter(Jimi.getEncoderTypes()[3], ots);
            writer.setSource(image);
            writer.setOptions(options);
            writer.putImage(ots);
            return ots.toByteArray();
        }  catch (JimiException e) {
            log.error("forJpg JimiException failed", e);
        } catch (IOException e) {
            log.error("forJpg IOException failed", e);
        }
        return EMPTY_ARRAY;
    }

    public static byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        IOUtils.closeQuietly(swapStream);
        IOUtils.closeQuietly(inStream);
        return in2b;
    }

}
