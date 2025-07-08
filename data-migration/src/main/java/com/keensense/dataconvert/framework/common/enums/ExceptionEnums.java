package com.keensense.dataconvert.framework.common.enums;

/**
 * @ClassName：ExceptionEnums
 * @Description： <p> ExceptionEnums 异常统一处理枚举类  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 11:17
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 * @version V1.0
*/
public enum  ExceptionEnums {

    /**
     * 数据库相关异常枚举
     */
    MYSQL_INSERT_EXCEPTION(10001,"Mysql insert exception 插入异常!"),
    MYSQL_UPDATE_EXCEPTION(10002,"Mysql update exception 更新异常!"),
    MYSQL_SELECT_EXCEPTION(10003,"Mysql select exception 查询异常!"),
    MYSQL_DELETE_EXCEPTION(10004,"Mysql delete exception 查询异常!"),


    /**
     * 响应相关
     */

    /**
     * 业务相关
     */
    REDIS_SERVICE_EXCEPTION(30001,"Redis service exception!"),


    /**
     * Http服务器相关
     */
    HTTP_OK(200,"Http请求成功"),
    HTTP_NOT_MODIFIED(304,"Not Modified."),
    HTTP_UNAUTHORIZED(401,"Unauthorized"),
    HTTP_NOT_FOUND(404,"Not Found."),
    HTTP_INTERNAL_ERROR(500,"Internal Server Error."),
    HTTP_BAD_GATEWAY(502,"Bad Gateway.");


    /**
     * 异常信息
     */
    private String msg;

    /**
     * 具体异常码
     */
    private int code;


    ExceptionEnums(int code,String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    protected void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    protected void setCode(int code) {
        this.code = code;
    }}
