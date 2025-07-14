package com.keensense.task.async.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: SeqUtil
 * @Description: 序列号生成工具类
 * @Author: cuiss
 * @CreateDate: 2019/8/10 16:51
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class SeqUtil {
    private static volatile Long BASE_ID = 1000L;

    public SeqUtil() {
    }

    public static synchronized String getSequence() {
        try {
            BASE_ID++;
            long curTime = System.currentTimeMillis() - 1000000000  ;
            long tempId = BASE_ID + curTime;
            return Long.toString(tempId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取序列异常:", e.getMessage());
        }
        return null;
    }

    /**
     * 字符串补位至特定长度
     * @param oldValue 原字符
     * @param ch 补位字符
     * @param length 需要转换的特定长度
     * @return
     */
    public static String paddingString(String oldValue,char ch,int length){
        int len = oldValue.length();
        if(length <= len){
            return  oldValue;
        }else{
            for(int i=0;i<length-len;i++){
                oldValue = ch + oldValue;
            }
        }
        return oldValue;
    }
}
