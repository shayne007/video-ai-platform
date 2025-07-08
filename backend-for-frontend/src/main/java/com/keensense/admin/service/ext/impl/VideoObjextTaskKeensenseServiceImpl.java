package com.keensense.admin.service.ext.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.constants.CameraConstants;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.mapper.task.HumanColorModelMapper;
import com.keensense.admin.mqtt.config.EnumerationConfig;
import com.keensense.admin.request.ResultUpdateRequest;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.sys.ICfgMemPropsService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.JSONUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.util.ThreadTaskUtil;
import com.keensense.admin.util.ValidateHelper;
import com.keensense.admin.util.VideoXmlUtil;
import com.keensense.admin.vo.VsdSlavestatusVo;
import com.keensense.common.exception.VideoException;
import com.keensense.common.platform.FdfsUtil;
import com.keensense.common.platform.StandardRequestUtil;
import com.keensense.common.platform.TaskUtil;
import com.keensense.common.platform.bo.video.AnalysisResultBo;
import com.keensense.common.platform.bo.video.ObjextTaskBo;
import com.keensense.common.platform.domain.NonMotorVehiclesResult;
import com.keensense.common.platform.domain.PersonResult;
import com.keensense.common.platform.domain.VlprResult;
import com.keensense.common.platform.enums.TaskTypeEnums;
import com.keensense.common.platform.enums.TypeEnums;
import com.keensense.common.util.DateUtil;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.MapUtil;
import com.loocme.sys.util.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author cuiss
 * @Description JManager增加vsd_task任务接口
 * @Date 2018/10/11
 */
@Service("videoObjextTaskService" + AbstractService.KS)
public class VideoObjextTaskKeensenseServiceImpl extends AbstractService implements VideoObjextTaskService {

    @Resource
    private IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    private ICfgMemPropsService cfgMemPropsService;

    @Autowired
    private EnumerationConfig enumerationConfig;

    @Autowired
    private HumanColorModelMapper humanColorModelMapper;

    public ObjextTaskBo initAddInputParam(Map<String, Object> paramMap) {
        ObjextTaskBo inParam = new ObjextTaskBo();
        String serialnumber = MapUtil.getString(paramMap, "serialnumber");
        String type = MapUtil.getString(paramMap, "type");
        String url = MapUtil.getString(paramMap, "url");
        //Integer scene = cfgMemPropsService.getScene();
        String cameraId = MapUtil.getString(paramMap, "cameraId");
        String name = MapUtil.getString(paramMap, "name");
        String userId = MapUtil.getString(paramMap, "userId");
        Integer sensitivity = MapUtil.getInteger(paramMap, "sensitivity");
        String deviceId = MapUtil.getString(paramMap, "deviceId");
        String startTime = MapUtil.getString(paramMap, "startTime");
        String endTime = MapUtil.getString(paramMap, "endTime");
        String entryTime = MapUtil.getString(paramMap, "entryTime");
        Integer fromType = MapUtil.getInteger(paramMap, "fromType");
        Integer taskType = MapUtil.getInteger(paramMap, "taskType");
        Boolean isInterested = MapUtil.getBoolean(paramMap, "isInterested");
        String udrVertices = MapUtil.getString(paramMap, "udrVertices");
        String tripWires = MapUtil.getString(paramMap, "tripWires");
        String cameraFileId = MapUtil.getString(paramMap, "cameraFileId");
        Long createuser = MapUtil.getLong(paramMap, "createuser");
        Integer enablePartial = MapUtil.getInteger(paramMap, "enablePartial");
        Integer scene = MapUtil.getInteger(paramMap, "scene");
        boolean enableIndependentFaceSnap = MapUtil.getBoolean(paramMap, "enableIndependentFaceSnap");
        boolean enableBikeToHuman = MapUtil.getBoolean(paramMap, "enableBikeToHuman");
        String uuid = MapUtil.getString(paramMap, "uuid");
        if (StringUtils.isNotEmptyString(serialnumber)) {
            inParam.setSerialnumber(serialnumber);//任务序列号
        }
        if (StringUtils.isNotEmptyString(type)) {
            inParam.setType(TypeEnums.get(type));//任务类型
        }
        if (StringUtils.isNotEmptyString(url)) {
            inParam.setUrl(url);//视频路径
        }
        if (scene != null) {
            inParam.setScene(scene);//分析场景
        }
        inParam.setDeviceId(deviceId);//设备ID
        if (StringUtils.isNotEmptyString(entryTime)) {
            inParam.setEntryTime(entryTime); //校正时间
        }
        if (StringUtils.isNotEmpty(cameraId)) {
            inParam.setCameraId(cameraId);//视频监控点
        }
        if (StringUtils.isNotEmptyString(name)) {
            inParam.setName(name);//任务名称
        }
        if (StringUtils.isNotEmptyString(userId)) {
            inParam.setUserId(userId);//创建用户
        }
        if (sensitivity != null) {
            inParam.setSensitivity(sensitivity);
        }
        if (StringUtils.isNotEmptyString(startTime)) {
            inParam.setStartTime(startTime);
        }
        if (StringUtils.isNotEmptyString(endTime)) {
            inParam.setEndTime(endTime);
        }

        if (fromType != null) {
            inParam.setFromType(fromType);
        }
        if (taskType != null) {
            inParam.setTaskType(TaskTypeEnums.get(taskType));
        }

        if (enablePartial != null) { //是否开启标注
            inParam.setEnablePartial(enablePartial);
        }

        if (StringUtils.isNotEmpty(udrVertices)) {
            inParam.setUdrVertices(udrVertices);
            inParam.setInterested(isInterested);
        }
        if (StringUtils.isNotEmpty(tripWires)) {
            inParam.setTripWires(tripWires);
        }

        if (StringUtils.isNotEmpty(cameraFileId)) {
            inParam.setCameraFileId(cameraFileId);
        }
        inParam.setEnableIndependentFaceSnap(enableIndependentFaceSnap);
        inParam.setEnablePartial(enablePartial);
        inParam.setEnableBikeToHuman(enableBikeToHuman);
        if (StringUtils.isNotEmptyString(uuid)) {
            inParam.setUuid(uuid);
        }
        inParam.setCreateuser(createuser);
        return inParam;
    }

