package com.keensense;

import com.alibaba.fastjson.JSONObject;
import com.keensense.glstsdk.RandomUtil;

import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by memory_fu on 2019/9/5.
 */
public class FeatureRequest implements Callable<String> {
    
    // 请求url
    private String reqUrl = "http://172.16.1.20:39081/addobject";
    // 人形特征
    private String personFeature = "";
    // 请求参数
    private String reqPara;
    
    public FeatureRequest(String personFeature) {
		this.personFeature = personFeature;
	}

	public String createReqPara(){
    	JSONObject jsonObject = new JSONObject();
    	String task = RandomUtil.getStr(999999);
    	long timestamp = System.currentTimeMillis();
    	String uuid = UUID.randomUUID().toString().replaceAll("-","");
    	
    	jsonObject.put("task", task);
    	jsonObject.put("first_object", 0);
    	jsonObject.put("timestamp", timestamp);
    	jsonObject.put("uuid", uuid);
    	jsonObject.put("type", 1);
    	jsonObject.put("feature", personFeature);
    	jsonObject.put("firm", 0);
    	
//    	System.out.println("feature length=====:"+jsonObject.getString("feature").length());
    	return jsonObject.toJSONString();
    }
	public static long getRandom(int start, int end){
		return (long)(Math.random() * (end-start+1) + start);
	}
    @Override
    public String call() throws Exception {
        
        try {
        	String createReqPara = createReqPara();
//            String responseStr = HttpUtil
//                .requestMethod(HttpUtil.HTTP_POST, reqUrl, createReqPara);
			long rand = getRandom(1,20);
//			Time.sleep(rand);
			String responseStr ="";
			if(rand/2 == 0){
				responseStr = "OK";

			}   else{
				responseStr = "ERR";
			}
//			String responseStr = HttpUtil1.sendHttpPost(reqUrl, createReqPara);
	
			if(responseStr.contains("OK")){
                return "true";
            }
//            System.out.println("====responseStr : "+responseStr);
        }catch (RuntimeException e){
            e.printStackTrace();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return "false";
    }

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public String getReqPara() {
		return reqPara;
	}

	public void setReqPara(String reqPara) {
		this.reqPara = reqPara;
	}

	public String getPersonFeature() {
		return personFeature;
	}

	public void setPersonFeature(String personFeature) {
		this.personFeature = personFeature;
	}
	
}
