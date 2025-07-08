package com.keensense.admin.util;

import java.util.Random;
import java.util.UUID;

/**
 * 随机数生成帮助类
 *
 * @author Administrator
 */
public class RandomUtils {

    /**
     * 获取当前时间做随机串
     *
     * @return
     */
    public static Long getCurrentTime() {
        Long id = System.currentTimeMillis();
        return id;
    }

    /***
     * 随机产生32位16进制字符串
     * @return
     */
    public static String getRandom32PK() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /***
     * 随机产生24位字符串，以时间开头+7位随机数
     * @return
     */
    public static String get24TimeRandom() {
        String timeStr = DateTime.currentDateTime(DateTime.CURRENTTIME_FORMAT);
        return timeStr + getRandom6Number(7);
    }

    /***
     * 随机产生32位16进制字符串，以时间开头
     * @return
     */
    public static String getRandom32BeginTimePK() {
        String timeStr = DateTime.currentDateTime(DateTime.CURRENTTIME_FORMAT);
        String random32 = getRandom32PK();
        return timeStr + random32.substring(17, random32.length());
    }

    /***
     * 随机产生32位16进制字符串，以时间结尾
     * @return
     */
    public static String getRandom32EndTimePK() {
        String timeStr = DateTime.currentDateTime(DateTime.CURRENTTIME_FORMAT);
        String random32 = getRandom32PK();
        return random32.substring(0, random32.length() - 17) + timeStr;
    }

    /**
     * @return
     */
    public static String getRandomValiteCode(int size) {
        if (size <= 0) size = 6;//默认6位
        String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生的字符串
        return ramdom(randString, size);
    }

    /**
     * 获取随机的验证码
     *
     * @return
     */
    public static String getRandom6ValiteCode(int size) {
        if (size <= 0) size = 6;//默认6位
        String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";//随机产生的字符串
        return ramdom(randString, size);
    }

    /**
     * 获取随机数
     *
     * @return
     */
    public static String getRandom6Number(int size) {
        if (size <= 0) size = 6;//默认6位
        String randString = "0123456789";//随机产生的字符串
        return ramdom(randString, size);
    }

    private static String ramdom(String randomChar, int size) {
        Random random = new Random();//随机种子
        StringBuilder rst = new StringBuilder();//返回值
        for (int i = 0; i < size; i++) {
            rst.append(randomChar.charAt(random.nextInt(6)));
        }
        return rst.toString();
    }

    /**
     * 获取8位随机字符串
     *
     * @return
     */
    public static String get8RandomValiteCode(int size) {
        if (size <= 0) size = 8;//默认8位
        String randString = "123456789";//随机产生的字符串
        return ramdom(randString, size);
    }

    /***
     * 随机产生18位字符串，以时间开头+7位随机数
     * 在并发的情况下会生成一样的随机数，加锁防止重复
     * @return
     */
    public static String get18TimeRandom() {
        synchronized (RandomUtils.class) {
            String timeStr = DateTime.currentDateTime(DateTime.CURRENTTIME_FORMAT);
            return (timeStr + getRandom6Number(7)).substring(6, 24);
        }
    }


    public static void main(String[] args) {
        System.out.println(get8RandomValiteCode(12));
        /* for (int i=0;i<100;i++)
         {
			 System.out.println(get24TimeRandom());
		 }*/
        //System.out.println("随机验证码6位:"+getRandomValiteCode(6));
        //System.out.println("随机"+RandomUtils.getRandom32PK().length()+"位："+RandomUtils.getRandom32PK());
        //System.out.println("随机"+RandomUtils.getRandom32BeginTimePK().length()+"位以时间打头："+RandomUtils.getRandom32BeginTimePK());
        //System.out.println("随机"+RandomUtils.getRandom32EndTimePK().length()+"位以时间结尾："+RandomUtils.getRandom32EndTimePK());
        //System.out.println(get8RandomValiteCode(16));
    }
}