    protected ObjextTaskBo initQueryInputParam(Map<String, Object> map) {
        ObjextTaskBo inParam = new ObjextTaskBo();
        String startTime = MapUtil.getString(map, "startTime");
        String endTime = MapUtil.getString(map, "endTime");
        String type = MapUtil.getString(map, "type");
        String serialnumber = MapUtil.getString(map, "serialnumber");
        String userId = MapUtil.getString(map, "userId");
        String containName = MapUtil.getString(map, "containName");
        String name = MapUtil.getString(map, "name");

        if (!StringUtils.isEmptyString(name)) {
            inParam.setName(name);
        }

        if (!StringUtils.isEmptyString(startTime)) {
            inParam.setStartTime(startTime);
        }
        if (!StringUtils.isEmptyString(endTime)) {
            inParam.setEndTime(endTime);
        }
        if (!StringUtils.isEmptyString(type)) {
            inParam.setType(TypeEnums.get(type));//任务类型
        }
        if (!StringUtils.isEmptyString(serialnumber)) {
            inParam.setSerialnumber(serialnumber);
        }
        if (!StringUtils.isEmptyString(userId)) {
            inParam.setUserId(userId);
        }

        if (null != map && map.containsKey("status")) {
            inParam.setStatus(MapUtil.getInteger(map, "status"));
        }

        if (null != map && map.containsKey("pageSize")) {
            inParam.setPageSize(MapUtil.getInteger(map, "pageSize"));
        }
        if (null != map && map.containsKey("pageNo")) {
            inParam.setPageNo(MapUtil.getInteger(map, "pageNo"));
        }
        return inParam;
    }

    protected ObjextTaskBo initOperInputParam(Map<String, Object> paramMap) {
        int scene = cfgMemPropsService.getScene();
        ObjextTaskBo var = new ObjextTaskBo();
        var.setSerialnumber(MapUtil.getString(paramMap, "serialnumber"));
        var.setScene(scene);
        var.setInterested(MapUtils.getBoolean(paramMap, "isInterested"));
        String featurFollowarea = MapUtils.getString(paramMap, "udrVertices");
        var.setUdrVertices(featurFollowarea);
        var.setUrl(MapUtils.getString(paramMap, "url"));
        var.setTripWires(MapUtils.getString(paramMap, "tripWires"));
        var.setEnablePartial(MapUtil.getInteger(paramMap, "enablePartial"));
        var.setEnableIndependentFaceSnap(MapUtil.getBoolean(paramMap, "enableIndependentFaceSnap"));
        var.setScene(MapUtil.getInteger(paramMap, "scene"));
        var.setEnableBikeToHuman(MapUtil.getBoolean(paramMap, "enableBikeToHuman"));
        return var;
    }


