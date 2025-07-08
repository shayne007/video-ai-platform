package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.constants.CameraConstants;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.request.ResultUpdateRequest;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.sys.ICfgMemPropsService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.ICtrlUnitFileService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.IdUtils;
import com.keensense.admin.util.InterestUtil;
import com.keensense.admin.util.PropertiesUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.TaskParamVo;
import com.keensense.admin.vo.VsdTaskVo;
import com.keensense.common.exception.VideoException;
import com.keensense.common.platform.enums.TaskTypeEnums;
import com.keensense.common.platform.enums.TypeEnums;
import com.keensense.common.util.R;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("vsdTaskService")
public class VsdTaskServiceImpl implements IVsdTaskService {

    @Resource
    private ICtrlUnitFileService ctrlUnitFileService;

    @Resource
    private ICameraService cameraService;

    @Resource
    private IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    private VideoObjextTaskService videoObjextTaskService;

    @Autowired
    private ICfgMemPropsService cfgMemPropsService;

    /**
     * 画感兴趣区域格式封装
     *
     * @param taskParam
     * @param udrSettingJsonObject
     */
    private void initInterestParam(TaskParamVo taskParam, JSONObject udrSettingJsonObject) {
        List<JSONObject> udrVerticesList = new LinkedList<>();
        // 感兴趣区域节点
        String featurFollowarea = taskParam.getFeaturFollowarea();
        if (StringUtils.isNotEmptyString(featurFollowarea)) {
            String[] featureArr = featurFollowarea.split(";");
            if (featureArr != null && featureArr.length > 0) {
                String[] verticesNumArray = null;
                String[] verticesArr = null;
                Double minRate = 0.01;
                Double maxRate = 0.98;
                try {
                    String minRateStr = PropertiesUtil.getParameterPackey("min.rate");
                    String maxRateStr = PropertiesUtil.getParameterPackey("max.rate");
                    if (StringUtils.isNotEmptyString(minRateStr)) {
                        minRate = Double.valueOf(minRateStr);
                    }
                    if (StringUtils.isNotEmptyString(maxRateStr)) {
                        maxRate = Double.valueOf(maxRateStr);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                // 每个区域循环
                for (int i = 0; i < featureArr.length; i++) {
                    JSONObject featureJSONObject = new JSONObject();
                    // 节点数量
                    verticesNumArray = featureArr[i].split("\\|");
                    String verticesStr = featureArr[i].replace("|", ",");
                    verticesArr = verticesStr.split(",");
                    // 点位信息
                    if (verticesArr != null) {
                        List<Double> verticesList = new LinkedList<>();
                        for (int j = 0; j < verticesArr.length; j++) {
                            if (Double.valueOf(verticesArr[j]) > maxRate) {
                                verticesList.add(Double.valueOf(1));
                            } else if (Double.valueOf(verticesArr[j]) < minRate) {
                                verticesList.add(Double.valueOf(0));
                            } else {
                                verticesList.add(Double.valueOf(verticesArr[j]));
                            }
                        }
                        featureJSONObject.put("vertices", verticesList);
                    }
                    // 当前兴趣区域节点数量
                    if (null != verticesNumArray) {
                        featureJSONObject.put("verticesNum", verticesNumArray.length);
                    }
                    udrVerticesList.add(featureJSONObject);
                }
            }
            // 数量
            int udrNum = featureArr.length;
            // 是否是感兴趣区域
            boolean isInterested = false;
            if ("1".equals(taskParam.getInterest())) {
                isInterested = true;
            }
            udrSettingJsonObject.put("isInterested", isInterested);
            udrSettingJsonObject.put("udrNum", udrNum);
            udrSettingJsonObject.put("udrVertices", udrVerticesList);
        }
    }

    /**
     * 初始化封装跨线参数
     *
     * @param tripwiresParam , 格式例如：[0,0.13,0.14,0.47,0.81]
     *                       "tripWireType":跨线的方向类别
     *                       "stPointX":起点X 坐标
     *                       "stPointY":起点Y 坐标
     *                       "edPointX":终点X 坐标
     *                       "edPointY":终点Y 坐标
     * @return 封装好的JSON格式的跨线参数
     */
    private String initTripwireParam(String tripwiresParam) {
        JSONObject eventConfigJson = new JSONObject();
        JSONObject tripwiresSettingJson = new JSONObject();

        if (StringUtils.isNotEmpty(tripwiresParam)) {
            // 支持画多个线
            String[] tripwiresArry = tripwiresParam.split(";");
            com.alibaba.fastjson.JSONArray tripwiresArryJson = new com.alibaba.fastjson.JSONArray();
            for (int i = 0; i < tripwiresArry.length; i++) {
                JSONObject tripwires = new JSONObject();
                String[] tripwiresPoint = tripwiresArry[i].split(",");
                if (tripwiresPoint.length >= 5) {
                    tripwires.put("tripWireType", Double.parseDouble(tripwiresPoint[0].equals("2") ? "0" : tripwiresPoint[0]));
                    tripwires.put("stPointX", Double.parseDouble(tripwiresPoint[1]));
                    tripwires.put("stPointY", Double.parseDouble(tripwiresPoint[2]));
                    tripwires.put("edPointX", Double.parseDouble(tripwiresPoint[3]));
                    tripwires.put("edPointY", Double.parseDouble(tripwiresPoint[4]));
                }
                tripwiresArryJson.add(i, tripwires);
            }

            tripwiresSettingJson.put("tripwireNum", tripwiresArry.length);
            tripwiresSettingJson.put("wireRelationship", 0);
            tripwiresSettingJson.put("tripwires", tripwiresArryJson);

            eventConfigJson.put("tripwiresSetting", tripwiresSettingJson);
            eventConfigJson.put("trafficJamThreshold", -1);
            eventConfigJson.put("crowdDetThreshold", -1);
            eventConfigJson.put("leftOverItemSensitivity", 0);
            eventConfigJson.put("leftOverItemDuration", 0);
        }
        return eventConfigJson.toString();
    }

    @Override
    public R startRealTimeTask(Long cameraId, Integer interestFlag, String interestParam, Integer chooseType,
                               String tripwires, Long userId, Integer overlineType, Integer enablePartial,
                               Integer scene, String enableIndependentFaceSnap, String enableBikeToHuman) {
        R result = R.ok();
        Camera camera = cameraService.selectCameraById(cameraId);
        Map<String, Object> paramMap = new HashedMap();
        Long id = System.currentTimeMillis();
        String url = camera.getUrl();

        //任务类型
        paramMap.put("type", VideoTaskConstant.Type.OBJEXT);

        paramMap.put("cameraId", cameraId + "");
        //异常行为检测标志
        if (null != overlineType) {
            paramMap.put("overlineType", overlineType);
        }


        if (camera != null) {
            paramMap.put("name", camera.getName());
            //设备ID
            paramMap.put("deviceId", camera.getExtcameraid());
        }
        if (enablePartial == null) {
            enablePartial = 0;
        }
        boolean tag = enablePartial == 1 ? true : false;
        Long cameratype = camera.getCameratype();
        String taskUserId = "";
        String newUrl = "";
        Integer fileFromType = null;
        String ws2ServerIp = cfgMemPropsService.getWs2ServerIp();
        String ws2ServerPort = cfgMemPropsService.getWs2ServerPort();
        if (cameratype == CameraConstants.CameraType.VAS) {
            taskUserId = VideoTaskConstant.USER_ID.REALTIME_VIDEO;
            fileFromType = VideoTaskConstant.FROM_TYPE.REAL;
            if (!CameraConstants.checkVas(url)) {
                return R.error("点位信息有误,请确认点位格式及内容");
            }
            String[] data = url.trim().split("&");
            Map<String, String> urls = new HashMap<>();
            for (String d : data) {
                String[] s = d.split("=");
                urls.put(s[0], s[1]);
            }
//            if (camera.getName().indexOf("vastest") > -1) {
//                newUrl = url;
//            } else {
//                newUrl = "ws://" + ws2ServerIp + ":" + ws2ServerPort + "/?nodeType=GB28181&type=real_Platform&channel=" + urls.get("devid") + "&tagServerIp=" + ws2TagServer + "&tagServerPort=" + ws2TagPort;
//            }
            newUrl = CameraConstants.transUrl(ws2ServerIp, ws2ServerPort, url, "real_Platform", tag);
        } else if (cameratype == CameraConstants.CameraType.RTSP) {
            //视频路径
//            newUrl = "ws://" + ws2TagServer + ":" + ws2TagPort + "/?type=ffmpeg_rtsp&channel=" + url;
            newUrl = CameraConstants.transUrl(ws2ServerIp, ws2ServerPort, url, "ffmpeg_rtsp", tag);
            taskUserId = VideoTaskConstant.USER_ID.IPC;
            fileFromType = VideoTaskConstant.FROM_TYPE.IPC;
        }
        String serialnumber = vsdTaskRelationService.getSerialnumber();
        boolean isInterested = false;
        if (interestFlag != null && 1 == interestFlag) {
            isInterested = true;
        }
        //任务序列号
        paramMap.put("serialnumber", serialnumber);
        String udrVertices = InterestUtil.initInterestParam(interestParam);
        paramMap.put("url", newUrl);
        paramMap.put("userId", taskUserId);


        paramMap.put("tripWires", InterestUtil.initTripwires(tripwires));
        paramMap.put("taskType", TaskTypeEnums.REAL_LINE.getValue());
        paramMap.put("udrVertices", udrVertices);
        paramMap.put("isInterested", isInterested);
        paramMap.put("createuser", userId);
        paramMap.put("enablePartial", enablePartial);
        paramMap.put("scene", scene);
        paramMap.put("enableIndependentFaceSnap", enableIndependentFaceSnap);
        paramMap.put("enableBikeToHuman", enableBikeToHuman);
        VsdTaskRelation relation = vsdTaskRelationService.queryVsdTaskRelationByCameraFileId(cameraId.toString(), taskUserId);
        //已添加过任务，重启
        if (null != relation) {
            id = relation.getId();
            paramMap.put("serialnumber", relation.getSerialnumber());
            String continueJson = continueTask(relation, paramMap);
            Var continueResVar = Var.fromJson(continueJson);

            String retCode = continueResVar.getString("ret");
            String retDesc = continueResVar.getString("desc");
            // -1：任务已经重启失败 -2 启动超路数
            if ("0".equals(retCode) || "1".equals(retCode) || (retDesc != null && retDesc.indexOf("任务已启动") > -1)) {
                this.updateVsdTaskRelations(relation.getId(), VideoTaskConstant.STATUS.RUNNING, userId, camera.getName(), url, overlineType, enablePartial);
                return R.ok();
            } else {
                return R.error(retDesc);
            }
        }

        paramMap.put("fromType", fileFromType);
        String resultJson = videoObjextTaskService.addVsdTaskService(paramMap, true);
        Var addResVar = Var.fromJson(resultJson);
        String retCode = addResVar.getString("ret");
        String retDesc = addResVar.getString("desc");
        if ("0".equals(retCode)) {
            return result;
        } else {
            return R.error(retDesc);
        }
    }

    /**
     * 监控点任务重启（重启任务后更新感兴趣区域）
     *
     * @param relation
     * @param paramMap
     * @return
     */
    private String continueTask(VsdTaskRelation relation, Map<String, Object> paramMap) {
        R result = R.ok();
        String serialnumber = relation.getSerialnumber();
        //重启更新感兴趣区域
        //格式："0.8021309,0.21680216,0.81430745,0.5745258,0.9421613,0.40921408\", \"0.12937595,0.1897019,0.10350076,0.7208672,0.36834094,0.73170733,0.35920852,0.25203252\", \"0.6347032,0.28455284,0.5449011,0.6124661,0.6818874,0.76151764,0.7427702,0.75609756\"]");"[\"0.8021309,0.21680216,0.81430745,0.5745258,0.9421613,0.40921408\", \"0.12937595,0.1897019,0.10350076,0.7208672,0.36834094,0.73170733,0.35920852,0.25203252\", \"0.6347032,0.28455284,0.5449011,0.6124661,0.6818874,0.76151764,0.7427702,0.75609756"
        videoObjextTaskService.updateVsdTaskService(paramMap);
        //重启任务
        String resultJson = videoObjextTaskService.continueVsdTaskService(paramMap);
        return resultJson;
    }


    @Override
    public R retryTask(String serialnumber) {
        R r = R.ok();
        VsdTaskRelation relation = vsdTaskRelationService.queryVsdTaskRelationBySerialnumber(serialnumber);
        if (relation != null) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("serialnumber", serialnumber);
            videoObjextTaskService.retryTask(paramMap);
            updateVsdTaskRelations(relation.getId(), VideoTaskConstant.STATUS.RUNNING, null, null, null, null, null);
            return r;
        } else {
            return R.error("任务不存在");
        }
    }

    @Override
    public Map<String, Object> stopRealtimeTask(String serialnumber) {
        Map<String, Object> result = new HashedMap();
        result.put("retCode", "0");
        Map<String, Object> pauseMap = new HashMap<>();
        pauseMap.put("serialnumber", serialnumber);
        String resultJson = videoObjextTaskService.pauseVsdTaskService(pauseMap);
        Var updateRestVar = Var.fromJson(resultJson);
        if (resultJson == null) {
            return R.error();
        }
        String retCode = updateRestVar.getString("ret");
        String retDesc = updateRestVar.getString("desc");
        if ("0".equals(retCode) || "1".equals(retCode) || "7".equals(retCode) || "8".equals(retCode)) {
            VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
//            vsdTaskRelation.setC1(VideoTaskConstant.STATUS.FINISHED);//后续需要抛弃的字段
            vsdTaskRelation.setIsvalid(Integer.parseInt(VideoTaskConstant.STATUS.FINISHED));
            vsdTaskRelationService.update(vsdTaskRelation, new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber));
        } else {
            result.put("retCode", retCode);
        }
        result.put("retDesc", retDesc);
        return result;
    }


