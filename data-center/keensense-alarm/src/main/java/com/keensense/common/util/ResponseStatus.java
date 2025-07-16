package com.keensense.common.util;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ycl
 * @date 2019/5/13
 */
@Data
public class ResponseStatus {
    private String requestURL;
    private Integer statusCode;
    private String statusString;
    private String id;
    private LocalDateTime localTime;
}
