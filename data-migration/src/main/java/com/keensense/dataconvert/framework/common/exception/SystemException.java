package com.keensense.dataconvert.framework.common.exception;

import com.keensense.dataconvert.framework.common.enums.ExceptionEnums;

/**
 * @ClassName：SystemException
 * @Description： <p> SystemException </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 11:16
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 * @version V1.0
*/
public class SystemException extends RuntimeException{

    private static final long serialVersionUID = 1401593546385403720L;

    /**
     * 异常信息
     */
    protected String msg;

    /**
     * 具体异常码
     */
    protected int code;

    /**
     * 异常
     * @param code
     * @param msgFormat
     * @param args
     */
    public SystemException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }

    /**
     * 异常枚举类
     * @param exceptionEnums
     */
    public SystemException(ExceptionEnums exceptionEnums){
        this.code = exceptionEnums.getCode();
        this.msg = exceptionEnums.getMsg();
    }

    /**
     * 异常枚举类
     * @param exceptionEnums
     */
    public SystemException(ExceptionEnums exceptionEnums, Throwable cause){
        super(cause);
        this.code = exceptionEnums.getCode();
        this.msg = exceptionEnums.getMsg();
    }

    public SystemException() {
        super();
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String message) {
        super(message);
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

}
