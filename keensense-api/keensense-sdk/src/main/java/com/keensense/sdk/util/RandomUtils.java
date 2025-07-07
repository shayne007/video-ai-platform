package com.keensense.sdk.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * 随机数生成帮助类
 * @author Administrator
 *
 */
@Slf4j
public  class RandomUtils {


	/**
	 * @return
	 */
	public static String getRandomValiteCode(int size){
		if(size <= 0) {
			size = 6;//默认6位
		}
		String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生的字符串
		return getRandom(randString,size);
	}
	
	/**
	 * 获取随机的验证码
	 * @return
	 */
	public static String getRandom6ValiteCode(int size){
		if(size <= 0) {
			size = 6;//默认6位
		}
		String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";//随机产生的字符串
		return getRandom(randString,size);
	}
	
	/**
	 * 获取随机数
	 * @return
	 */
	public static String getRandom6Number(int size){
		if(size <= 0) {
			size = 6;//默认6位
		}
		String randString = "0123456789";//随机产生的字符串
		return getRandom(randString,size);
	}
	
	/**
	 * 获取8位随机字符串 
	 * @return
	 */
	public static String get8RandomValiteCode(int size){

		if(size <= 0) {
			size = 8;//默认8位
		}
		String randString = "123456789";//随机产生的字符串
		return getRandom(randString,size);
	}

	private static String getRandom(String randString,int size){
		String rst = "";//返回值
		try {
			for (int i = 0; i < size; i++) {
				rst += randString.charAt(SecureRandom.getInstanceStrong().nextInt(9));
			}
		}catch (Exception e){
			log.error("get8RandomValiteCode secureRandom error",e);
		}
		return rst;
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}

