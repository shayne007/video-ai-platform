package com.keensense.common.util;

import lombok.Getter;

/**
 * @author ycl
 * @date 2019/5/14
 */
@Getter
public enum  ApiResponseEnum {

    /**
     * 返回成功
     */
    SUCCESS(0, "正常"),
    /**
     * 返回失败
     */
    FAIL(1, "异常");

    private Integer code;

    private String msg;

    ApiResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
