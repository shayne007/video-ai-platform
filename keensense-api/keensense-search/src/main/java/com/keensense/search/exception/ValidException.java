package com.keensense.search.exception;

/**
 * Created by zhanx xiaohui on 2019-02-23.
 */
public class ValidException extends RuntimeException {

    private final String errMessage;

    public ValidException(String errMessage){
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
 * @create: 2019-02-23 14:52
 **/