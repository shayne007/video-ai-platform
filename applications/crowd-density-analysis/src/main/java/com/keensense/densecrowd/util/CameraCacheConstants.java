package com.keensense.densecrowd.util;

import java.util.*;

/**
 * 监控点缓存常量类
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2019/4/2
 */
public class CameraCacheConstants {

    public static final String KEY_CACHE_ALL = "camera_all_";
    public static final String KEY_CACHE_VAS = "camera_vas_";
    public static final String KEY_CACHE_IPC = "camera_ipc_";
    public static final String KEY_CACHE_CAPTURE = "camera_capture_";
    public static final String KEY_CACHE_GATE = "camera_gate_";

    private CameraCacheConstants() {
    }

    /**
     * 根据点位类型获取缓存的key
     *
     * @param cameraType 监控点类型
     * @return
     */
    public static Map<String, String> getCameraCacheKey(String cameraType) {
        Map<String, String> retMap = new HashMap<>();
        Date d = new Date();
        String firstDateOfMonthStr = DateTimeUtils.formatDate(d, "yyyyMMdd");
        String prevfirstDateOfMonthStr = DateTimeUtils.getSomeDayYMD(d, -1);

        String key = KEY_CACHE_ALL + firstDateOfMonthStr;
        String prevkey = KEY_CACHE_ALL + prevfirstDateOfMonthStr;

        if (StringUtils.isNotEmptyString(cameraType)) {
            if (String.valueOf(CameraConstants.CameraType.VAS).equals(cameraType)) {
                //vas
                key = KEY_CACHE_VAS + firstDateOfMonthStr;
                prevkey = KEY_CACHE_VAS + prevfirstDateOfMonthStr;
            } else if (String.valueOf(CameraConstants.CameraType.RTSP).equals(cameraType)) {
                //ipc
                key = KEY_CACHE_IPC + firstDateOfMonthStr;
                prevkey = KEY_CACHE_IPC + prevfirstDateOfMonthStr;
            } else if (String.valueOf(CameraConstants.CameraType.CAPTURE).equals(cameraType)) {
                //抓怕机
                key = KEY_CACHE_CAPTURE + firstDateOfMonthStr;
                prevkey = KEY_CACHE_CAPTURE + prevfirstDateOfMonthStr;
            }else if (String.valueOf(CameraConstants.CameraType.Gate).equals(cameraType)) {
                //卡口
                key = KEY_CACHE_GATE + firstDateOfMonthStr;
                prevkey = KEY_CACHE_GATE + prevfirstDateOfMonthStr;
            }
        }
        retMap.put("key", key);
        retMap.put("prevkey", prevkey);

        return retMap;
    }

    /**
     * 如果有新增、编辑和删除监控点的时候，需要清除缓存
     *
     * @param cameraType 监控点类型
     */
    public static void cleanCameraCacheByCameraType(String cameraType) {
        Date d = new Date();
        String firstDateOfMonthStr = DateTimeUtils.formatDate(d, "yyyyMMdd");
        String prevfirstDateOfMonthStr = DateTimeUtils.getSomeDayYMD(d, -1);
        String key = KEY_CACHE_ALL + firstDateOfMonthStr;
        String prevkey = KEY_CACHE_ALL + prevfirstDateOfMonthStr;
        EhcacheUtils.removeItem(key);
        EhcacheUtils.removeItem(prevkey);

        if (String.valueOf(CameraConstants.CameraType.VAS).equals(cameraType)) {
            String keyVas = KEY_CACHE_VAS + firstDateOfMonthStr;
            String prevkeyVas = KEY_CACHE_VAS + prevfirstDateOfMonthStr;
            EhcacheUtils.removeItem(keyVas);
            EhcacheUtils.removeItem(prevkeyVas);
        } else if (String.valueOf(CameraConstants.CameraType.RTSP).equals(cameraType)) {
            String keyIpc = KEY_CACHE_IPC + firstDateOfMonthStr;
            String prevkeyIpc = KEY_CACHE_IPC + prevfirstDateOfMonthStr;
            EhcacheUtils.removeItem(keyIpc);
            EhcacheUtils.removeItem(prevkeyIpc);
        } else if (String.valueOf(CameraConstants.CameraType.CAPTURE).equals(cameraType)) {
            String keyCapture = KEY_CACHE_CAPTURE + firstDateOfMonthStr;
            String prevkeyCapture = KEY_CACHE_CAPTURE + prevfirstDateOfMonthStr;
            EhcacheUtils.removeItem(keyCapture);
            EhcacheUtils.removeItem(prevkeyCapture);
        }
    }

}
