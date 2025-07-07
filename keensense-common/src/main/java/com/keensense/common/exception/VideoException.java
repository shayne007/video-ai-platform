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
	private int status = 200;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public VideoException() {
    }

    public VideoException(int status, String message) {
        super(message);
        this.status = status;
    }

    public VideoException(String message) {
        super(message);
    }

    public VideoException(String message, Throwable cause) {
        super(message, cause);
    }

    public VideoException(Throwable cause) {
        super(cause);
    }

}
