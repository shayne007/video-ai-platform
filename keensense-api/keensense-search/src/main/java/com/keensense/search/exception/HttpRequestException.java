package com.keensense.search.exception;

/**
 * Created by memory_fu on 2019/6/13.
 */
public class HttpRequestException extends RuntimeException{

    private final String errMessage;

    public HttpRequestException(String errMessage){
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

}
