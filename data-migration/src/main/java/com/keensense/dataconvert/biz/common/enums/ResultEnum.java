package com.keensense.dataconvert.biz.common.enums;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.common.enums
 * @Description： <p> ResultEnum app 全局的响应枚举类 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/24 - 16:51
 * @Modify By：
 * @ModifyTime： 2019/7/24
 * @Modify marker：
 */
public enum ResultEnum {


    /**
     * 特征服务响应成功
     */
    ALG_FEATURE_SUCCESS(0,"0","特征服务响应成功"),

    /**
     * 处理成功
     */
    BIZ_DEAL_SUCCESS(200,"200","处理成功"),

    /**
     * mysql 推送到es 异常
     */
    BIZ_MYSQL_PUSH_ES_ERROR(300,"300","mysql2es error");

    /**
     * 数字响应码
     */
    private int resultCode;

    /**
     * 字符响应码
     */
    private String strCode;

    /**
     * 描述
     */
    private String resultDesc;


    ResultEnum(String strCode, String resultDesc) {
        this.strCode = strCode;
        this.resultDesc = resultDesc;
    }

    ResultEnum(int resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }

    ResultEnum(int resultCode, String strCode, String resultDesc) {
        this.resultCode = resultCode;
        this.strCode = strCode;
        this.resultDesc = resultDesc;
    }

    public String getStrCode() {
        return strCode;
    }

    public void setStrCode(String strCode) {
        this.strCode = strCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }}
