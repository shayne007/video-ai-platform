package com.keensense.search.exception;

/**
 * Created by zhanx xiaohui on 2019-05-10.
 */
public class FeaturePushException extends RuntimeException{
    private final String errMessage;

    public FeaturePushException(String errMessage){
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }
}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-05-10 15:02
 **/