    /**
     * 根据id更新VsdTaskRelation
     *
     * @param relationId
     * @param isvalid
     */
    public boolean updateVsdTaskRelations(Long relationId, String isvalid, Long createUser, String taskName, String url,
                                          Integer overlineType, Integer enablePartial) {
        VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
        vsdTaskRelation.setId(relationId);
        vsdTaskRelation.setIsvalid(Integer.parseInt(isvalid));
        vsdTaskRelation.setTaskStatus(0);
        vsdTaskRelation.setCreateuser(createUser);
        vsdTaskRelation.setTaskName(taskName);
        vsdTaskRelation.setUrl(url);
        vsdTaskRelation.setOverlineType(overlineType);
        vsdTaskRelation.setEnablePartial(enablePartial);
        boolean ret = vsdTaskRelationService.updateById(vsdTaskRelation);
        return ret;
    }

    public boolean insertTaskRelation(Long id, String serialnumber, String fileId, Integer fromType, Long createUser,
                                      Integer taskStatus, Integer isvalid, String taskUserId, String taskName,
                                      String cameraId, String url, Integer overlineType, Integer enablePartial) {
        VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
        vsdTaskRelation.setIsvalid(isvalid);
        vsdTaskRelation.setTaskStatus(0);
        vsdTaskRelation.setCreateuser(createUser);
        vsdTaskRelation.setTaskName(taskName);
        vsdTaskRelation.setUrl(url);
        vsdTaskRelation.setOverlineType(overlineType);
        vsdTaskRelation.setEnablePartial(enablePartial);
        boolean ret = vsdTaskRelationService.updateById(vsdTaskRelation);
        return ret;
    }

