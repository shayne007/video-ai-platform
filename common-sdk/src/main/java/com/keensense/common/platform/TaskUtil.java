package com.keensense.common.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.platform.bo.video.CrowdDensityTaskBo;
import com.keensense.common.platform.bo.video.ObjextTaskBo;
import com.keensense.common.platform.constant.StandardUrlConstant;
import com.keensense.common.platform.constant.WebserviceConstant;
import com.keensense.common.util.HttpU2sGetUtil;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: zengyc
 * @Description: 任务类见任务管理文档
 * @Date: Created in 9:32 2019/9/26
 * @Version v0.1
 */
public class TaskUtil {
    /**
     * 添加任务接口
     *
     * @param bo
     * @return
     */
    public static String addVideoObjextTask(String requestUrl, ObjextTaskBo bo) {

        JSONObject requestParams = new JSONObject();
        requestParams.put("url", bo.getUrl());
        requestParams.put("serialnumber", bo.getSerialnumber());//任务序列号
        requestParams.put("type", bo.getType().getValue());
        requestParams.put("name", bo.getName());
        requestParams.put("userId", bo.getUserId());
        requestParams.put("cameraId", bo.getCameraId());
        requestParams.put("startTime", bo.getStartTime());
        requestParams.put("endTime", bo.getEndTime());
        requestParams.put("entryTime", bo.getEntryTime());
        requestParams.put("enablePartial", bo.getEnablePartial());
        if (bo.getScene() != null) {
            requestParams.put("scene", bo.getScene());
        }
        if (bo.getInterested() != null) {
            requestParams.put("isInterested", bo.getInterested());
        }
        if (StringUtils.isNotEmpty(bo.getTripWires())) {
            requestParams.put("tripWires", bo.getTripWires());
        }
        if (StringUtils.isNotEmpty(bo.getUdrVertices())) {
            requestParams.put("udrVertices", bo.getUdrVertices());
        }
        if (StringUtils.isNotEmpty(bo.getStartTime())) {
            requestParams.put("taskType", 3);
        } else {
            requestParams.put("taskType", bo.getTaskType().getValue());
        }
        requestParams.put("detectionFrameSkipInterval", 24);
        requestParams.put("pushFrameMaxWaitTime", 0);
        requestParams.put("detectionScaleFactor", 1);
        requestParams.put("enableDensityMapOutput", true);
        requestParams.put("heatmapWeight", 0.4);
        requestParams.put("scene", bo.getScene());
        requestParams.put("enableIndependentFaceSnap", bo.getEnableIndependentFaceSnap());
        requestParams.put("enableBikeToHuman", bo.getEnableBikeToHuman());
        requestParams.put("sensitivity", bo.getSensitivity());
        requestUrl += WebserviceConstant.ADD_VIVEO_OBJECT_TASK;
        String addVideoObjextTaskReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return addVideoObjextTaskReponse;
    }

    /**
     * 添加任务接口
     *
     * @param bo
     * @return
     */
    public static String addCrowdDensityVideoObjextTask(String requestUrl, CrowdDensityTaskBo bo) {

        JSONObject requestParams = new JSONObject();
        requestParams.put("url", bo.getUrl());
        requestParams.put("serialnumber", bo.getSerialnumber());//任务序列号
        requestParams.put("type", bo.getType().getValue());
        requestParams.put("name", bo.getName());
        requestParams.put("userId", bo.getUserId());
        requestParams.put("cameraId", bo.getCameraId());
        requestParams.put("entryTime", bo.getEntryTime());
        requestParams.put("enablePartial", bo.getEnablePartial());
        if (bo.getScene() != null) {
            requestParams.put("scene", bo.getScene());
        }
        if (bo.getInterested() != null) {
            requestParams.put("isInterested", bo.getInterested());
        }
        if (StringUtils.isNotEmpty(bo.getTripWires())) {
            requestParams.put("tripWires", bo.getTripWires());
        }
        if (StringUtils.isNotEmpty(bo.getUdrVertices())) {
            requestParams.put("udrVertices", bo.getUdrVertices());
        }
        requestParams.put("taskType", bo.getTaskType().getValue());
        requestParams.put("detectionFrameSkipInterval", bo.getDetectionFrameSkipInterval());
        requestParams.put("pushFrameMaxWaitTime", bo.getPushFrameMaxWaitTime());
        requestParams.put("detectionScaleFactor", bo.getDetectionScaleFactor());
        requestParams.put("enableDensityMapOutput", bo.isEnableDensityMapOutput());
        requestParams.put("heatmapWeight", bo.getHeatmapWeight());
        requestUrl += WebserviceConstant.ADD_VIVEO_OBJECT_TASK;
        String addVideoObjextTaskReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return addVideoObjextTaskReponse;
    }

