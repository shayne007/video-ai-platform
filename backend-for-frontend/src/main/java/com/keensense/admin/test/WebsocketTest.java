package com.keensense.admin.test;

import com.keensense.admin.util.StringUtils;
import com.keensense.admin.util.VideoXmlUtil;
import com.keensense.common.util.DateUtil;
import com.keensense.common.util.ImageUtils;
import com.keensense.common.ws.WebsocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.junit.Test;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebsocketTest {

    @Test
    public void test1() throws Exception {
        String url = "vas://name=name&psw=psw&srvip=172.16.1.39&srvport=9080&devid=43010000001320000001&";
        String[] masInfos = PatternUtil.getMatch(url, "vas://name=([^&]*)&psw=([^&]*)&srvip=([^&]*)&srvport=([^&]*)&devid=([^&]*)&");
        String masMasterIp = masInfos[3];
        String masMasterPort = masInfos[4];
        String wsAddr = "ws://"+masMasterIp+":"+masMasterPort;
        JSONObject param = new JSONObject();
        param.put("request", "getRecordList");
        param.put("seq", System.currentTimeMillis());
        param.put("channel",  masInfos[5]);
        param.put("startTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        param.put("stopTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        WebsocketClient websocketClient = new WebsocketClient(new URI(wsAddr), new Draft_6455());
        websocketClient.connectBlocking();
        websocketClient.send(param.toString());
        String msg = websocketClient.getResponse();
        int i = 1;
        long startTime = System.currentTimeMillis();
        while (StringUtils.isEmpty(msg)) {
            msg = websocketClient.getResponse();
            Thread.sleep(100);
            i++;
            long costTime = System.currentTimeMillis() - startTime;
            if (costTime > 15000){
                System.out.println("超时无录像");
                return;
            }

        }
        System.out.println("i++++++++++++"+i);
        System.out.println("msg++++++++++++++++++++++"+msg);
        JSONObject jsonResp = JSONObject.fromObject(msg);
        System.out.println("jsonResp++++++++++++++++++++++"+jsonResp);
        String errCode = jsonResp.getString("errCode");
        String info = jsonResp.getString("info");
        String sumNumStr = VideoXmlUtil.getSumNumString(info);
        if (StringUtils.isNotEmptyString(sumNumStr)){
            Integer sumNum = Integer.valueOf(sumNumStr);
            if ("0".equals(errCode) && sumNum > 0){
                System.out.println(sumNum);
            }
        }
        System.out.println("执行结果: " + jsonResp.toString());
    }

    @Test
    public void test2(){
        try {
            String startTime = "2019-11-29 11:36:52";
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtil.parseDate(startTime, "yyyy-MM-dd HH:mm:ss"));
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBase64() throws Exception {
        String imageUrl = "";
        String urlImage = ImageUtils.getURLImage(imageUrl);
        System.out.println(urlImage);
    }

}
