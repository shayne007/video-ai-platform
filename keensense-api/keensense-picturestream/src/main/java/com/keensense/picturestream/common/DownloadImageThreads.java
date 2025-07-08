package com.keensense.picturestream.common;

import com.keensense.common.config.SpringContext;
import com.keensense.picturestream.config.NacosConfig;
import com.keensense.picturestream.entity.PictureInfo;
import com.keensense.picturestream.util.IDUtil;
import com.keensense.picturestream.util.ImageBaseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.PatternMatchUtils;
import sun.net.www.protocol.ftp.FtpURLConnection;

@Slf4j
public class DownloadImageThreads {

    private DownloadImageThreads(){}

    private static ExecutorService service = null;
    private static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    public static void initCapacity(int capacity) {
        if (0 >= capacity) {
            capacity = 40;
        }
        service = Executors.newFixedThreadPool(capacity);
    }

    public static void downloadResource(final PictureInfo picInfo) {
        if (StringUtils.isEmpty(picInfo.getPicUrl())) {
            picInfo.setStatus(PictureInfo.STATUS_DOWNLOAD_FAIL);
            log.info(String.format("[%s] download picture faield. the picture url is empty.", picInfo.getExtendId()));
            ResultSendThreads.sendError(picInfo);
            return;
        }

        if (PictureInfo.STATUS_INIT != picInfo.getStatus()) {
            return;
        }

        if (PatternMatchUtils.simpleMatch(picInfo.getPicUrl(), "^(http|ftp).*")) {
            picInfo.setStatus(PictureInfo.STATUS_DOWNLOADING);
             service.execute(()->{
                Thread.currentThread().setName(IDUtil.threadName("downloadPic"));
                downloadImage(picInfo);
            });
        } else {
            picInfo.setStatus(PictureInfo.STATUS_UNKOWN_URL);
            log.info(String.format("[%s] download picture failed. unknown picUrl pattern.", picInfo.getExtendId()));
            ResultSendThreads.sendError(picInfo);
        }
    }

    private static void downloadImage(PictureInfo picInfo) {
        byte[] picBy = null;
        URLConnection conn = null;
        InputStream inputStream = null;
        try {
            log.info(String.format("[%s] start download picture [%s].", picInfo.getExtendId(), picInfo.getPicUrl()));

            URL url = new URL(picInfo.getPicUrl());
            conn = url.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            conn.setReadTimeout(nacosConfig.getDownloadReadTimeout() * 1000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            inputStream = conn.getInputStream();
            if (null != inputStream) {
                picBy = ImageBaseUtil.input2byte(inputStream);
            }
        } catch (Exception e) {
            log.error("http/ftp download error ", e);
            ResultSendThreads.sendError(picInfo);
        } finally {
            if (null != conn) {
                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection conn1 = (HttpURLConnection) conn;
                    conn1.disconnect();
                } else if (conn instanceof FtpURLConnection) {
                    FtpURLConnection conn1 = (FtpURLConnection) conn;
                    conn1.close();
                }
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream close failed", e);
                }
            }
        }

        if (null == picBy) {
            // 下载失败
            picInfo.setStatus(PictureInfo.STATUS_DOWNLOAD_FAIL);
            log.info(String.format("download picture failed, url = [%s]",  picInfo.getPicUrl()));
        } else {
            // 下载成功
            picInfo.setStatus(PictureInfo.STATUS_DOWNLOAD_SUCC);
            picInfo.setPicBase64(new String(Base64.encode(picBy)));
            log.info(String.format("download picture success,url =  [%s]",  picInfo.getPicUrl()));
            if (picInfo.getRecogTypeList().contains(PictureInfo.RECOG_TYPE_OBJEXT)) {
                DownloadImageQueue.putObjext(picInfo);
            } else if (picInfo.getRecogTypeList().contains(PictureInfo.RECOG_TYPE_THIRD_VLPR)) {
                DownloadImageQueue.putVlpr(picInfo);
            } else if (picInfo.getRecogTypeList().contains(PictureInfo.RECOG_TYPE_FACE)) {
                DownloadImageQueue.putFace(picInfo);
            }
        }
    }
}
