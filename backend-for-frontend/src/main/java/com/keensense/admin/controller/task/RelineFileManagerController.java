package com.keensense.admin.controller.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.annotation.Login;
import com.keensense.admin.annotation.UnLogin;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.SwaggerTest;
import com.keensense.admin.dto.FileBo;
import com.keensense.admin.dto.TaskCondDTO;
import com.keensense.admin.entity.task.CtrlUnitFile;
import com.keensense.admin.request.CtrlUnitFileRequest;
import com.keensense.admin.request.InterestSettingsRequest;
import com.keensense.admin.request.Plupload;
import com.keensense.admin.request.ResultQueryRequest;
import com.keensense.admin.service.task.FtpService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.ICtrlUnitFileService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.InterestUtil;
import com.keensense.admin.util.PolygonUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.keensense.common.validator.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 视频监控点控制器
 **/
@Slf4j
@Api(tags = "接入源-录像分析-离线视频")
@RestController
@RequestMapping("/reline")
public class RelineFileManagerController extends BaseController {

    @Resource
    private ICtrlUnitFileService ctrlUnitFileService;

    @Resource
    private ICameraService cameraService;

    @Resource
    private IVsdTaskService vsdTaskService;

    @Resource
    private ResultQueryContoller resultQueryContoller;
    /**
     * 自动分析
     */
    private static final Integer AUTO_ANALYSIS_YES = 1;

    @Resource
    private FtpService ftpService;