    /**
     * 调用JManager服务
     *
     * @param paramMap
     * @return
     */
    @Override
    public String addVsdTaskService(Map<String, Object> paramMap, boolean saveRelation) {
        int startCount = vsdTaskRelationService.countRealTask(CameraConstants.CameraStatus.START);
        int routes = DbPropUtil.getInt("task-authorize-connect-number", 100);
        if (startCount > routes) {
            throw new VideoException(3000, "已达到系统最大支持路数" + routes + "路");
        }
        ObjextTaskBo params = initAddInputParam(paramMap);
        String vasUrl = MapUtil.getString(paramMap, "vasUrl");
        String entryTime = MapUtil.getString(paramMap, "entryTime");
        if (StringUtils.isEmptyString(entryTime)) {
            entryTime = DateUtil.formatDate(DateTimeUtils.getBeginDateTime(), "yyyy-MM-dd HH:mm:ss");
        }
        params.setEntryTime(entryTime);
        Integer overlineType = MapUtil.getInteger(paramMap, "overlineType");
        Integer enablePartial = MapUtil.getInteger(paramMap, "enablePartial");
        //写入vsd_task_relation表
        VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
        vsdTaskRelation.setEntryTime(DateUtil.parseDate(entryTime, "yyyy-MM-dd HH:mm:ss"));
        vsdTaskRelation.setId(Long.valueOf(RandomUtils.get18TimeRandom()));
        vsdTaskRelation.setTaskId(Long.valueOf(RandomUtils.get18TimeRandom()));
        vsdTaskRelation.setSerialnumber(params.getSerialnumber());
        vsdTaskRelation.setCreatetime(new Date());
        if (StringUtils.isNotEmpty(params.getCameraFileId())) {
            vsdTaskRelation.setCameraFileId(params.getCameraFileId());
        } else {
            vsdTaskRelation.setCameraFileId(params.getCameraId());
        }
        vsdTaskRelation.setFromType(params.getFromType());
        vsdTaskRelation.setIsvalid(0);
        vsdTaskRelation.setTaskStatus(0);
        vsdTaskRelation.setTaskProgress(0);
        vsdTaskRelation.setTaskUserId(params.getUserId());
        vsdTaskRelation.setTaskName(params.getName());
        vsdTaskRelation.setCameraId(params.getCameraId());
        if (StringUtils.isNotEmpty(vasUrl)) {
            vsdTaskRelation.setUrl(vasUrl);
        } else {
            vsdTaskRelation.setUrl(params.getUrl());
        }
        if (null != overlineType) {
            vsdTaskRelation.setOverlineType(overlineType);
        }
        vsdTaskRelation.setEnablePartial(enablePartial);
        vsdTaskRelation.setCreateuser(params.getCreateuser());
        if (saveRelation) {//排除接力追踪任务
            vsdTaskRelationService.save(vsdTaskRelation);
        }
        String userId = MapUtils.getString(paramMap, "userId");
        if (StringUtils.isNotEmptyString(userId) && userId.equals(VideoTaskConstant.USER_ID.ONLINE_VIDEO)) {
            ThreadTaskUtil.submit(() -> {
                boolean hasVideo = VideoXmlUtil.hasVideo(paramMap);//调用websocket接口获取是否有录像
                if (!hasVideo) {//无录像
                    vsdTaskRelation.setIsvalid(0);
                    vsdTaskRelation.setTaskStatus(5);
                    vsdTaskRelationService.updateById(vsdTaskRelation);
                } else {
                    addVideoObjextTask(params, saveRelation, vsdTaskRelation);
                }
            });
            JSONObject retcode = new JSONObject();
            retcode.put("ret", 0);
            return retcode.toString();
        } else {
            return addVideoObjextTask(params, saveRelation, vsdTaskRelation);
        }

    }

    private String addVideoObjextTask(ObjextTaskBo params, boolean saveRelation, VsdTaskRelation vsdTaskRelation) {
        JSONObject retcode = new JSONObject();
        String result = TaskUtil.addVideoObjextTask(initKeensenseUrl(), params);
        Var resultVar = Var.fromJson(result);
        String ret = resultVar.getString("ret");
        if ("0".equals(ret) && saveRelation) {
            vsdTaskRelation.setIsvalid(1);
            vsdTaskRelationService.updateById(vsdTaskRelation);
            retcode.put("ret", 0);
        } else {
            retcode.put("ret", ret);
            retcode.put("desc", resultVar.getString("desc"));
        }
        return retcode.toString();
    }


    @Override
    public String queryVsdTaskService(Map<String, Object> paramMap) {
        ObjextTaskBo params = initQueryInputParam(paramMap);
        return TaskUtil.getVideoObjectTaskList(initKeensenseUrl(), params);

    }

    @Override
    public String queryVsdTaskAllService(Map<String, Object> paramMap) {
        ObjextTaskBo params = initQueryInputParam(paramMap);
        return TaskUtil.getAllVideoObjectTaskList(initKeensenseUrl(), params);
    }


