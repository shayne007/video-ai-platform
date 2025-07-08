package com.keensense.admin.annotation;

import java.lang.annotation.*;

/**
 * 登录效验
 *
 * @author zengy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}
