package com.keensense.admin.util;

import com.loocme.sys.util.ByteUtil;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Administrator
 */
public class AesUtil {

    private static final String CHARSET_NAME = "utf-8";
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String KEYSPEC = "AES";

    // 加密
    public static String encrypt(String sSrc, String sKey) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (sKey == null) {
            throw new NullPointerException("sKey is null");
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            throw new IllegalBlockSizeException("Key长度不是16位");
        }
        byte[] raw = sKey.getBytes(CHARSET_NAME);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEYSPEC);
        Cipher cipher = Cipher.getInstance(ALGORITHM);//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(CHARSET_NAME));

        return ByteUtil.bytesToHex(encrypted);
    }

    // 解密
    public static String decrypt(String sSrc, String sKey) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (sKey == null) {
            throw new NullPointerException("sKey is null");
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            throw new IllegalBlockSizeException("Key长度不是16位");
        }
        byte[] raw = sKey.getBytes(CHARSET_NAME);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEYSPEC);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original, CHARSET_NAME);
    }

    public static void main(String[] args) throws Exception {
        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         * B5DEE41326398A4211E819CB6E0ABB1C
         */
        String cKey = "abcdef0123456789";
        // 需要加密的字串
        String cSrc = "9ling.cn";
        // 加密
        String enString = AesUtil.encrypt(cSrc, cKey);

        // 解密
        String deString = AesUtil.decrypt(enString, cKey);

        System.out.println(deString);
    }
}

