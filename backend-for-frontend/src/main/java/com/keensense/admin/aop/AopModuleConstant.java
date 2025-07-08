package com.keensense.admin.aop;

import java.util.HashMap;
import java.util.Map;

public class AopModuleConstant {
    private AopModuleConstant() {
    }

    private static Map<String, String> URLMAP = new HashMap<>();
    private static final String[] URL = {
            "/u2s/reline/addInterestSettings",
            "/u2s/relinevideo/addRealVideoTask",
            "/u2s/onlineCameraManager/addInterestSettingsAndTask",
            "/u2s/ipcManager/addInterestSettingsAndTask",
            "/u2s/captureManage/startPictureRealTimeList",
            "/u2s/reline/deleteVideoList",
            "/u2s/relinevideo/deleteVideoList",
            "/u2s/onlineCameraManager/batchDelete",
            "/u2s/ipcManager/deleteIpc",
            "/u2s/captureManage/deletePictureRealTimeList",
            "/u2s/onlineCameraManager/updateCamera",
            "/u2s/ipcManager/updateCamera",
            "/u2s/captureManage/updateCamera",
            "/u2s/onlineCameraManager/batchStopRealtimeTask",
            "/u2s/ipcManager/batchStopRealtimeTask",
            "/u2s/captureManage/stopPictureRealTimeList",
            "/u2s/reline/addCameraMediaChucks",
            "/u2s/reline/deleteVideoList",
            "/u2s/twiceImageQuery/submitImageQueryTaskMem",
            "/u2s/onlineCameraManager/addOnlineCamera",
            "/u2s/ipcManager/submitIpc",
            "/u2s/captureManage/submitCapture"


    };
    /**
     * 操作类型 1查询 2新增 3修改 4删除 5登录 6退出 7其他
     */
    public static final String[] NAME = {
            "添加任务,2",
            "添加任务,2",
            "添加任务,2",
            "添加任务,2",
            "添加任务,2",
            "删除任务,4" ,
            "删除任务,4" ,
            "删除任务,4" ,
            "删除任务,4" ,
            "删除任务,4" ,
            "更新监控点信息,3" ,
            "更新监控点信息,3" ,
            "更新监控点信息,3" ,
            "停止实时任务,3",
            "停止实时任务,3",
            "停止实时任务,3",
            "上传离线视频,2",
            "删除离线视频,3",
            "以图搜图查询,1",
            "新增监控点位,2",
            "新增监控点位,2",
            "新增监控点位,2"
    };

    static {
        for (int i = 0; i < URL.length; i++) {
            URLMAP.put(URL[i], NAME[i]);
        }
    }

    public static Map<String, String> getUrlMap() {
        return URLMAP;
    }
}
