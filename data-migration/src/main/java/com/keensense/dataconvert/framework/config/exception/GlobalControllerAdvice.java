package com.keensense.dataconvert.framework.config.exception;

import com.keensense.dataconvert.framework.common.base.vo.RespBody;
import com.keensense.dataconvert.framework.common.exception.BizException;
import com.keensense.dataconvert.framework.common.exception.SqlException;
import com.keensense.dataconvert.framework.common.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName：GlobalControllerAdvice
 * @Description： <p> GlobalControllerAdvice </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 11:18
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 * @version V1.0
*/
@ControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public RespBody bizException(BizException ex) {
        logger.error("=== 全局异常处理：bizException ：{} === ",ex.getMsg());
        RespBody respBody = new RespBody();
        respBody.addError(ex.getMessage());
        return  respBody;
    }

    @ResponseBody
    @ExceptionHandler(value = SqlException.class)
    public RespBody bizException(SqlException ex) {
        logger.error("=== 全局异常处理：SqlException :{} === ",ex.getMsg());
        RespBody respBody = new RespBody();
        respBody.addError(ex.getMessage());
        return  respBody;
    }

    @ResponseBody
    @ExceptionHandler(value = SystemException.class)
    public RespBody bizException(SystemException ex) {
        logger.error("=== 全局异常处理：SystemException :{} === ",ex.getMsg());
        RespBody respBody = new RespBody();
        respBody.addError(ex.getMessage());
        return  respBody;
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RespBody bizException(Exception ex) {
        logger.error("=== 全局异常处理：Exception :{} === ",ex);
        RespBody respBody = new RespBody();
        respBody.addError(ex.getMessage());
        return  respBody;
    }

}
