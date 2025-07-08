package com.keensense.admin.util;

import com.google.common.base.Preconditions;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;




/** 
* shiro进行加密解密的工具类封装 
* @User： qing
* @Date： 2016/1/27 0027 
* @Time： 16:49 
*/ 
public class EndecryptUtils {
	
    // 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    
    public static String GetMd5Code(String strObj) {
        String resultString = strObj;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
    
 // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 返回形式只为数字
   /* private static String byteToNum(byte bByte) {
        int iRet = bByte;
        System.out.println("iRet1=" + iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }*/

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

	

	 /** 
     * base64进制加密 
     * 
     * @param password 
     * @return 
     */ 
    public static String encrytBase64(String password) { 
        Preconditions.checkArgument(StringUtils.isNotEmptyString(password), "不能为空");
        byte[] bytes = password.getBytes(); 
        return Base64.encodeToString(bytes);
    } 
    /** 
     * base64进制解密 
     * @param cipherText 
     * @return 
     */ 
    public static String decryptBase64(String cipherText) { 
        Preconditions.checkArgument(StringUtils.isNotEmptyString(cipherText), "消息摘要不能为空");
        return Base64.decodeToString(cipherText);
    } 
    /** 
     * 16进制加密 
     * 
     * @param password 
     * @return 
     */ 
    public static String encrytHex(String password) { 
        Preconditions.checkArgument(StringUtils.isNotEmptyString(password), "不能为空");
        byte[] bytes = password.getBytes(); 
        return Hex.encodeToString(bytes);
    } 
    /** 
     * 16进制解密 
     * @param cipherText 
     * @return 
     */ 
    public static String decryptHex(String cipherText) { 
        Preconditions.checkArgument(StringUtils.isNotEmptyString(cipherText), "消息摘要不能为空");
        return new String(Hex.decode(cipherText));
    } 
    public static String generateKey() 
    { 
        AesCipherService aesCipherService=new AesCipherService();
        Key key=aesCipherService.generateNewKey(); 
        return Base64.encodeToString(key.getEncoded());
    }

    /**
     * 用jdk原生方法实现 SHA-256加密算法
     * @param str
     * @return
     */
    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    /**
     * byte转16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                // 1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
	
    public static void main(String[] args) { 
    	
    /*	String username="yxq";
        String password = "123456"; //IvoT2BTSlobHMaYnzAgtkQ==
        //SysUser user = md5Password(username,password);
        
        System.out.println(password+"\n 通过md5加密之后的密文是："+GetMD5Code(password)); */
        //System.out.println(password+"\n 通过md5加密之后的密文是："+user.getPassword()); 
        //System.out.println("\n "+checkMd5Password(username,password,user.getPassword()));
        //System.out.println("\n "+checkMd5Password("",null,user.getPassword()));
    	System.out.println(encrytBase64("username=admin&passwd=e10adc3949ba59abbe56e057f20f883e"));
        System.out.println(getSHA256("huidvyugdfussbhsbhba")+","+getSHA256("wdguygduygshwu1234AQW").length());
    }

}
