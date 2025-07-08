package com.keensense.common.exception;


import com.keensense.common.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 * @description:
 * @author: luowei
 * @createDate:2019年5月10日 下午2:56:42
 * @company:
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	private final static String FAILURE_CODE = "-1";
	private final static String FAILURE_MESS = "系统异常，请再重试一次，若有异常，请再联系系统管理员！";
	
	/**
	 *  异常返回处理，在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
	 */
    @ExceptionHandler(Exception.class)
    @ResponseBody 
    public String exceptionHandler(Exception e, HttpServletResponse response) {
    	if(e instanceof VideoException){
    		log.error("VideoException code: " + ((VideoException) e).getStatus() + " message:" + e.getMessage(), e);
    		return ResultUtils.renderFailure(String.valueOf(((VideoException) e).getStatus()),e.getMessage(),null);
    	}else {
    		log.error("GlobalExceptionHandler", e);
    		return ResultUtils.renderFailure(FAILURE_CODE,  FAILURE_MESS, "GlobalExceptionHandler");
    	}
    }
}