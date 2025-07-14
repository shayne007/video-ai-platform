package com.keensense.picturestream.util;

import com.keensense.common.util.DateUtil;
import com.keensense.picturestream.common.BitCommonConst;
import com.keensense.picturestream.entity.PictureInfo;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.stream.FileImageOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public class ImageBaseUtil {

    private ImageBaseUtil(){}

    private static String localUrl;
    private static String remoteUrl;
    /*图片的类型，大图还是小图，小图是thumb,大图是big*/
    private static final String PIC_THUMB_TYPE = "thumb";
    private static final String PIC_BIG_TYPE = "big";

    public static void init(String localUrl, String remoteUrl) {
        ImageBaseUtil.localUrl = localUrl;
        ImageBaseUtil.remoteUrl = remoteUrl;
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

    /**
     * @param pictureInfo pictureInfo
     * @param pois cutX = roi[0]    cutY = pois[1]     cutWidth = roi[2]  cutHeight = roi[3]
     * @description:
     * @return: java.lang.String[]
     */
    public static String[] getPicUrl(PictureInfo pictureInfo, String objextType,
        String uuid, int[] pois) throws IOException {
        log.info("getpicurl");
        String[] url = new String[2];
//        OpencvUtil util = OpencvUtil.getInstance();
        byte[] bytes = Base64.decode(Arrays.toString(pictureInfo.getPicBase64().getBytes()));
//        Mat img = util.loadImage(bytes);
//        byte[][] targetImages = util.cutTargetImages(img, pois, 20, 20);
//        if (null != targetImages) {
//            url[0] = savePicToUrl(targetImages[0], pictureInfo, PIC_THUMB_TYPE, objextType, uuid);
//            url[1] = savePicToUrl(targetImages[1], pictureInfo, PIC_BIG_TYPE, objextType, uuid);
//        }
        return url;
    }

    /**
     * @param pictureInfo pictureInfo
     * @param pois cutX = roi[0]    cutY = pois[1]     cutWidth = roi[2]  cutHeight = roi[3]
     * @description:
     * @return: java.lang.String[]
     */
    public static byte[][] splitPic(PictureInfo pictureInfo, int[] pois) {
        log.info("getpicurl");
//        OpencvUtil util = OpencvUtil.getInstance();
//        byte[] bytes = Base64.decode(pictureInfo.getPicBase64().getBytes());
//        Mat img = util.loadImage(bytes);
//        return util.cutTargetImages(img, pois, 20, 20);
        return new byte[][]{};
    }

    /**
     * 将图片进行解码，然后存入本地，然后将nginx的IP拼接到本地路径前面，由nginx来完成对本地路径的网络映射
     *
     * @param objextType 图片是四类数据中的哪一类
     * @param uuid 数据唯一标识
     * @return 返回图片可以在网页中直接访问大url
     */
    public static String savePicToUrl(byte[] bytes, PictureInfo pictureInfo, String picType,
        String objextType, String uuid) throws IOException {

        String imageUrl = getImageName(pictureInfo, picType, objextType, uuid);
        log.debug("image url is {}", imageUrl);
        String imagePath = localUrl + imageUrl;
        File file = new File(imagePath).getParentFile();
        if (!file.exists()) {
            mkdirAndPosix(file);
        }
        if (byte2image(bytes, imagePath)) {
            return remoteUrl + imageUrl;
        }
        return null;
    }

    /**
     * 拼接图片的名称
     *
     * @param objextType 图片是四类数据中的哪一类
     * @param uuid 数据唯一标识
     */
    private static String getImageName(PictureInfo pictureInfo, String picType, String objextType,
        String uuid) {
        String date = DateUtil.formatDate(new Date(pictureInfo.getCaptureTime()), "yyyyMMddHH");
        final String pathSeparator = "/";
        StringBuilder sb = new StringBuilder();
        sb.append(pathSeparator);
        sb.append(date.substring(0, 8));
        sb.append(pathSeparator);
        sb.append(date.substring(8, 10));
        sb.append(pathSeparator);
        sb.append(picType);
        sb.append(pathSeparator);
        sb.append(objextType).append("_").append(uuid).append("_").append(picType).append("_st")
                .append(pictureInfo.getCaptureTime()).append("_end").append(pictureInfo.getLeaveTime().toString());
        sb.append(".jpg");
        return sb.toString();
    }

    /**
     * 将图片存入本地路径下
     *
     * @param data 图片base64解码之后的二进制
     * @param path 本地路径
     * @return 是否成功
     * @throws IOException 写入过程中抛出的异常
     */
    private static boolean byte2image(byte[] data, String path) throws IOException {
        if (data.length < 3 || "".equals(path)) {
            return false;
        }
        File imageFile = new File(path);
        try (FileImageOutputStream imageOutput = new FileImageOutputStream(imageFile)) {
            imageOutput.write(data, 0, data.length);
        } catch (IOException e) {
            log.error("byte2image error", e);
        }
        posixFile(imageFile);
        return true;
    }

    private static void mkdirAndPosix(File file) throws IOException {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            mkdirAndPosix(parentFile);
        }
        file.mkdir();
        posixFile(file);
    }

    private static void posixFile(File file) throws IOException {
        Path filePath = Paths.get(file.getAbsolutePath());
        Files.setPosixFilePermissions(filePath, BitCommonConst.getFilePfpSet());
    }

}