    @Override
    public String deleteVsdTaskService(Map<String, Object> paramMap) {
        ObjextTaskBo params = initOperInputParam(paramMap);
        return TaskUtil.deleteVideoObjectTask(initKeensenseUrl(), params);
    }

    @Override
    public String pauseVsdTaskService(Map<String, Object> paramMap) {
        ObjextTaskBo params = initOperInputParam(paramMap);
        return TaskUtil.pauseVideoObjectTask(initKeensenseUrl(), params);
    }

    @Override
    public String continueVsdTaskService(Map<String, Object> paramMap) {
        ObjextTaskBo params = initOperInputParam(paramMap);
        return TaskUtil.continueVideoObjectTask(initKeensenseUrl(), params);
    }

    @Override
    public String updateVsdTaskService(Map<String, Object> paramMap) {
        ObjextTaskBo params = initOperInputParam(paramMap);
        return TaskUtil.updateVideoObjectTask(initKeensenseUrl(), params);
    }

    @Override
    public Page<VsdSlavestatusVo> getVsdSlaveList(Page<VsdSlavestatusVo> pages, Map<String, Object> paramMap) {
        ObjextTaskBo params = new ObjextTaskBo();
        params.setDeviceId(MapUtil.getString(paramMap, "id"));
        params.setPageNo((int) pages.getCurrent());
        params.setPageSize((int) pages.getSize());
        String req = TaskUtil.getVsdSlaveList(initKeensenseUrl(), params);
        JSONObject reqJson = JSONObject.parseObject(req);
        List<VsdSlavestatusVo> vsdSlavestatusVoList = JSONObject.parseArray(reqJson.getString("salves"), VsdSlavestatusVo.class);
        if (ValidateHelper.isNotEmptyList(vsdSlavestatusVoList)) {
            for (VsdSlavestatusVo vsdSlavestatusVo : vsdSlavestatusVoList) {
                // 需要对load进行json解析
                if (vsdSlavestatusVo != null) {
                    if (StringUtils.isNotEmptyString(vsdSlavestatusVo.getPayload())) {
                        Map<String, Object> jsonMap = JSONUtil.getMap4Json(vsdSlavestatusVo.getPayload());
                        if (null != jsonMap) {
                            // cpu信息
                            initCpuInfo(vsdSlavestatusVo, jsonMap);
                            // 内存信息
                            initRamInfo(vsdSlavestatusVo, jsonMap);
                            // 磁盘信息
                            initDiskInfo(vsdSlavestatusVo, jsonMap);
                            // 任务信息
                            initTaskInfo(vsdSlavestatusVo, jsonMap);
                        }
                    }
                    if (vsdSlavestatusVo.getValid() == 1) {
                        vsdSlavestatusVo.setValidStr("可用");
                    } else {
                        vsdSlavestatusVo.setValidStr("失效");
                    }
                }
            }

        }
        pages.setTotal(reqJson.getInteger("totalCount"));
        pages.setRecords(vsdSlavestatusVoList);
        return pages;
    }

    private void initCpuInfo(VsdSlavestatusVo vsdSlavestatusVo, Map<String, Object> jsonMap) {
        String cpuRate;
        if (null != jsonMap.get("cpu")) {
            cpuRate = jsonMap.get("cpu").toString();
            vsdSlavestatusVo.setCpuRate(cpuRate);
        }
    }

    private void initTaskInfo(VsdSlavestatusVo vsdSlavestatusVo, Map<String, Object> jsonMap) {
        net.sf.json.JSONArray tasks = (net.sf.json.JSONArray) jsonMap.get("tasks");
        if (null != tasks) {
            vsdSlavestatusVo.setTaskNum(tasks.size());
        } else {
            vsdSlavestatusVo.setTaskNum(0);
        }
    }

    private void initRamInfo(VsdSlavestatusVo vsdSlavestatusVo, Map<String, Object> jsonMap) {
        double ramUsed;
        double allRam;
        int ramUsedRate;
        net.sf.json.JSONArray ram = null;
        if (null != jsonMap.get("ram")) {
            ram = (net.sf.json.JSONArray) jsonMap.get("ram");
        }
        if (null != ram) {
            // MB转G 保留一位小数点
            ramUsed = (int) (Double.valueOf(ram.get(0).toString()) / 1024 * 10) / 10.0;
            allRam = (int) (Double.valueOf(ram.get(1).toString()) / 1024 * 10) / 10.0;
            ramUsedRate = (int) (ramUsed / allRam * 100);
            vsdSlavestatusVo.setMemoryUsageRate(ramUsedRate);
            vsdSlavestatusVo.setMemoryUsage(String.valueOf(ramUsed));
            vsdSlavestatusVo.setMemoryAll(String.valueOf(allRam));
        }
    }

