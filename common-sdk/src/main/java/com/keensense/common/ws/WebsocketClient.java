package com.keensense.common.ws;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: WebsocketClient
 * @Description: ws客户端处理类
 * @Author: cuiss
 * @CreateDate: 2019/11/21 15:55
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class WebsocketClient extends WebSocketClient {

    private List<byte[]> masByteList = new ArrayList<>();

    @Getter
    private String response;

    public WebsocketClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("开始建立链接...");
    }

    @Override
    public void onMessage(String message) {
//        System.out.print("hello ,message!");
        log.info("检测到服务器请求...message： " + message);
        this.response = message;
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("客户端已关闭!");
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        log.info("客户端发生错误,即将关闭!");
    }

    @Override
    public void onMessage(ByteBuffer byteBuffer) {
        byte[] bytes = convert(byteBuffer);
        //System.out.println("获取视频流长度："+bytes.length);
        masByteList.add(bytes);
    }

    public static byte[] convert(ByteBuffer byteBuffer) {
        int len = byteBuffer.limit() - byteBuffer.position();
        byte[] bytes = new byte[len];

        if (byteBuffer.isReadOnly()) {
            return null;
        } else {
            byteBuffer.get(bytes);
        }
        return bytes;
    }

    public List<byte[]> getMasByteList() {
        return masByteList;
    }

}
