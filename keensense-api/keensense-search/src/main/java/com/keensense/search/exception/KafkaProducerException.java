package com.keensense.search.exception;

/**
 * Created by zhanx xiaohui on 2019-04-18.
 */
public class KafkaProducerException extends RuntimeException {
    private final String errMessage;

    public KafkaProducerException(String errMessage){
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
 * @create: 2019-04-18 16:06
 **/