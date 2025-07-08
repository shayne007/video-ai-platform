package com.keensense.common.util;

import java.util.Random;
import java.util.UUID;

/**
 * 随机数生成帮助类
 * @author Administrator
 *
 */
public  class RandomUtils {

    /**
     * 获取当前时间做随机串
     * @return
     */
    public static Long getCurrentTime()
    {
        Long id = System.currentTimeMillis();
        return id;
    }
    
	/***
	 * 随机产生32位16进制字符串
	 * @return
	 */
	public static String getRandom32PK(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	} 
	
	/***
	 * 随机产生24位字符串，以时间开头+7位随机数
	 * @return
	 */
	public static String get24TimeRandom(){
		String timeStr = DateTime.currentDateTime("yyyyMMddHHmmssSSS");
		return timeStr +getRandom6Number(7);
	}
	/***
	 * 随机产生18位字符串，以时间开头+7位随机数
	 * @return
	 */
	public static String get18TimeRandom(){
		String timeStr = DateTime.currentDateTime("yyyyMMddHHmmssSSS");
		return (timeStr +getRandom6Number(7)).substring(6, 24);
	}
	/***
	 * 随机产生32位16进制字符串，以时间开头
	 * @return
	 */
	public static String getRandom32BeginTimePK(){
		String timeStr = DateTime.currentDateTime("yyyyMMddHHmmssSSS");
		String random32 = getRandom32PK();
		return timeStr+random32.substring(17,random32.length());
	}
	
	/***
	 * 随机产生32位16进制字符串，以时间结尾
	 * @return
	 */
	public static String getRandom32EndTimePK(){
		String timeStr = DateTime.currentDateTime("yyyyMMddHHmmssSSS");
		String random32 = getRandom32PK();
		return random32.substring(0,random32.length()-17)+timeStr;
	}
	
	/**




	 * @return
	 */
	public static String getRandomValiteCode(int size){
		if(size <= 0) {
			//默认6位
			size = 6;
		}
		//随机产生的字符串
		String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		//随机种子
		Random random = new Random();
		//返回值
		String rst = "";
		for (int i = 0; i < size; i++) {
			rst += randString.charAt(random.nextInt(36));
		}
		return rst;
	}
	
	/**
	 * 获取随机的验证码
	 * @return
	 */
	public static String getRandom6ValiteCode(int size){
		if(size <= 0) {
			//默认6位
			size = 6;
		}
		//随机产生的字符串
		String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		//随机种子
		Random random = new Random();
		//返回值
		String rst = "";
		for (int i = 0; i < size; i++) {
			rst += randString.charAt(random.nextInt(36));
		}
		return rst;
	}
	
	/**
	 * 获取随机数
	 * @return
	 */
	public static String getRandom6Number(int size){
		if(size <= 0) {
			//默认6位
			size = 6;
		}
		//随机产生的字符串
		String randString = "0123456789";
		//随机种子
		Random random = new Random();
		//返回值
		String rst = "";
		for (int i = 0; i < size; i++) {
			rst += randString.charAt(random.nextInt(6));
		}
		return rst;
	}
	
	/**
	 * 获取8位随机字符串 
	 * @return
	 */
	public static String get8RandomValiteCode(int size){
		if(size <= 0) {
			//默认8位
			size = 8;
		}
		//随机产生的字符串
		String randString = "123456789";
		//随机种子
		Random random = new Random();
		//返回值
		String rst = "";
		for (int i = 0; i < size; i++) {
			rst += randString.charAt(random.nextInt(9));
		}
		return rst;
	}
	
	
}

