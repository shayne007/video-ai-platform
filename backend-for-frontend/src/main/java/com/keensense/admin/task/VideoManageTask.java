package com.keensense.admin.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.CtrlUnitFile;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.mapper.task.CameraMapper;
import com.keensense.admin.mapper.task.VsdTaskRelationMapper;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.ICtrlUnitFileService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.TaskParamVo;
import com.keensense.admin.vo.VsdTaskVo;
import com.keensense.common.platform.enums.TaskTypeEnums;
import com.keensense.common.util.DateUtil;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class VideoManageTask {

    @Resource
    private ICtrlUnitFileService ctrlUnitFileService;

    @Resource
    private CameraMapper cameraMapper;

    @Resource
    private ICameraService cameraService;

    @Resource
    private IVsdTaskService vsdTaskService;

    @Autowired
    private VideoObjextTaskService videoObjextTaskService;

    @Autowired
    private VsdTaskRelationMapper vsdTaskRelationMapper;

    @Autowired
    private IVsdTaskRelationService vsdTaskRelationService;

    /**
     * 定时任务，更新离线视频转码进度
     * 每5s进行转码进度查询
     */
    @Scheduled(cron = "4/5 * * * * ?")
    public void updateAllVideoTranscodingProgress() {
        log.info("定时任务，更新离线视频转码进度");
        List<CtrlUnitFile> ctrlUnitFiles = ctrlUnitFileService.selectAllUntrascodingVideos();
        if (ListUtil.isNull(ctrlUnitFiles)) {
            log.info("-------------->updateAllVideoTranscodingProgress,ctrlUnitFiles is null");
            return;
        }

        log.info("-------------->updateAllVideoTranscodingProgress,ctrlUnitFiles size " + ctrlUnitFiles.size());
        for (CtrlUnitFile ctrlUnitFile : ctrlUnitFiles) {
            log.info("-------------->updateAllVideoTranscodingProgress,ctrlUnitFiles ctrlUnitFile id " + ctrlUnitFile.getId());
            Boolean aBoolean = ctrlUnitFileService.updateCtrlUnitFileTranscodingProgress(ctrlUnitFile);

            if (!aBoolean) {
                log.info("-------------->updateAllVideoTranscodingProgress update fail, fileId :  " + ctrlUnitFile.getId());
            } else {
                //转码完成提交离线任务
            }
        }
    }

    @Scheduled(cron = "4/5 * * * * ?")
    public void clearFileDate() {
        log.info("定时任务，定期清理离线视频及任务");
        List<CtrlUnitFile> ctrlUnitFiles = ctrlUnitFileService.selectAllDelVideos();
        log.info("-------------->clearFileDate,ctrlUnitFiles size " + ctrlUnitFiles.size());
        for (CtrlUnitFile ctrlUnitFile : ctrlUnitFiles) {
            log.info("-------------->clearFileDate,ctrlUnitFiles ctrlUnitFile id " + ctrlUnitFile.getId());
            ctrlUnitFileService.clearFileDate(ctrlUnitFile.getId() + "");
        }
    }


    /**
     * 定时任务，后台处理自动提交离线视频分析任务
     * 每2秒执行一次
     */
//    @Scheduled(cron = "0 1/2 * * * ?")
    @Scheduled(cron = "4/5 * * * * ?")
    public void autoCommitOfflineTask() {
        log.info("定时任务，后台处理自动提交离线视频分析任务");
        List<CtrlUnitFile> ctrlFileList = ctrlUnitFileService.selectCtrlUnitFileByTask();
        if (ListUtil.isNull(ctrlFileList)) {
            log.info("-------------->autoCommitOfflineTask，ctrlFileList is null");
            return;
        }
        log.info("-------------->autoCommitOfflineTask,ctrlFileList size : " + ctrlFileList.size());

        for (CtrlUnitFile ctrlFile : ctrlFileList) {
            Integer autoAnalysisFlag = ctrlFile.getAutoAnalysisFlag();
            Long ctrlFileId = ctrlFile.getId();
            // 不是自动分析状态，不调用添加分析任务的接口
            if (autoAnalysisFlag == 0) {
                log.error("-------------->autoCommitOfflineTask is wrong, autoAnalysisFlag == 0 , ctrlFileId : " + ctrlFileId);
                continue;
            }
            VsdTaskRelation vsdTaskRelation = vsdTaskRelationMapper.selectOne(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", ctrlFile.getId()));
            if (vsdTaskRelation == null) {
                boolean aBoolean = addVsdTaskByJManager(ctrlFile);
                if (!aBoolean) {
                    log.info("-------------->autoCommitOfflineTask commit fail, ctrlFileId :  " + ctrlFileId);
                }
            } else {
                if (vsdTaskRelation.getTaskStatus() == -1) {
                    log.info("OfflineTask not found to delete" + vsdTaskRelation.getSerialnumber());
                    vsdTaskRelationMapper.deleteById(vsdTaskRelation.getId());
                }
            }

        }
    }

    /**
     * 调用JManager接口插入添加任务
     *
     * @param ctrlUnitFile
     * @return
     */
    private boolean addVsdTaskByJManager(CtrlUnitFile ctrlUnitFile) {
        boolean isSuccess = true;
        Map<String, Object> paramMap = new HashedMap();
        String serialnumber = vsdTaskRelationService.getSerialnumber();
        Long ctrlFileId = ctrlUnitFile.getId();
        log.info("添加离线视频分析任务，离线视频文件ID  :  " + ctrlFileId);

        TaskParamVo taskParamBo = new TaskParamVo();
        VsdTaskVo vsdTask = new VsdTaskVo();
        vsdTask.setType(VideoTaskConstant.Type.OBJEXT);
        vsdTask.setFilefromtype(VideoTaskConstant.FROM_TYPE.OFFLINE);
        vsdTask.setParam("");
        vsdTask.setCameraFileId(String.valueOf(ctrlFileId));
        String extcameraid = "";
        String url = ctrlUnitFile.getFileFtpPath();
        Long cameraId = ctrlUnitFile.getCameraId();
        if (cameraId != null) {
            Camera camera = cameraService.selectCameraById(String.valueOf(cameraId));
            if (camera != null) {
                extcameraid = camera.getExtcameraid();
            }
        }
        paramMap.put("serialnumber", serialnumber);//任务序列号
        paramMap.put("type", vsdTask.getType());//任务类型
        paramMap.put("url", url);//视频路径
        paramMap.put("cameraId", ctrlUnitFile.getCameraId() + "");
        paramMap.put("name", ctrlUnitFile.getFileName());
        paramMap.put("userId", VideoTaskConstant.USER_ID.OFFLINE_VIDEO);
        paramMap.put("deviceId", extcameraid);//设备ID
        paramMap.put("entryTime", DateUtil.formatDate(ctrlUnitFile.getEntryTime(), DateFormatConst.YMDHMS_));
        paramMap.put("fromType", VideoTaskConstant.FROM_TYPE.OFFLINE);
        paramMap.put("taskType", TaskTypeEnums.OFF_LINE.getValue());
        paramMap.put("cameraFileId", vsdTask.getCameraFileId());
        paramMap.put("createuser", ctrlUnitFile.getCreateUserId());
        paramMap.put("scene", ctrlUnitFile.getScene());
        paramMap.put("enableIndependentFaceSnap", ctrlUnitFile.getEnableIndependentFaceSnap());
        paramMap.put("enableBikeToHuman", ctrlUnitFile.getEnableBikeToHuman());
        paramMap.put("sensitivity", ctrlUnitFile.getSensitivity());
        if (StringUtils.isNotEmpty(ctrlUnitFile.getInterestParam())) {
            paramMap.put("udrVertices", ctrlUnitFile.getInterestParam());
            paramMap.put("isInterested", true);
        } else if (StringUtils.isNotEmpty(ctrlUnitFile.getUninterestParam())) {
            paramMap.put("udrVertices", ctrlUnitFile.getUninterestParam());
            paramMap.put("isInterested", false);
        }
        if (StringUtils.isNotEmpty(ctrlUnitFile.getTripwires())) {
            paramMap.put("tripWires", ctrlUnitFile.getTripwires());
        }
        if (null != ctrlUnitFile.getOverlineType()) {
            paramMap.put("overlineType", ctrlUnitFile.getOverlineType());
        }
        String resultJson = videoObjextTaskService.addVsdTaskService(paramMap, true);

        Var resultVar = Var.fromJson(resultJson);
        if (!"0".equals(resultVar.getString("ret"))) {
            log.error("videoObjextTaskService 返回数据错误,resultVar : " + resultVar);
            return false;
        }

        return isSuccess;
    }
}
