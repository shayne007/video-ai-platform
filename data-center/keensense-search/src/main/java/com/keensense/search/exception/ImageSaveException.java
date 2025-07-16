package com.keensense.search.exception;

/**
 * Created by zhanx xiaohui on 2019-03-07.
 */
public class ImageSaveException extends RuntimeException{
  private final String errMessage;

  public ImageSaveException(String errMessage){
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
 * @create: 2019-03-07 16:29
 **/