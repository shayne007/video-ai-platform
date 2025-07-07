package com.keensense.glstsdk;

import java.util.concurrent.Callable;

import lombok.Data;

/**
 * Created by memory_fu on 2019/9/5.
 */
@Data
public class GlAlgoRequest implements Callable<String> {

    // 请求url
    private String reqUrl;
    // 请求参数
    private String reqPara;

    public GlAlgoRequest(String reqUrl, String reqPara) {
        this.reqUrl = reqUrl;
        this.reqPara = reqPara;
    }

    @Override
    public String call() {

        try {
            // String responseStr = HttpUtil.requestMethod(HttpUtil.HTTP_POST, reqUrl, reqPara);
            String responseStr = "SUCCESS";
            if (responseStr.contains("SUCCESS")) {
                return "true";
            }
            System.out.println("====responseStr : " + responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }
}
