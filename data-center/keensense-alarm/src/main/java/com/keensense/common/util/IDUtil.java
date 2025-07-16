package com.keensense.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author ycl
 * @date 2019/5/14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IDUtil {

    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