    public Map<String, Object> selectAnalysisProgressByFileId(String fileId) {
        Map<String, Object> analysisProgress = new HashMap<>();
        VsdTaskRelation vsdTaskRelation = vsdTaskRelationService.queryVsdTaskRelationByCameraFileId(fileId, VideoTaskConstant.USER_ID.OFFLINE_VIDEO);
        if (vsdTaskRelation == null) {
            analysisProgress.put("analysisProgress", 0);
            analysisProgress.put("serialnumber", null);
            analysisProgress.put("taskStatus", null);
            analysisProgress.put("isdelete", true);
        } else {
            analysisProgress.put("analysisProgress", vsdTaskRelation.getTaskProgress());
            analysisProgress.put("serialnumber", vsdTaskRelation.getSerialnumber());
            analysisProgress.put("taskStatus", vsdTaskRelation.getTaskStatus());
        }
        return analysisProgress;
    }

    @Override
    public List<VsdTaskVo> taskResultHandle(List<VsdTaskVo> vsdTaskList) {
        if (ListUtil.isNotNull(vsdTaskList)) {
            Map<String, CameraVo> cameraIdRelNameMaps = new HashMap<>();
            for (VsdTaskVo task : vsdTaskList) {
                String cameraId = task.getCameraId();
                String cameraName = "";
                String regionName = "";
                // 获取监控点名称，如果已经查询过则不在查询数据库，直接从Map获取
                if (cameraIdRelNameMaps.containsKey(cameraId)) {
                    cameraName = cameraIdRelNameMaps.get(cameraId).getName();
                    regionName = cameraIdRelNameMaps.get(cameraId).getRegionName();
                } else {
                    if (StringUtils.isNumeric(cameraId)) {
                        CameraVo camera = cameraService.selectByPrimaryKey(cameraId);
                        if (null != camera) {
                            cameraName = camera.getName();
                            regionName = camera.getRegionName();
                            cameraIdRelNameMaps.put(cameraId, camera);
                        }
                    }
                }
                if ("".equals(cameraName)) {
                    task.setCameraName("未查询到点位信息");
                } else {
                    task.setCameraName(cameraName);
                    task.setRegion(regionName);
                }
            }
        }
        return vsdTaskList;
    }

