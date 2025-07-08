package com.keensense.densecrowd.test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 22:34 2019/12/28
 * @Version v0.1
 */
public class Base64Test {
    public static void main(String[] args) throws IOException {
        String content = "admin:888888";
        System.out.println("加密之前：" + content);

        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(content.getBytes());
        System.out.println("加密后的内容：" + encode);
        System.out.println(encode);
        System.out.println("YWRtaW46ODg4ODg4");
        System.out.println("YWRtaW46ODg4ODg4".equals(encode));


        // 解密
        BASE64Decoder decoder = new BASE64Decoder();
        String decode = new String(decoder.decodeBuffer(encode));
        System.out.println("解密后的内容：" + decode);
    }
}
