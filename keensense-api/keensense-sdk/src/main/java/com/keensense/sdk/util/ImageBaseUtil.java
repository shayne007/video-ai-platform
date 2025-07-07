package com.keensense.sdk.util;

import com.keensense.common.exception.VideoException;
import com.keensense.sdk.constants.SdkExceptionConst;
import com.loocme.security.encrypt.Base64;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;
import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.JimiException;
import com.sun.jimi.core.JimiWriter;
import com.sun.jimi.core.options.JPGOptions;
import java.awt.image.ImageProducer;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Decoder;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/10 14:15
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class ImageBaseUtil {

    public static byte[] getPictureBytes(String picture) {
        if (StringUtil.isNull(picture)) {
            return null;
        }
        String regex = "^(http|ftp).*$";
        byte[] picBy = null;
        // 获取图片
        if (PatternUtil.isMatch(picture, regex, Pattern.CASE_INSENSITIVE)) {
            URLConnection conn = null;

            try {
                // 下载图片
                URL url = new URL(picture);
                conn = url.openConnection();
                // 设置超时间为3秒
                conn.setConnectTimeout(3 * 1000);
                conn.setReadTimeout(10 * 1000);
                // 防止屏蔽程序抓取而返回403错误
                conn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                InputStream inputStream = conn.getInputStream();
                if (null != inputStream) {
                    picBy = input2byte(inputStream);
                }
            } catch (Exception e) {
                log.error("getPictureBytes error", e);
                return null;
            }
        } else {
            // base64解析
            picBy = Base64.decode(picture.getBytes());
        }

        String suffix = ImageBaseUtil.getExtension(picBy);
        if (PatternUtil.isNotMatch(suffix, "^(jpg|jpeg)$",
            Pattern.CASE_INSENSITIVE)) {
            picBy = ImageBaseUtil.forJpg(new ByteArrayInputStream(picBy));
            if (picBy == null) {
                log.error("不支持的图片格式: " + suffix);
                return null;
            }
        }
        return picBy;
    }

    public static byte[] forJpg(InputStream input) {
        try {
            ByteArrayOutputStream ots = new ByteArrayOutputStream();

            JPGOptions options = new JPGOptions();
            options.setQuality(100);

            ImageProducer image = Jimi.getImageProducer(input);
            JimiWriter writer = Jimi.createJimiWriter(Jimi.getEncoderTypes()[3],
                ots);

            writer.setSource(image);
            writer.setOptions(options);
            writer.putImage(ots);

            return ots.toByteArray();
        } catch (JimiException e) {
            log.error("forJpg error", e);
        }
        return null;
    }

    public static String getExtension(byte[] by) {
        return getExtension(new ByteArrayInputStream(by));
    }

    public static final byte[] input2byte(InputStream inStream)
        throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();

        IOUtils.closeQuietly(swapStream);
        IOUtils.closeQuietly(inStream);
        return in2b;
    }

    /**
     * 获取图片格式函数
     */
    public static String getExtension(InputStream input) {
        // 图片格式
        String format = "";
        ImageInputStream iis = null;

        try {
            iis = ImageIO.createImageInputStream(input);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (iter.hasNext()) {
                format = iter.next().getFormatName();
            }

        } catch (IOException e) {
            log.error("getExtension error", e);
        } finally {
            if (iis != null) {
                try {
                    iis.close();
                } catch (IOException e) {
                    log.error("getExtension error", e);
                }
            }
        }
        return format;
    }

    public static Map<String, Integer> getWH(String imgString, StringBuffer faceBase64)
        throws VideoException {
        Map<String, Integer> map = new HashMap<>();
        byte[] bytes = null;
        try {
            if (PatternUtil.isMatch(imgString, "^(http|ftp).*$", Pattern.CASE_INSENSITIVE)) {
                // 传入URL，支持图片下载
                URLConnection conn = null;
                // 下载图片
                URL url = new URL(imgString);
                conn = url.openConnection();
                // 设置超时间为3秒
                conn.setConnectTimeout(3 * 1000);
                conn.setReadTimeout(10 * 1000);
                // 防止屏蔽程序抓取而返回403错误
                conn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                InputStream inputStream = conn.getInputStream();
                if (null != inputStream) {
                    bytes = input2byte(inputStream);
                    faceBase64.append(new String(Base64.encode(bytes)));
                }

            } else {
                bytes = new BASE64Decoder().decodeBuffer(imgString);
                faceBase64.append(imgString);
            }
            // 不带类似data:image/jpg;base64,前缀的解析
            ImageInputStream imageInputstream = new MemoryCacheImageInputStream(
                new ByteArrayInputStream(bytes));
            // 不使用磁盘缓存
            ImageIO.setUseCache(false);
            Iterator<ImageReader> it = ImageIO.getImageReaders(imageInputstream);
            if (it.hasNext()) {
                ImageReader imageReader = it.next();
                // 设置解码器的输入流
                imageReader.setInput(imageInputstream, true, true);

                // 图像文件格式后缀
                int height = imageReader.getHeight(0);
                int width = imageReader.getWidth(0);
                map.put("width", width);
                map.put("height", height);
                imageInputstream.close();
                return map;
            }

        } catch (Exception e) {
            log.error("getWH-glst", e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "getWH-glst" + e.getMessage());
        }

        return null;
    }

    /*byte数组到图片到硬盘上*/
    public static void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) {
            return;
        }
        /*判断输入的byte是否为空*/
        /*打开输入流*/
        try (FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path))) {
            imageOutput.write(data, 0, data.length);//将byte写入硬盘
        } catch (Exception ex) {
            log.error("byte2image error", ex);
        }
    }

}