    private void initDiskInfo(VsdSlavestatusVo vsdSlavestatusVo, Map<String, Object> jsonMap) {
        double diskUsage;
        double diskRemain;
        net.sf.json.JSONArray diskfreespace = null;
        if (null != jsonMap.get("diskfreespace")) {
            if (jsonMap.get("diskfreespace") instanceof net.sf.json.JSONArray) {
                diskfreespace = (net.sf.json.JSONArray) jsonMap.get("diskfreespace");
                if (null != diskfreespace && diskfreespace.size() > 1) {
                    // MB
                    double allDisk = StringUtil.getDouble((diskfreespace.get(1).toString()));
                    allDisk = allDisk < 0 ? 0 : allDisk;
                    diskRemain = StringUtil.getDouble((diskfreespace.get(0).toString()));
                    diskRemain = diskRemain < 0 ? 0 : diskRemain;
                    diskUsage = allDisk - diskRemain;
                    vsdSlavestatusVo.setDiskRemain(diskRemain);
                    vsdSlavestatusVo.setDiskUsage(diskUsage);
                }
            }

        }
    }

    @Override
    public String retryTask(Map<String, Object> paramMap) {
        ObjextTaskBo var = new ObjextTaskBo();
        var.setSerialnumber(MapUtil.getString(paramMap, "serialnumber"));
        return StandardRequestUtil.retryTask(initKeensenseUrl(), var);
    }

    @Override
    public String startNewGateTask(Map<String, Object> paramMap) {
        ObjextTaskBo params = initAddInputParam(paramMap);
        String result = StandardRequestUtil.startNewGateTask(initKeensenseUrl(), params);
        return result;
    }

    @Override
    public String deleteResultData(Map<String, Object> paramMap) {
        ObjextTaskBo params = initAddInputParam(paramMap);
        String result = StandardRequestUtil.deleteResultDate(initKeensenseUrl(), params);
        return result;
    }

    @Override
    public String updateResultData(ResultUpdateRequest paramBo) {
        AnalysisResultBo params = initInputParam(paramBo);
        String resultJson = StandardRequestUtil.updateResultDate(initKeensenseUrl(), params);
        return resultJson;
    }

    public AnalysisResultBo initInputParam(ResultUpdateRequest paramBo) {
        AnalysisResultBo inParam = new AnalysisResultBo();
        String objType = paramBo.getType();
        if (paramBo != null) {
            if (CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON.equals(objType)) {
                inParam = initInputParamPerson(paramBo, inParam);
            } else if (CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR.equals(objType)) {
                inParam = initInputParamCar(paramBo, inParam);
            } else if (CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR_PERSON.equals(objType)) {
                inParam = initInputParamCarAndPerson(paramBo, inParam);
            }
            inParam.setObjType(paramBo.getType());
        }
        return inParam;
    }

