package com.keensense.admin.exception;

import com.keensense.common.exception.VideoException;
import com.keensense.common.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 异常处理器
 * @Date: Created in 11:03 2019/6/11
 * @Version v0.1
 */
@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(VideoException.class)
    public R handleVideoException(VideoException e) {
        R r = new R();
        r.put("code", e.getStatus());
        r.put("msg", e.getMessage());
        log.error("处理自定义异常" + r);
        return r;
    }

    /**
     * 校验异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        R r = new R();
        log.error("handleMethodArgumentNotValidException：" + ex);

        Map<String, String> messages = new HashMap<>();
        BindingResult result = ex.getBindingResult();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors) {
                FieldError fieldError = (FieldError) error;
                messages.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            r.put("code", 600);
            r.put("msg", messages.toString());
            log.error("校验异常：" + messages.toString());
        } else {
            String code = ex.getMessage();
            String msg = ex.getMessage();
            r.put("code", code);
            r.put("msg", msg);
            log.error("校验异常：" + r);
        }
        return r;
    }

    /**
     * 校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R handleMethodArgumentNotValidException(HttpMessageNotReadableException e) {
        log.error("httpMessageNotReadableException：" + e);
        R r = new R();
        r.put("code", 501);
        r.put("msg", "请求格式错误");
        log.error("校验请求异常：" + r);
        return r;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException：" + e);
        R r = new R();
        r.put("code", 505);
        r.put("msg", e.getMessage());
        log.error("handleHttpRequestMethodNotSupportedException：" + r);
        return r;
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error("handleException：" + e);
        log.error(e.getMessage(), e);
        return R.error();
    }

}
