package com.keensense.common.exception;

/**
 * 统一返回异常类
 * @description:
 * @author: luowei
 * @createDate:2019年5月10日 上午10:45:13
 * @company:
 */

public class VideoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int status = 500;
    private String message;

    public VideoException(String message) {
        super(message);
        this.message = message;
    }

    public VideoException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public VideoException(String message, Throwable cause) {
        super(message);
        this.status = status;
        this.message = message;
    }



    public VideoException(int status,String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.message = message;
    }

    public VideoException() {

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
