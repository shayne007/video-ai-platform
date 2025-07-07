package com.keensense.glstsdk;

import java.util.Random;

public class RandomUtil {
	
	public static final Random r = new Random();

	/**
	 * 获取随机数字符串
	 * @param bound  随机范围
	 * @return
	 */
	public static String getStr(int bound){
		return String.valueOf(r.nextInt(bound));
	}
	
	/**
	 * 获取随机数数值
	 * @param bound 随机范围
	 * @return
	 */
    public static int getInt(int bound){
        return r.nextInt(bound);
    }
    
	public static void main(String[] args) {
		for (int i = 0; i < 30; i++) {
			String randomNumInt = getStr(3);
			System.out.println(randomNumInt);
			
		}
		
	}

}
