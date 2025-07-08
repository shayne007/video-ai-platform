package com.keensense.dataconvert.api.util;

import java.io.Serializable;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util
 * @Description： <p> HttpClientResult  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/24 - 17:57
 * @Modify By：
 * @ModifyTime： 2019/7/24
 * @Modify marker：
 */
public class HttpClientResult implements Serializable {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private String content;


    public HttpClientResult(int code) {
        this.code = code;
    }

    public HttpClientResult(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