    /**
     * 查询联网录像分析任务列表接口
     *
     * @param bo
     * @return
     */
    public static String getVideoObjectTaskList(String requestUrl, ObjextTaskBo bo) {
        requestUrl += WebserviceConstant.GET_VIDEO_OBJECTTASK_LIST;
        JSONObject requestParams = new JSONObject();
        requestParams.put("serialnumber", bo.getSerialnumber());
        requestParams.put("startTime", bo.getStartTime());
        requestParams.put("endTime", bo.getEndTime());
        requestParams.put("type", "objext");
        requestParams.put("userId", bo.getUserId());
        requestParams.put("status", bo.getStatus());
        requestParams.put("pageNo", bo.getPageNo());
        requestParams.put("pageSize", bo.getPageSize());
        String getVideoObjectTaskListReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return getVideoObjectTaskListReponse;
    }


    /**
     * 查询联网录像分析任务列表接口
     *
     * @param bo
     * @return
     */
    public static String getAllVideoObjectTaskList(String requestUrl, ObjextTaskBo bo) {
        requestUrl += WebserviceConstant.GET_ALL_VIDEO_OBJECTTASK_LIST;
        JSONObject requestParams = new JSONObject();
        requestParams.put("serialnumber", bo.getSerialnumber());
        requestParams.put("startTime", bo.getStartTime());
        requestParams.put("endTime", bo.getEndTime());
//        requestParams.put("type", "objext");
        requestParams.put("userId", bo.getUserId());
        requestParams.put("status", bo.getStatus());
        requestParams.put("pageNo", bo.getPageNo());
        requestParams.put("pageSize", bo.getPageSize());
        String getVideoObjectTaskListReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return getVideoObjectTaskListReponse;
    }


    /**
     * 删除任务接口
     *
     * @param bo
     * @return
     */
    public static String deleteVideoObjectTask(String requestUrl, ObjextTaskBo bo) {
        requestUrl += WebserviceConstant.DELETE_TASK_TZ;
        JSONObject requestParams = new JSONObject();
        requestParams.put("serialnumber", bo.getSerialnumber());//任务序列号
        String addVideoObjextTaskReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return addVideoObjextTaskReponse;
    }

    /**
     * 暂停实时任务
     *
     * @param requestUrl
     * @param bo
     * @return
     */
    public static String pauseVideoObjectTask(String requestUrl, ObjextTaskBo bo) {
        requestUrl += WebserviceConstant.PAUSE_VIDEO_OBJECTTASK;
        JSONObject requestParams = new JSONObject();
        requestParams.put("serialnumber", bo.getSerialnumber());//任务序列号
        String addVideoObjextTaskReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return addVideoObjextTaskReponse;
    }

    /**
     * 继续实时任务接口
     *
     * @param bo
     * @return
     */
    public static String continueVideoObjectTask(String requestUrl, ObjextTaskBo bo) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("serialnumber", bo.getSerialnumber());
        requestUrl += WebserviceConstant.CONTINUE_VIDEO_OBJECTTASK;
        String addVideoObjextTaskReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return addVideoObjextTaskReponse;
    }

    /**
     * 更新画感兴趣区域
     *
     * @param bo
     * @return
     */
    public static String updateVideoObjectTask(String requestUrl, ObjextTaskBo bo) {
        requestUrl += WebserviceConstant.UPDATE_VIDEO_OBJEXT_TASK;
        JSONObject requestParams = new JSONObject();
        requestParams.put("serialnumber", bo.getSerialnumber());
        requestParams.put("scene", bo.getScene());
        requestParams.put("isInterested", bo.getInterested());
        requestParams.put("udrVertices", bo.getUdrVertices());
        requestParams.put("url", bo.getUrl());
        requestParams.put("tripWires", bo.getTripWires());
        requestParams.put("enablePartial", bo.getEnablePartial());
        requestParams.put("scene", bo.getScene());
        requestParams.put("enableIndependentFaceSnap", bo.getEnableIndependentFaceSnap());
        requestParams.put("enableBikeToHuman", bo.getEnableBikeToHuman());
        String addVideoObjextTaskReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return addVideoObjextTaskReponse;
    }

    /**
     * 查询集群信息
     *
     * @param params
     * @return
     */
    public static String getVsdSlaveList(String requestUrl, ObjextTaskBo params) {


        System.out.println("requestUrl: " + requestUrl);
        JSONObject requestParams = new JSONObject();
        requestParams.put("pageNo", params.getPageNo());//任务序列号
        requestParams.put("pageSize", params.getPageSize());//任务序列号
        String response = "";
        if (StringUtils.isNotEmpty(params.getDeviceId())) {
            requestUrl += StandardUrlConstant.VSD_SLAVE_BY_ID;
            requestParams.put("id", params.getDeviceId());
            response = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
            com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
            jsonArray.add(jsonObject.get("data"));
            jsonObject.put("salves", jsonArray);
            jsonObject.put("totalCount", 1);
            response = jsonObject.toJSONString();
        } else {
            requestUrl += StandardUrlConstant.VSD_SLAVE;
            response = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        }
        return response;
    }

}
