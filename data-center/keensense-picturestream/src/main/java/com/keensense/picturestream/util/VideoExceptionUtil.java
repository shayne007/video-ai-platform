package com.keensense.picturestream.util;

import com.keensense.common.exception.VideoException;

/**
 * @Description: 异常工具类，临时使用
 * @Author: wujw
 * @CreateDate: 2019/5/14 13:54
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class VideoExceptionUtil {

    private VideoExceptionUtil(){}
    
    /***
     * @description: 获取校验异常
     * @param message 消息
     * @return: com.keensense.common.exception.VideoException
     */
    public static VideoException getValidException(String message){
        return new VideoException(7,message);
    }

    /***
     * @description: 获取配置异常
     * @param message 消息
     * @return: com.keensense.common.exception.VideoException
     */
    public static VideoException getCfgException(String message){
        return new VideoException(5,message);
    }

    /***
     * @description: 数据库操作异常
     * @param message 消息
     * @return: com.keensense.common.exception.VideoException
     */
    public static VideoException getDbException(String message){
        return new VideoException(8,message);
    }
}
