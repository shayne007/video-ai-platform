package com.keensense.search.exception;

/**
 * Created by zhanx xiaohui on 2019-05-09.
 */
public class QueryException extends RuntimeException{
    private final String errMessage;

    public QueryException(String errMessage){
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
 * @create: 2019-05-09 15:36
 **/