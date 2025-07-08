package com.keensense.admin.util;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageBaseUtil {

    private ImageBaseUtil() {
    }

    public static final byte[] input2byte(InputStream inStream) throws IOException {
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

    public static String forPng2Jpg(String sourceFilePath) throws IOException {
        BufferedImage bufferedImage;
        String targetFilePath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf(".")) + ".jpg";
        try {
            // read image file
            bufferedImage = ImageIO.read(new File(sourceFilePath));
            // create a blank, RGB, same width and height, and a white
            // background
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            // TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            // write to jpeg file
            ImageIO.write(newBufferedImage, "jpg", new File(targetFilePath));

        } catch (IOException e) {
            throw new IOException(e);
        }

        return targetFilePath;
    }
}
