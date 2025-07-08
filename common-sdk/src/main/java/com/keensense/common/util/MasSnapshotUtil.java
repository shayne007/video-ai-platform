package com.keensense.common.util;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.ws.WebsocketClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.java_websocket.drafts.Draft_6455;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.util.List;

/**
 * @ClassName: MasSnapshotUtil
 * @Description: MAS获取快照工具类
 * @Author: cuiss
 * @CreateDate: 2019/11/27 17:11
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class MasSnapshotUtil {

    /**
     * @return Base64 字符串
     */
    public static String getMasSnapshotBase64(String masMasterIp, String masMasterPort, String channelId) throws Exception {
        log.info("masMasterIp:" + masMasterIp + " masMasterPort:" + masMasterPort + " channelId:" + channelId);
        return Base64.encodeBase64String(getMasSnapshotByte(masMasterIp, masMasterPort, channelId));
    }

    /**
     * @return byte 数组
     */
    public static byte[] getMasSnapshotByte(String masMasterIp, String masMasterPort, String channelId) throws Exception {
        byte[] pictureByte = null;


        String masNodeIp = getMasNode(masMasterIp, masMasterPort);

        pictureByte = getPlayStream(masNodeIp, channelId);

        if (pictureByte != null) {
            // FileUtil.write(new File("D:\\test\\bbb111.jpg"), pictureByte);
        }

        return pictureByte;
    }

    /**
     * 向Mas node请求2s的播放流
     *
     * @param masNodeIp
     * @param channelId
     * @return byte[]
     * @throws Exception
     */
    private static byte[] getPlayStream(String masNodeIp, String channelId) throws Exception {
        WebsocketClient websocketClient = null;
        JSONObject param = null;
        String wsAddr = "ws://" + masNodeIp;
        String nodeIp = masNodeIp.split(":")[0];
        websocketClient = new WebsocketClient(new URI(wsAddr), new Draft_6455());
        websocketClient.connectBlocking();
        param = new JSONObject();
        param.put("request", "play");
        param.put("seq", 123);
        param.put("channel", channelId);
        param.put("type", "real_Platform");
        param.put("startTime", "");
        param.put("stopTime", "");
        param.put("nodeIP", nodeIp);
        param.put("nodePort", 0);
        param.put("serialNum", "");
        param.put("use", "");

        log.info("websocket param :" + param.toString());
        websocketClient.send(param.toString());
        String msg = websocketClient.getResponse();
        long openTime = System.currentTimeMillis();
        while (StringUtils.isEmpty(msg)) {
            msg = websocketClient.getResponse();
            Thread.sleep(100);
            long costTime = System.currentTimeMillis() - openTime;
            if (costTime > 10000) {
                log.info("超时调用websocket,接口返回msg:" + msg);
            }
        }


        log.info("msg:" + msg);
        JSONObject jsonResp = JSONObject.parseObject(msg);
        param = new JSONObject();
        param.put("request", "stop");
        param.put("seq", 123);
        param.put("channel", channelId);
        param.put("callId", jsonResp.get("callId"));

        log.info("websocket param :" + param.toString());
        websocketClient.send(param.toString());

        websocketClient.closeBlocking();

        List<byte[]> masByteList = websocketClient.getMasByteList();
        log.info("收到流数量:" + masByteList.size());

        int framesLen = 0;
        for (int i = 0; i < masByteList.size(); i++) {
            framesLen += masByteList.get(i).length;
        }
        byte[] framesBy = new byte[framesLen];
        framesLen = 0;
        for (int i = 0; i < masByteList.size(); i++) {
            System.arraycopy(masByteList.get(i), 0, framesBy, framesLen, masByteList.get(i).length);
            framesLen += masByteList.get(i).length;
        }

        InputStream inputStream = new ByteArrayInputStream(framesBy);
        byte[] picBy = getByteFromFfmpeg(inputStream);
        return picBy;
    }

    /**
     * 利用ffmpeg解出一个i帧
     *
     * @param inputStream
     * @return byte[]  一个i帧
     * @throws IOException
     */
    private static byte[] getByteFromFfmpeg(InputStream inputStream) throws Exception {
        byte[] picBy = null;
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);
        grabber.start();
        Frame frame = null;
        int cnt = 0, frameLen = grabber.getFrameNumber();
        while (true) {
            Frame tmpframe = grabber.grabFrame();
            if (tmpframe == null || tmpframe.image == null) {
                continue;
            }
            if (tmpframe.keyFrame) {
                frame = tmpframe;
                break;
            }
//            cnt++;
//            if (cnt > 30){
//                break;
//            }

        }
        if (frame != null) {
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage bufferedImage = converter.getBufferedImage(frame);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean flag = ImageIO.write(bufferedImage, "jpg", out);
            if (flag) {
                picBy = out.toByteArray();
            }
        }
        grabber.stop();
        grabber.close();
        return picBy;
    }

    /**
     * MAS节点地址请求
     *
     * @param masMasterIp
     * @param masMasterPort
     * @return mas node的ip及端口
     */
    private static String getMasNode(String masMasterIp, String masMasterPort) {
        WebsocketClient websocketClient = null;
        JSONObject param = null;
        String masNodeIp = null;
        String wsAddr = "ws://" + masMasterIp + ":" + masMasterPort;
        try {
            websocketClient = new WebsocketClient(new URI(wsAddr), new Draft_6455());
            websocketClient.connectBlocking();
            param = new JSONObject();
            param.put("request", "nodeAddr");
            param.put("seq", 0);
            param.put("nodeType", "GB28181");
            websocketClient.send(param.toString());
            Thread.sleep(200L);
            String response = websocketClient.getResponse();
            System.out.println("收到nodeAddr服务端消息:" + response);
            if (!StringUtils.isEmpty(response)) {
                JSONObject jsonObject = JSONObject.parseObject(response);
                masNodeIp = jsonObject.get("nodeIp") + ":" + jsonObject.get("port");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                websocketClient.closeBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return masNodeIp;
    }

    public static void main(String[] args) throws Exception {
//        String base64String = MasSnapshotUtil.getMasSnapshotBase64("vas://name=name&psw=psw&srvip=172.16.0.80&srvport=9080&devid=43010000001320000001&");
//        System.out.println("base64:"+base64String);
    }

}
