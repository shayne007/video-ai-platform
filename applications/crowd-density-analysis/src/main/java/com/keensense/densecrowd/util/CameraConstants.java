package com.keensense.densecrowd.util;

import com.keensense.common.exception.VideoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.util.*;

/**
 * 监控点类别常量类
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2019/3/14
 */
@Slf4j
public class CameraConstants {
    private CameraConstants() {
    }

    public static class CameraType {
        private CameraType() {
        }

        /**
         * Vas点位类型
         */
        public static final long VAS = 1L;
        /**
         * IPC点位类型
         */
        public static final long RTSP = 2L;
        /**
         * 抓拍相机类型
         */
        public static final long CAPTURE = 3L;
        /**
         * 卡口类型
         */
        public static final long Gate = 4L;
    }

    public static class CameraUrlPrefix {
        private CameraUrlPrefix() {
        }

        /**
         * 协议Vas前缀
         */
        public static final String PREFIX_VAS = "vas";

        /**
         * 协议rtsp或者rtmp
         */
        public static final String PREFIX_RT = "rt";
    }

    /**
     * 任务启动状态
     */
    public static class CameraStatus {
        private CameraStatus() {
        }

        /**
         * 监控点未启动状态
         */
        public static final long STOP = 0L;
        /**
         * 监控点启动状态
         */
        public static final long START = 1L;
    }

    //默认监控点Id
    public static final String DEFAULT_ID = "1234567891011";


    /**
     * 流获取快照需要转换一个地址
     * url=vas://srvip=192.168.0.86|srvport=8350|devid=43010000001320000001|
     * vas地址: vas://name=admin&psw=1234&srvip=192.168.0.86&srvport=8350&devid=43010000001320000001&
     * rtsp地址：
     *
     * @param ws2ServerIp   实时流地址
     * @param ws2ServerPort 实时流端口
     * @param url           url地址
     * @param type          playbackPlatform,real_Platform
     * @return
     */
    public static String transUrl(String ws2ServerIp, String ws2ServerPort, String url, String type) {
        String newUrl = "";
        if (url == null) {
            throw new VideoException("点位不能为空");
        }
        if (url.indexOf("vas") == 0) {
            if (!checkVas(url)) {
                log.error("点位信息有误,请确认点位格式及内容:" + url);
                throw new VideoException("点位信息有误,请确认点位格式及内容");
            }
            String resultUrl = url.trim();
            String[] data = resultUrl.split("&");
            Map<String, String> urls = new HashMap<>();
            for (String d : data) {
                String[] s = d.split("=");
                urls.put(s[0], s[1]);
            }
            newUrl = "ws://" + ws2ServerIp + ":" + ws2ServerPort + "/?nodeType=GB28181&type=" + type + "&channel=" + urls.get("devid") + "&";
        } else {
            newUrl = url;
        }
        return newUrl;
    }

    /**
     * 检测是否为Vas格式数据
     *
     * @param url
     * @return 无效为false
     */
    public static boolean checkVas(String url) {
        if (PatternMatchUtils.simpleMatch(url, "^vas://name=.+&psw=.+&srvip=.+&srvport=\\d+&devid=.+&.*$")) {
            return false;
        } else {
            return true;
        }
    }
}