    //TODO 逻辑有问题，需要再修改
    @Override
    public boolean deleteBySerialnumber(String serialnumber) {
        List<VsdTaskRelation> relations = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber));
        //假如是离线视频，删掉离线视频记录
        if (relations != null && !relations.isEmpty()) {
            VsdTaskRelation relation = relations.get(0);
            if (VideoTaskConstant.FROM_TYPE.OFFLINE == relation.getFromType()) {
                Serializable cameraFileId = relation.getCameraFileId();
                ctrlUnitFileService.removeById(cameraFileId);
            }
        }
        return vsdTaskRelationService.remove(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber));
    }

    @Override
    @Transactional
    public R deleteGateTask(String cameraId) {
        VsdTaskRelation vsdTaskRelation = vsdTaskRelationService.getOne(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraId));
        Map<String, Object> paramMap = new HashedMap();
        if (vsdTaskRelation == null) {//有卡口点位, 但没有启动任务
            cameraService.removeById(cameraId);
            return R.ok();
        }
        paramMap.put("serialnumber", vsdTaskRelation.getSerialnumber());
        String resultJson = videoObjextTaskService.deleteVsdTaskService(paramMap);
        if (StringUtils.isNotEmptyString(resultJson)) {
            Var reVar = Var.fromJson(resultJson);
            String retCode = reVar.getString("ret");
            if ("0".equals(retCode)) {
                cameraService.removeById(cameraId);
                vsdTaskRelationService.removeById(vsdTaskRelation.getId());
                return R.ok();
            } else {
                log.info("调用Jmanager 删除监控点关联任务失败！ serialnumber: " + vsdTaskRelation.getSerialnumber());
                return R.error("监控点位失败");
            }
        } else {
            return R.error("监控点位失败");
        }
    }

    @Override
    @Transactional
    public R startNewGateTask(Long userId, Camera camera) {
        VsdTaskRelation taskRelation = vsdTaskRelationService.getOne(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", camera.getId().toString()));
        if (taskRelation == null) {
            VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
            vsdTaskRelation.setId(System.currentTimeMillis());
            vsdTaskRelation.setTaskId(System.currentTimeMillis());
            vsdTaskRelation.setSerialnumber(IdUtils.getTaskId().toString());
            vsdTaskRelation.setCreatetime(new Date());
            vsdTaskRelation.setCameraFileId(camera.getId().toString());
            vsdTaskRelation.setFromType(VideoTaskConstant.TASK_TYPE.GATE);
            vsdTaskRelation.setIsvalid(1);
            vsdTaskRelation.setTaskStatus(1);
            vsdTaskRelation.setTaskProgress(0);
            vsdTaskRelation.setTaskUserId(VideoTaskConstant.USER_ID.GATE);
            vsdTaskRelation.setTaskName(camera.getName());
            vsdTaskRelation.setCameraId(camera.getId().toString());
            vsdTaskRelation.setUrl(camera.getUrl());
            vsdTaskRelation.setCreateuser(userId);
            vsdTaskRelation.setLastUpdateTime(new Date());
            vsdTaskRelationService.save(vsdTaskRelation);
            Map<String, Object> paramMap = new HashedMap();
            paramMap.put("serialnumber", vsdTaskRelation.getSerialnumber());
            paramMap.put("url", IdUtils.getTaskId().toString());//有值就行
            paramMap.put("type", TypeEnums.GATE_OTHER.getValue());
            paramMap.put("name", camera.getName());
            paramMap.put("taskType", 1);//实时流
            paramMap.put("createuser", userId);//实时流
            paramMap.put("cameraId", camera.getId().toString());
            String resultJson = videoObjextTaskService.startNewGateTask(paramMap);
            Var var = Var.fromJson(resultJson);
            String retCode = var.getString("ret");
            String retDesc = var.getString("desc");
            if ("0".equals(retCode)) {
                return R.ok();
            } else {
                throw new VideoException(retDesc);
            }
        } else {
            taskRelation.setIsvalid(1);//0停止, 1启动
            taskRelation.setTaskStatus(1);//1分析中,2分析完成
            taskRelation.setLastUpdateTime(new Date());
            vsdTaskRelationService.updateById(taskRelation);
        }
        return R.ok();
    }

    @Override
    public R deleteResultData(String uuid) {
        Map<String, Object> paramMap = new HashedMap();
        paramMap.put("uuid", uuid);
        String resultJson = videoObjextTaskService.deleteResultData(paramMap);
        Var addResVar = Var.fromJson(resultJson);
        String retCode = addResVar.getString("ErrorCode");
        String retDesc = addResVar.getString("Status");
        if ("0".equals(retCode)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                log.error("thread interrupted", ex);
            }
            return R.ok();
        } else {
            return R.error(retDesc);
        }
    }

    @Override
    public R updataResultData(ResultUpdateRequest paramBo) {
        String resultJson = videoObjextTaskService.updateResultData(paramBo);
        Var addResVar = Var.fromJson(resultJson);
        String retCode = addResVar.getString("StatusCode");
        String retDesc = addResVar.getString("StatusString");
        if ("0".equals(retCode)) {
            return R.ok();
        } else {
            return R.error(retDesc);
        }
    }
}
