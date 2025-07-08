package com.keensense.dataconvert.framework.common.exception;

/**
 * @ClassName：BizException
 * @Description： <p> BizException 自定义异常  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 11:15
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 * @version V1.0
*/
public class BizException extends RuntimeException{

    private static final long serialVersionUID = -2593701532448151906L;

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
    public BizException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }

    public BizException() {
        super();
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message) {
        super(message);
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

}