    public AnalysisResultBo initInputParamPerson(ResultUpdateRequest paramBo, AnalysisResultBo inParam) {
        PersonResult personResult = new PersonResult();
        Map<String, String> colorGroups = StringUtils.splitEnumerationNameForCode(enumerationConfig.getColorGroups());

        if (StringUtils.isNotEmptyString(paramBo.getUuid())) {
            personResult.setPersonID(paramBo.getUuid());
        }
        if (StringUtils.isNotEmptyString(paramBo.getSex())) {
            Map<String, String> personSex = StringUtils.splitEnumerationNameForCode(enumerationConfig.getSex());
            personResult.setGenderCode(Integer.valueOf(personSex.get(paramBo.getSex())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getAge())) {
            Map<String, String> personAge = StringUtils.splitEnumerationNameForCode(enumerationConfig.getAge());
            personResult.setAge(Integer.valueOf(personAge.get(paramBo.getAge())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getUpcolorStr())) {
            personResult.setCoatColor(colorGroups.get(paramBo.getUpcolorStr()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getCoatStyle())) {
            Map<String, String> personCoatStyle =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getCoatStyle());
            personResult.setCoatStyle(personCoatStyle.get(paramBo.getCoatStyle()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getTrousersColor())) {
            personResult.setTrousersColor(colorGroups.get(paramBo.getTrousersColor()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getTrousersStyle())) {
            Map<String, String> personTrousersStyle =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getTrousersStyle());
            personResult.setTrousersStyle(personTrousersStyle.get(paramBo.getTrousersStyle()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getAngle())) {
            Map<String, String> personAngle = StringUtils.splitEnumerationNameForCode(enumerationConfig.getAngle());
            personResult.setAngle(Integer.valueOf(personAngle.get(paramBo.getAngle())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getHandbag())) {
            Map<String, String> personHandbag = StringUtils.splitEnumerationNameForCode(enumerationConfig.getHandBag());
            personResult.setHandbag(Integer.valueOf(personHandbag.get(paramBo.getHandbag())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getUmbrella())) {
            Map<String, String> personUmbrella =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getUmbrella());
            personResult.setUmbrella(Integer.valueOf(personUmbrella.get(paramBo.getUmbrella())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getGlasses())) {
            Map<String, String> personGlasses = StringUtils.splitEnumerationNameForCode(enumerationConfig.getGlasses());
            personResult.setGlasses(Integer.valueOf(personGlasses.get(paramBo.getGlasses())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getCap())) {
            Map<String, String> personCap = StringUtils.splitEnumerationNameForCode(enumerationConfig.getCap());
            personResult.setCap(personCap.get(paramBo.getCap()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getRespirator())) {
            Map<String, String> personRespirantor =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getRespirator());
            personResult.setRespirator(personRespirantor.get(paramBo.getRespirator()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBag())) {
            Map<String, String> personBag = StringUtils.splitEnumerationNameForCode(enumerationConfig.getBag());
            personResult.setBag(Integer.valueOf(personBag.get(paramBo.getBag())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getHairStyle())) {
            Map<String, String> personHairStyle =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getHairStyle());
            personResult.setHairStyle(personHairStyle.get(paramBo.getHairStyle()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getTrolley())) {
            Map<String, String> personTrolley = StringUtils.splitEnumerationNameForCode(enumerationConfig.getTrolley());
            personResult.setTrolley(Integer.valueOf(personTrolley.get(paramBo.getTrolley())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getLuggage())) {
            Map<String, String> personLuggage = StringUtils.splitEnumerationNameForCode(enumerationConfig.getLuggage());
            personResult.setLuggage(Integer.valueOf(personLuggage.get(paramBo.getLuggage())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getCoatTexture())) {
            Map<String, String> personCoatTexture =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getCoatTexture());
            personResult.setCoatTexture(personCoatTexture.get(paramBo.getCoatTexture()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getTrousersTexture())) {
            Map<String, String> personTrousersTexture =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getTrousersTexture());
            personResult.setTrousersTexture(personTrousersTexture.get(paramBo.getTrousersTexture()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getHasKnife())) {
            Map<String, String> personHasKnife =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getHasKnife());
            personResult.setHasKnife(Integer.valueOf(personHasKnife.get(paramBo.getHasKnife())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getShape())) {
            Map<String, String> personShape = StringUtils.splitEnumerationNameForCode(enumerationConfig.getShape());
            personResult.setShape(Integer.valueOf(personShape.get(paramBo.getShape())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getChestHold())) {
            Map<String, String> personChestHold =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getChestHold());
            personResult.setChestHold(Integer.valueOf(personChestHold.get(paramBo.getChestHold())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getMinority())) {
            Map<String, String> personMinority =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getMinority());
            personResult.setMinority(Integer.valueOf(personMinority.get(paramBo.getMinority())));
        }
        inParam.setPersonResult(personResult);
        return inParam;
    }

    public AnalysisResultBo initInputParamCarAndPerson(ResultUpdateRequest paramBo, AnalysisResultBo inParam) {
        NonMotorVehiclesResult nonMotorVehiclesResult = new NonMotorVehiclesResult();
        Map<String, String> colorGroups = StringUtils.splitEnumerationNameForCode(enumerationConfig.getColorGroups());

        if (StringUtils.isNotEmptyString(paramBo.getUuid())) {
            nonMotorVehiclesResult.setNonMotorVehicleID(paramBo.getUuid());
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeSex())) {
            Map<String, String> bikeSex = StringUtils.splitEnumerationNameForCode(enumerationConfig.getSex());
            nonMotorVehiclesResult.setSex(Byte.valueOf(bikeSex.get(paramBo.getBikeSex())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeGenre())) {
            Map<String, String> bikeGenre = StringUtils.splitEnumerationNameForCode(enumerationConfig.getBikeGenre());
            nonMotorVehiclesResult.setBikeGenre(Integer.valueOf(bikeGenre.get(paramBo.getBikeGenre())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getPassengersUpColorStr())) {
            nonMotorVehiclesResult.setCoatColor1(colorGroups.get(paramBo.getPassengersUpColorStr()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeAge())) {
            Map<String, String> bikeAge = StringUtils.splitEnumerationNameForCode(enumerationConfig.getAge());
            nonMotorVehiclesResult.setAge(Byte.valueOf(bikeAge.get(paramBo.getBikeAge())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeCoatStyle())) {
            Map<String, String> bikeCoatStyle =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getCoatStyle());
            nonMotorVehiclesResult.setCoatStyle(bikeCoatStyle.get(paramBo.getBikeCoatStyle()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeCoatTexture())) {
            Map<String, String> bikeCoatTexture =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getCoatTexture());
            nonMotorVehiclesResult.setCoatTexture(Integer.valueOf(bikeCoatTexture.get(paramBo.getBikeCoatTexture())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeAngle())) {
            Map<String, String> bikeAngle = StringUtils.splitEnumerationNameForCode(enumerationConfig.getAngle());
            nonMotorVehiclesResult.setAngle(Integer.valueOf(bikeAngle.get(paramBo.getBikeAngle())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeGlasses())) {
            Map<String, String> bikeGlasses = StringUtils.splitEnumerationNameForCode(enumerationConfig.getGlasses());
            nonMotorVehiclesResult.setGlasses(Integer.valueOf(bikeGlasses.get(paramBo.getBikeGlasses())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeRespirator())) {
            Map<String, String> bikeRespirator =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getRespirator());
            nonMotorVehiclesResult.setRespirator(Integer.valueOf(bikeRespirator.get(paramBo.getBikeRespirator())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeBag())) {
            Map<String, String> bikeBag = StringUtils.splitEnumerationNameForCode(enumerationConfig.getBag());
            nonMotorVehiclesResult.setBag(Integer.valueOf(bikeBag.get(paramBo.getBikeBag())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getWheels())) {
            Map<String, String> bikeWheels = StringUtils.splitEnumerationNameForCode(enumerationConfig.getWheels());
            nonMotorVehiclesResult.setWheels(Byte.valueOf(bikeWheels.get(paramBo.getWheels())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getHelmet())) {
            Map<String, String> bikeHelmet = StringUtils.splitEnumerationNameForCode(enumerationConfig.getHelmet());
            nonMotorVehiclesResult.setHelmet(Integer.valueOf(bikeHelmet.get(paramBo.getHelmet())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getHelmetcolorStr())) {
            nonMotorVehiclesResult.setHelmetColorTag1(colorGroups.get(paramBo.getHelmetcolorStr()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeHasPlate())) {
            Map<String, String> bikeHasPlate = StringUtils.splitEnumerationNameForCode(enumerationConfig.getHasPlate());
            nonMotorVehiclesResult.setHasPlate(Short.valueOf(bikeHasPlate.get(paramBo.getBikeHasPlate())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeUmbrella())) {
            Map<String, String> bikeUmbrella = StringUtils.splitEnumerationNameForCode(enumerationConfig.getUmbrella());
            nonMotorVehiclesResult.setUmbrella(Integer.valueOf(bikeUmbrella.get(paramBo.getBikeUmbrella())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getLampShape())) {
            Map<String, String> lampShap = StringUtils.splitEnumerationNameForCode(enumerationConfig.getLampShape());
            nonMotorVehiclesResult.setLampShape(lampShap.get(paramBo.getLampShape()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getCarryPassenger())) {
            Map<String, String> carryPassenger =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getCarryPassenger());
            nonMotorVehiclesResult.setCarryPassenger(carryPassenger.get(paramBo.getCarryPassenger()));
        }

        inParam.setNonMotorVehiclesResult(nonMotorVehiclesResult);
        return inParam;
    }


    public AnalysisResultBo initInputParamCar(ResultUpdateRequest paramBo, AnalysisResultBo inParam) {
        VlprResult vlprResult = new VlprResult();
        Map<String, String> colorGroups = StringUtils.splitEnumerationNameForCode(enumerationConfig.getColorGroups());

        if (StringUtils.isNotEmptyString(paramBo.getUuid())) {
            vlprResult.setMotorVehicleID(paramBo.getUuid());
        }
        if (StringUtils.isNotEmptyString(paramBo.getLicense())) {
            vlprResult.setPlateNo(paramBo.getLicense());
        }
        if (StringUtils.isNotEmptyString(paramBo.getCarlogo())) {
            vlprResult.setVehicleBrand(paramBo.getCarlogo());
        }
        if (StringUtils.isNotEmptyString(paramBo.getVehicleseries())) {
            vlprResult.setVehicleModel(paramBo.getVehicleseries());
        }
        if (StringUtils.isNotEmptyString(paramBo.getVehiclekind())) {
            Map<String, String> vehicleClass =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getVehicleClass());
            vlprResult.setVehicleClass(vehicleClass.get(paramBo.getVehiclekind()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getCarcolor())) {
            vlprResult.setVehicleColor(colorGroups.get(paramBo.getCarcolor()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getMainDriver())) {
            Map<String, String> safetyBelt = StringUtils.splitEnumerationNameForCode(enumerationConfig.getSafetyBelt());
            vlprResult.setSafetyBelt(Integer.valueOf(safetyBelt.get(paramBo.getMainDriver())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getCoDriver())) {
            Map<String, String> secondBelt = StringUtils.splitEnumerationNameForCode(enumerationConfig.getSecondBelt());
            vlprResult.setSecondBelt(Integer.valueOf(secondBelt.get(paramBo.getCoDriver())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getDrop())) {
            Map<String, String> drop = StringUtils.splitEnumerationNameForCode(enumerationConfig.getDrop());
            vlprResult.setDrop(drop.get(paramBo.getDrop()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getTissueBox())) {
            Map<String, String> tissueBox = StringUtils.splitEnumerationNameForCode(enumerationConfig.getPaper());
            vlprResult.setPaper(tissueBox.get(paramBo.getTissueBox()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getHasCall())) {
            Map<String, String> hasCall = StringUtils.splitEnumerationNameForCode(enumerationConfig.getHasCall());
            vlprResult.setCalling(Integer.valueOf(hasCall.get(paramBo.getHasCall())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getTagNum())) {
            vlprResult.setTagNum(paramBo.getTagNum());
        }
        if (StringUtils.isNotEmptyString(paramBo.getSun())) {
            Map<String, String> sun = StringUtils.splitEnumerationNameForCode(enumerationConfig.getSun());
            vlprResult.setSun(sun.get(paramBo.getSun()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getHasDanger())) {
            Map<String, String> danger = StringUtils.splitEnumerationNameForCode(enumerationConfig.getHasDanger());
            vlprResult.setHasDanger(danger.get(paramBo.getHasDanger()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getSunRoof())) {
            Map<String, String> sunRoof = StringUtils.splitEnumerationNameForCode(enumerationConfig.getSunRoof());
            vlprResult.setSunRoof(Integer.valueOf(sunRoof.get(paramBo.getSunRoof())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getRack())) {
            Map<String, String> rack = StringUtils.splitEnumerationNameForCode(enumerationConfig.getRack());
            vlprResult.setRack(Integer.valueOf(rack.get(paramBo.getRack())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getPlateColorCode())) {
            String plateColor = paramBo.getPlateColorCode().substring(0, paramBo.getPlateColorCode().length() - 1);
            vlprResult.setPlateColor(colorGroups.get(plateColor));
        }
        if (StringUtils.isNotEmptyString(paramBo.getDirection())) {
            Map<String, String> direction = StringUtils.splitEnumerationNameForCode(enumerationConfig.getDirection());
            vlprResult.setDirection(direction.get(paramBo.getDirection()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getDecoration())) {
            Map<String, String> decoration = StringUtils.splitEnumerationNameForCode(enumerationConfig.getDecoration());
            vlprResult.setDecoration(decoration.get(paramBo.getDecoration()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getVehicleHasPlate())) {
            Map<String, String> vehicleHasPlate =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getVehicleHasPlate());
            vlprResult.setHasPlate(vehicleHasPlate.get(paramBo.getVehicleHasPlate()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getCarSpeed())) {
            vlprResult.setSpeed(Double.valueOf(paramBo.getCarSpeed()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getVehicleAngle())) {
            Map<String, String> vehicleAngle = StringUtils.splitEnumerationNameForCode(enumerationConfig.getAngle());
            vlprResult.setAngle(Integer.valueOf(vehicleAngle.get(paramBo.getVehicleAngle())));
        }
        if (StringUtils.isNotEmptyString(paramBo.getPlateClass())) {
            Map<String, String> plateclassMap =
                    StringUtils.splitEnumerationNameForCode(enumerationConfig.getPlateClass());
            vlprResult.setPlateClass(plateclassMap.get(paramBo.getPlateClass()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getVehicleseries())) {
            vlprResult.setVehicleModel(paramBo.getVehicleseries());
        }
        if (StringUtils.isNotEmptyString(paramBo.getVehicleStyle())) {
            vlprResult.setVehicleStyles(paramBo.getVehicleStyle());
        }
        if (StringUtils.isNotEmptyString(paramBo.getHasCrash())) {
            Map<String, String> hasCrashMap = StringUtils.splitEnumerationNameForCode(enumerationConfig.getHasCrash());
            vlprResult.setHasCrash(hasCrashMap.get(paramBo.getHasCrash()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getAerial())) {
            Map<String, String> aerialMap = StringUtils.splitEnumerationNameForCode(enumerationConfig.getAerial());
            vlprResult.setAerial(aerialMap.get(paramBo.getAerial()));
        }

        inParam.setVlprResult(vlprResult);
        return inParam;
    }

    @Override
    public String saveImageToFdfs(String imageBase64) {
        return FdfsUtil.saveImage(initKeensenseUrl(), imageBase64);
    }
}