    /**
     * 离线视频列表查询
     * 原queryFileByPage接口
     *
     * @param relineListRequest
     * @return
     */
    @UnLogin
    @ApiOperation(value = "离线视频列表查询" + SwaggerTest.DEBUG)
    @PostMapping(value = "/list")
    public R queryFileByPage(@RequestBody TaskCondDTO relineListRequest) throws Exception {
        if (StringUtils.isNotEmptyString(relineListRequest.getFileName())) {
            if (StringUtils.isEmpty(relineListRequest.getFileName().trim())) {
                return R.error("不允许全为空格");
            }
            if (relineListRequest.getFileName().length() > CommonConstants.REGEX.CASE_LOCATION_NAME_LENGTH)
                if (StringUtils.checkRegex_false(relineListRequest.getFileName(),
                        CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
                    return R.error("视频名称不符合规则");
                }
        }
        Page<CtrlUnitFile> page = new Page<CtrlUnitFile>(relineListRequest.getPage(), relineListRequest.getRows());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fileName", relineListRequest.getFileName());
        map.put("status", relineListRequest.getStatus());
        relineListRequest.setFromType(2L);

        Page<CtrlUnitFile> result = ctrlUnitFileService.queryOfflineVideo(page, map);
        return R.ok().put("page", new PageUtils(result));
    }

    /**
     * 批量删除
     */
    @ApiOperation("删除离线视频(单个/批量)" + SwaggerTest.DEBUG)
    @ApiImplicitParam(name = "ids", value = "离线视频id,多个以,分割")
    @PostMapping(value = "/deleteVideoList")
    public R deleteVideoList(String ids) {
        int num = ctrlUnitFileService.deleteFiles(ids);
        return R.ok().put("msg", "删除数量:" + num);
    }

    /**
     * 设置感兴趣、不感兴趣区域
     *
     * @param interestSettingsRequest
     * @return
     * @throws Exception
     */
    @ApiOperation("设置感兴趣、不感兴趣区域" + SwaggerTest.DEBUG)
    @PostMapping(value = "/addInterestSettings")
    public R addInterestSettings(@RequestBody InterestSettingsRequest interestSettingsRequest) {
        CtrlUnitFile ctrlUnitFile = ctrlUnitFileService.getById(interestSettingsRequest.getFileId());
        if (ctrlUnitFile == null) {
            return R.error("离线视频不存在");
        } else if (ctrlUnitFile.getAutoAnalysisFlag().equals(1)) {
            return R.error("离线视频已在分析中,无法再画感兴趣区域");
        }
        String interestParam = interestSettingsRequest.getInterestParam();
        Integer chooseType = interestSettingsRequest.getChooseType();
        String tripwires = interestSettingsRequest.getTripwires();
        long fileId = interestSettingsRequest.getFileId();
        Integer interestFlag = interestSettingsRequest.getInterestFlag();
        Integer overlineType = interestSettingsRequest.getOverlineType();
        Integer scene = interestSettingsRequest.getScene();
        String enableIndependentFaceSnap = interestSettingsRequest.getEnableIndependentFaceSnap();
        String enableBikeToHuman = interestSettingsRequest.getEnableBikeToHuman();
        CtrlUnitFile file = new CtrlUnitFile();
        file.setId(fileId);
        file.setAutoAnalysisFlag(AUTO_ANALYSIS_YES);
        file.setInterestFlag(interestFlag);
        file.setInterestParamSave(interestParam);//保存前端传值
        if (chooseType != null && chooseType == 1) {
            if (StringUtils.isNotEmptyString(interestParam) && interestParam.length() > 1024) {
                //所选区域点数过多
                return R.error("所选区域点数过多");
            }
            interestParam = InterestUtil.initInterestParam(interestParam);
            if (interestFlag != null && interestFlag == 1) {//感兴趣
                file.setInterestParam(interestParam);
            }
            if (interestFlag != null && interestFlag == 0) {
                file.setUninterestParam(interestParam);
            }
        } else if (chooseType != null && chooseType == 2) {
            // 保存跨线参数
            tripwires = InterestUtil.initTripwires(tripwires);
            file.setTripwires(tripwires);
        }
        if (null != overlineType) {
            file.setOverlineType(overlineType);
        }
        if (null != scene) {
            file.setScene(scene);
        }
        if (StringUtils.isNotEmptyString(enableIndependentFaceSnap)) {
            file.setEnableIndependentFaceSnap(enableIndependentFaceSnap);
        }
        if (StringUtils.isNotEmptyString(enableBikeToHuman)) {
            file.setEnableBikeToHuman(enableBikeToHuman);
        }
        boolean update = ctrlUnitFileService.updateById(file);
        log.info("更新感兴趣信息成功，fileId ：" + fileId);
        if (update) {

            return R.ok();
        } else {
            return R.error("更新感兴趣信息失败");
        }
    }

    /**
     * 新增文件 提供给结构化前台上传视频文件使用, 用?
     */
    @ApiOperation("上传离线视频")
    @UnLogin
    @PostMapping(value = "/addCameraMediaChucks")
    @ResponseBody
    public R addCameraMediaChucks(Plupload plupload, @RequestParam("fileName") MultipartFile multipartFile) throws Exception {
        // 返回对象
        FileBo returnFileBo = new FileBo();
        String token = UUID.randomUUID().toString().replace("-", "");
        File file = ctrlUnitFileService.saveUploadFileToTemp(plupload, token, multipartFile);
        int chunks = plupload.getChunks();// 用户上传文件被分隔的总块数
        int nowChunk = plupload.getChunk();// 当前块，从0开始
        CtrlUnitFile cameraMedia = null;
        if (null != file && chunks - nowChunk == 1) {
            cameraMedia = initCtrlUnitFile(plupload);
            returnFileBo = cameraService.uploadFileChuck(file, cameraMedia);
            if ("0".equals(returnFileBo.getRetFlag())) {//上传成功,并执行转码
                boolean insert = ctrlUnitFileService.insertCameraMedia(cameraMedia);
            }
        }
        return R.ok().put("fileId", returnFileBo.getCtrlUnitFile().getId());
    }

    @ApiOperation("添加离线分析任务")
    @UnLogin
    @PostMapping(value = "/addOfflineTask")
    public R addCameraMediaChucks(@RequestBody CtrlUnitFileRequest ctrlUnitFile) {
        ValidatorUtils.validateEntity(ctrlUnitFile);
        Date entryTime = ctrlUnitFile.getEntryTime();
        if (entryTime == null) {
            ctrlUnitFile.setEntryTime(DateTimeUtils.getBeginDateTime());
        }
        CtrlUnitFile cameraMedia = EntityObjectConverter.getObject(ctrlUnitFile, CtrlUnitFile.class);
        CtrlUnitFile ctrlUnitFile1 = ctrlUnitFileService.getById(ctrlUnitFile.getFileId());
        if (ctrlUnitFile1 == null) {
            return R.error("离线视频不存在");
        }
        if (ctrlUnitFile1.getAutoAnalysisFlag() != null && ctrlUnitFile1.getAutoAnalysisFlag().equals(1)) {
            return R.error("任务已在分析中...");
        }
        Integer sensitivity = ctrlUnitFile.getSensitivity();
        if (null != sensitivity) {
            cameraMedia.setSensitivity(sensitivity);
        }
        cameraMedia.setId(ctrlUnitFile.getFileId());

        if (ctrlUnitFile.getAutoAnalysisFlag() != null) {
            cameraMedia.setAutoAnalysisFlag(ctrlUnitFile.getAutoAnalysisFlag());
        }
        cameraMedia.setCreateUserId(getUserId());
        //勾选了最近的感兴趣区域
        if (ctrlUnitFile.getChooseInterest() != null && ctrlUnitFile.getChooseInterest() == 1) {
            CtrlUnitFile ctrlUnitFileRecent = ctrlUnitFileService.selectRecentInterest(ctrlUnitFile.getCameraId());//查点位最近的数据
            if (ctrlUnitFileRecent != null) {
                cameraMedia.setInterestParam(ctrlUnitFileRecent.getInterestParam());//InterestParam是后台处理过的参数,给前端没用
                cameraMedia.setUninterestParam(ctrlUnitFileRecent.getUninterestParam());
                cameraMedia.setInterestParamSave(ctrlUnitFileRecent.getInterestParamSave());//前端给什么返回什么,
                cameraMedia.setInterestFlag(ctrlUnitFileRecent.getInterestFlag());//保留当前点位上一次的感兴趣标识给前端;
            }
        }
        if (StringUtils.isEmpty(cameraMedia.getTranscodingId())) {
            String transecodeId = ftpService.addTranscodeTask(ctrlUnitFile1.getFileNameafterupload(), ctrlUnitFile.getVideoType());
            cameraMedia.setTranscodingId(transecodeId);
        }
        boolean insert = ctrlUnitFileService.updateById(cameraMedia);
        if (insert) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @ApiOperation("test")
    @GetMapping(value = "/test")
    public R task(String objType, String cameraId) {
        return R.ok();
    }

    private CtrlUnitFile initCtrlUnitFile(Plupload plupload) {
        CtrlUnitFile cameraMedia = new CtrlUnitFile();
        Long cameraMediaId = Long.valueOf(RandomUtils.get8RandomValiteCode(10));
        cameraMedia.setId(cameraMediaId);
//        String cameraId = plupload.getCameraId();
//        if (StringUtils.isNotEmptyString(cameraId)) {
//            cameraMedia.setCameraId(Long.valueOf(cameraId));
//        }
//        String entryTime = plupload.getEntryTime();
//        if (StringUtils.isEmptyString(entryTime)) {
//            cameraMedia.setEntryTime(DateTimeUtils.getBeginDateTime());
//        } else {
//            cameraMedia.setEntryTime(DateTimeUtils.formatDate(entryTime, DateTimeUtils.DEFAULT_FORMAT_DATE));
//        }

        cameraMedia.setVideoType("2");//离线视频类型
        //是否自动分析
        /*String autoAnalysisFlag = plupload.getAutoAnalysisFlag();
        if (StringUtils.isNotEmptyString(autoAnalysisFlag)) {
            cameraMedia.setAutoAnalysisFlag(Integer.parseInt(autoAnalysisFlag));
        }*/
        // 获取当前登陆用户信息
        Long userId = getUserId();
        cameraMedia.setCreateUserId(userId);
        cameraMedia.setFileType("2");
        // 当前时间
        cameraMedia.setCreateTime(new Date());
        return cameraMedia;
    }

    @ApiOperation("查询转码进度" + SwaggerTest.DEBUG)
    @UnLogin
    @PostMapping(value = "/updateTranscodeInfoById")
    @ApiImplicitParam(name = "fileId", value = "离线视频id")
    public R updateTranscodeInfoById(String fileId) {
        // 区域信息
        CtrlUnitFile ctrlUnitFile = ctrlUnitFileService.updateTranscodeInfoById(fileId);
        if (ctrlUnitFile == null) {
            return R.error("文件不存在").put("isdelete", true);
        } else if (ctrlUnitFile.getFilePathafterupload() != null && ctrlUnitFile.getFilePathafterupload().startsWith("http")) {
            ctrlUnitFile.setFilePathafterupload((ctrlUnitFile.getFilePathafterupload()));
            ctrlUnitFile.setThumbNail(ctrlUnitFile.getThumbNail());
        }
        return R.ok().put("data", ctrlUnitFile);

    }

    @UnLogin
    @ApiOperation("查询分析进度" + SwaggerTest.DEBUG)
    @PostMapping(value = "/selectAnalysisProgressById")
    @ApiImplicitParam(name = "fileId", value = "离线视频id")
    public R selectAnalysisProgressById(String fileId) {
        R result = R.ok();
        Map<String, Object> analysisProgress = vsdTaskService.selectAnalysisProgressByFileId(fileId);
        result.put("data", analysisProgress);// 列表数据
        return result;
    }

    //用?
    @ApiOperation("查询离线视频任务详情" + SwaggerTest.DEBUG)
    @PostMapping(value = "/task/queryTaskInfoByFileId")
    @ApiImplicitParam(name = "fileId", value = "离线视频id")
    public R queryTaskInfoByFileId(String fileId) {
        if (StringUtils.isNotEmptyString(fileId)) {
            CtrlUnitFile result = ctrlUnitFileService.queryOfflineVideoByFileId(fileId);
            return R.ok().put("data", result);
        } else {
            return R.error("离线视频ID不能为空");
        }
    }

    /**
     * 离线结构化结果查询
     *
     * @return
     */
    @ApiOperation("离线结构化结果查询")
    @PostMapping(value = "/getAnalysisDataList")
    public Map<String, Object> getOfflineLargeDataList(@RequestBody ResultQueryRequest paramBo) {
        ValidatorUtils.validateEntity(paramBo);
        return resultQueryContoller.getOfflineLargeDataList(paramBo);
    }

    /**
     * 查询多边形内的点位信息
     */
    @ApiOperation(value = "查询多边形内的点位信息")
    @PostMapping(value = "/queryCamerasInPolygon")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "polygon", value = "经纬度"),
            @ApiImplicitParam(name = "status", value = "状态 [0:离线，1:在线]")
    })
    public R queryCamerasInPolygon(String polygon, String status) {
        R result = R.ok();
        List<Point2D.Double> polygonList = PolygonUtil.paresPolygonList(polygon);
        List<CameraVo> cameraData = cameraService.queryCameraAll(null, status, polygonList);
        result.put("list", cameraData);
        return result;
    }
}