package com.keensense.admin.controller.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.UploadConstants;
import com.keensense.admin.entity.lawcase.AppLawcaseBasicoption;
import com.keensense.admin.entity.lawcase.TbCase;
import com.keensense.admin.entity.lawcase.TbCaseArchivePic;
import com.keensense.admin.entity.lawcase.TbCaseArchiveVideo;
import com.keensense.admin.entity.sys.SysUser;
import com.keensense.admin.enums.CaseJdStatus;
import com.keensense.admin.request.CaseQueryRequest;
import com.keensense.admin.service.lawcase.IAppLawcaseBasicoptionService;
import com.keensense.admin.service.lawcase.ITbCaseArchivePicService;
import com.keensense.admin.service.lawcase.ITbCaseArchiveVideoService;
import com.keensense.admin.service.lawcase.ITbCaseService;
import com.keensense.admin.service.lawcase.UploadDataToJdService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.PropertiesUtil;
import com.keensense.admin.util.QuerySqlUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.CaseArchivePicVo;
import com.keensense.admin.vo.CaseArchiveVideoVo;
import com.keensense.admin.vo.CaseArchiveVo;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.admin.vo.TbCaseVo;
import com.keensense.admin.vo.UploadDataToJdVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.FileUtil;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 佳都案件录入控制器
 **/
@Slf4j
@RestController
@Api(tags = "案件管理-定制化-佳都")
@RequestMapping("/jdCase")
public class JdCaseController extends BaseController {

    @Resource
    private ICameraService cameraService;

    @Resource
    private IAppLawcaseBasicoptionService appLawcaseBasicoptionService;

    @Resource
    private ITbCaseService tbCaseService;

    @Resource
    private ITbCaseArchivePicService tbCaseArchivePicService;

    @Resource
    private ITbCaseArchiveVideoService tbCaseArchiveVideoService;

    @Resource
    private UploadDataToJdService uploadDataToJdService;

    @Resource
    private ResultService resultService;

    /**
     * 刷新案件列表【佳都】
     */
    @ApiOperation(value = "同步案件")
    @PostMapping(value = "/refreshCaseList")
    public R refreshCaseList() {
        boolean ret = tbCaseService.saveCaseData();
        if (!ret) {
            return R.error("同步失败");
        }
        return R.ok();
    }

    /**
     * 案件详情[佳都]
     */
    @ApiOperation(value = "")
    @PostMapping(value = "/queryBaseCaseInfo")
    @ApiImplicitParam(name = "lawcaseId", value = "案件Id")
    public R queryBaseCaseInfo(String lawcaseId) {
        R result = R.ok();
        try {
            TbCase tbCase = tbCaseService.getOne(new QueryWrapper<TbCase>().eq("case_code", lawcaseId));
            TbCaseVo lawCase = EntityObjectConverter.getObject(tbCase, TbCaseVo.class);
            if (lawCase.getCaseStartTime() != null) {
                lawCase.setCaseStartTimeStr(DateTimeUtils.formatDate(lawCase.getCaseEndTime(), null));
            }
            if (lawCase.getCaseEndTime() != null) {
                lawCase.setCaseEndTimeStr(DateTimeUtils.formatDate(lawCase.getCaseEndTime(), null));
            }
            String caseCode = lawCase.getCaseCode();

            List<CaseArchivePicVo> picClues = tbCaseArchivePicService.getCaseArchiveInfoPicList(caseCode);

            List<CaseArchiveVideoVo> videoClues = tbCaseArchiveVideoService.getCaseArchiveInfoVideoList(caseCode);

            // 获取案件的类别
            if (StringUtils.isNotEmptyString(lawCase.getCaseOptionId())) {
                AppLawcaseBasicoption appLawcaseBasicoption = appLawcaseBasicoptionService.getOne(new QueryWrapper<AppLawcaseBasicoption>().eq("OPTIONS_VALUE", lawCase.getCaseOptionId()));
                if (appLawcaseBasicoption != null) {
                    String categoryName = appLawcaseBasicoption.getOptionsName();
                    lawCase.setCaseOptionName(categoryName);
                }
            }
            result.put("lawCase", lawCase);
            result.put("picClues", picClues);
            result.put("videoClues", videoClues);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            R.error(e.getMessage());
        }
        return result;
    }

    /**
     * 案件详情中 目标轨迹 [佳都]
     *
     * @throws Exception
     */
    @ApiOperation(value = "查询目标轨迹")
    @PostMapping(value = "/selectByList")
    @ApiImplicitParam(name = "lawcaseId", value = "案件Id")
    public R selectByList(String lawcaseId) {
        R result = R.ok();
        try {
            List<CaseArchivePicVo> clues = tbCaseArchivePicService.queryCurrentTaskCameras(lawcaseId);
            result.put("clues", clues);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
        return result;
    }

    /**
     * 保存目标轨迹图片到案件记录中[佳都]
     *
     * @param lawcaseId  案件的ID
     * @param base64Data 图片的base64编码
     * @return
     */
    @ApiOperation(value = "保存图片轨迹")
    @PostMapping("/saveTrackImage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lawcaseId", value = "案件Id"),
            @ApiImplicitParam(name = "base64Data", value = "base64Data数据")
    })
    public R saveTrackImage(String lawcaseId, String base64Data) {
        R result = R.ok();
        try {
            if (StringUtil.isNotNull(base64Data)) {
//                String persistPictureId = PersistPictureUtil.savePicture(base64Data);
                String persistPictureId = null;
                if (StringUtil.isNotNull(persistPictureId)) {
                    TbCase tbCase = new TbCase();
                    tbCase.setTrackImageUrl(persistPictureId);
                    boolean update = tbCaseService.update(tbCase, new QueryWrapper<TbCase>().eq("case_code", lawcaseId));
                    if (update) {
                        result.put("result", persistPictureId);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error("保存失败");
        }
        return result;
    }

    /**
     * 查任务总数
     * 录入员只能看到自己录入的任务
     * 审核员能看到已经审核完成的任务
     */
    @ApiOperation(value = "案件查询（列表/状态/名称")
    @PostMapping(value = "/queryCasePage")
    public R queryTaskPage(@RequestBody CaseQueryRequest caseQueryRequest) {
        R result = R.ok();
        if (StringUtils.isNotEmptyString(caseQueryRequest.getCaseName())) {
            if (StringUtils.isEmpty(caseQueryRequest.getCaseName().trim())) {
                return R.error("不允许全为空格");
            }
            if (caseQueryRequest.getCaseName().length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
                return R.error("案件名称超过32位");
            }
            if (StringUtils.checkRegex_false(caseQueryRequest.getCaseName(),
                    CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
                return R.error("案件名称不符合规则");
            }
        }
        try {
            String caseName = caseQueryRequest.getCaseName();
            String caseState = caseQueryRequest.getCaseState();
            int page = caseQueryRequest.getPage();
            int rows = caseQueryRequest.getRows();
            Page<TbCaseVo> pages = new Page<>(page, rows);
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.isNotEmptyString(caseState)) {
                params.put("caseStatus", caseState);
            }
            String retCaseName = QuerySqlUtil.replaceQueryName(caseName);
            if (StringUtils.isNotEmpty(retCaseName)) {
                params.put("caseName", "%" + retCaseName + "%");
            }
            /*if(StringUtils.isNotEmptyString(objtype) && StringUtils.isNotEmptyString(resultId)){
                ResultQueryVo resultQueryVo = resultService.getResultBoById(objtype,resultId);
                if (resultQueryVo == null) {
                    return R.error("归档图片信息不存在");
                }
                result.put("createTime",resultQueryVo.getCreatetimeStr());
            }*/
            Page<TbCaseVo> pageResult = tbCaseService.selectCaseByPage(pages, params);
            result.put("page", new PageUtils(pageResult));
            //案件状态
            List<Map<Integer, String>> caseStatusList = CaseJdStatus.getAllDescAndValue();
            result.put("caseStatusList", caseStatusList);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return R.error(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "导出报告")
    @PostMapping("downLoadCaseRptDoc")
    @ApiImplicitParam(name = "lawcaseId", value = "案件Id")
    public ModelAndView downWordReport(HttpServletRequest request, HttpServletResponse response, String lawcaseId) throws Exception {
        String modelPath = CaseController.class.getResource("/").getPath() + "word/jdcase/case-word-model-001.html";
        modelPath = modelPath.substring(1);
        String modelFile = FileUtil.read(new File(modelPath));
        TbCase textData = tbCaseService.getOne(new QueryWrapper<TbCase>().eq("case_code", lawcaseId));
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/msword");

        String caseName = textData.getCaseName();
        String docFileName = caseName;

        if (StringUtils.isEmptyString(docFileName)) {
            docFileName = textData.getCaseCode();
        }
        String ua = request.getHeader("User-Agent");
        if (ua != null) {
            if ((ua.toLowerCase().indexOf("firefox") > 0) || ua.toLowerCase().indexOf("mozilla") > 0) {
                //解决火狐浏览器下载文件名乱码问题 (20150619 new)
                String userAgent = request.getHeader("User-Agent");
                byte[] bytes = userAgent.contains("MSIE") ? docFileName.getBytes() : docFileName.getBytes("UTF-8"); // name.getBytes("UTF-8")处理safari的乱码问题
                docFileName = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码
                response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", (docFileName + ".doc"))); // 文件名外的双引号处理firefox的空格截断问题
            } else {
                //设置响应头，控制浏览器下载该文件 ,仅此火狐下会出现下载文件中文乱码的问题   ，ie chrome正常
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(docFileName, "UTF-8").replace("+", "%20") + ".doc");
            }
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(docFileName, "UTF-8").replace("+", "%20") + ".doc");
        }

        String userName = textData.getCaseHandleUser();

        if (StringUtils.isNotEmptyString(caseName)) {
            caseName = java.util.regex.Matcher.quoteReplacement(caseName);
        }
        modelFile = modelFile.replaceAll("##caseName##", caseName);
        modelFile = modelFile.replaceAll("##recieveCode##", textData.getCaseCode());//${createAccount}

        modelFile = modelFile.replaceAll("##caseCode##", textData.getCaseCode());
        //System.out.println(StringUtils.isEmptyString(textData.getCaseDesc())+"--"+textData.getCaseDesc());
        String caseDesc = textData.getCaseDetail();
        if (!StringUtils.isEmptyString(caseDesc)) {
            caseDesc = java.util.regex.Matcher.quoteReplacement(caseDesc);
        }
        modelFile = modelFile.replaceAll("##caseDesc##", caseDesc == null ? "" : caseDesc);
        String caseStatus = CaseJdStatus.getDescByValue(textData.getCaseStatus());
        modelFile = modelFile.replaceAll("##caseStatus##", null == caseStatus ? "" : caseStatus);
        modelFile = modelFile.replaceAll("##caseStartTime##", null == textData.getCreateTime() ? "" : DateUtil.getFormat(textData.getCreateTime(), "yyyy年MM月dd日 HH时mm分ss秒"));


        modelFile = modelFile.replaceAll("#userName#", userName);
        modelFile = modelFile.replaceAll("#currentYmd#", DateUtil.getFormat(new Date(), "yyyy年MM月dd日"));

        // 图片线索
        String table2101Pic = FileUtil.read(new File(CaseController.class.getResource("/").getPath() + "word/jdcase/case001-table21-01.txt"));

        List<CaseArchivePicVo> clues = tbCaseArchivePicService.getCaseArchiveInfoPicList(lawcaseId);

        StringBuffer table2101BuffPic = new StringBuffer();
        String serverIp = getServerIpByRequest(request);
        int port = request.getServerPort();
        if (ListUtil.isNotNull(clues)) {
            CaseArchivePicVo caseArchivePic = null;
            for (int i = 0; i < clues.size(); i++) {
                caseArchivePic = clues.get(i);

                CameraVo camera = cameraService.selectByPrimaryKey(caseArchivePic.getCameraId().toString());
                String cameraName = "";
                if (camera != null) {
                    cameraName = camera.getName();
                }
                // 0 事前 1 事中 2 事后
                Integer happenPeriod = caseArchivePic.getHappenPeriod();
                String happenPeriodStr = "";
                if (happenPeriod != null) {
                    happenPeriodStr = happenPeriod == 0 ? "事前" : happenPeriod == 1 ? "事中" : happenPeriod == 2 ? "事后" : "";
                }
                String fileName = caseArchivePic.getFileName();
                String imgUrl = caseArchivePic.getPicThumbPath();
                table2101BuffPic.append(table2101Pic
                        .replaceAll("###recordIndex###", (i + 1) + "")
                        .replaceAll("###ImageFileName###", getStringByEnter(30, fileName))
                        .replaceAll("###recordImgUrl###", "<img border=0 width=79 height=57 src=\"" + imgUrl + "\"/>")
                        .replaceAll("###recordTime###",
                                DateUtil.getFormat(caseArchivePic.getCreateTime(),
                                        "yyyy年MM月dd日 HH时mm分ss秒"))
                        .replaceAll("###recordCname###", getStringByEnter(30, cameraName))
                        .replaceAll("###happenPeriod###", happenPeriodStr));

            }
        }
        modelFile = modelFile.replaceAll("###table221-01###", table2101BuffPic.toString());

        // 视频线索
        String table2102Video = FileUtil.read(new File(CaseController.class.getResource("/").getPath() + "word/jdcase/case001-table21-02.txt"));

        List<CaseArchiveVideoVo> videoClues = tbCaseArchiveVideoService.getCaseArchiveInfoVideoList(lawcaseId);

        StringBuffer table2102BuffVideo = new StringBuffer();
        if (ListUtil.isNotNull(videoClues)) {
            CaseArchiveVideoVo caseArchiveVideo = null;
            int index = 1;
            for (int i = 0; i < videoClues.size(); i++) {
                caseArchiveVideo = videoClues.get(i);
                // 视频的http本地地址,fileLocalPath 为空，视频下载失败或者视频正在下载中
                String fileLocalPath = caseArchiveVideo.getFileLocalPath();
                if (StringUtils.isNotEmptyString(fileLocalPath)) {
                    CameraVo camera = cameraService.selectByPrimaryKey(caseArchiveVideo.getCameraId().toString());
                    String cameraName = "";
                    if (camera != null) {
                        cameraName = camera.getName();
                    }
                    // 0 事前 1 事中 2 事后
                    Integer happenPeriod = caseArchiveVideo.getHappenPeriod();
                    String happenPeriodStr = "";
                    if (happenPeriod != null) {
                        happenPeriodStr = happenPeriod == 0 ? "事前" : happenPeriod == 1 ? "事中" : happenPeriod == 2 ? "事后" : "";
                    }
                    // 0 否 1 是
                    int isProof = caseArchiveVideo.getIsProof();
                    String isProofStr = "";
                    if (happenPeriod != null) {
                        isProofStr = isProof == 0 ? "否" : isProof == 1 ? "是" : "";
                    }
                    String videoFileName = caseArchiveVideo.getFileName();
                    String relatePictureUrl = caseArchiveVideo.getRelatePictureUrl();

                    String videoStartTime = DateUtil.getFormat(caseArchiveVideo.getVideoStartTime(), "yyyy年MM月dd日 HH时mm分ss秒");
                    String videoEndTime = DateUtil.getFormat(caseArchiveVideo.getVideoEndTime(), "yyyy年MM月dd日 HH时mm分ss秒");
                    String recordTimePeriod = videoStartTime + " ~ " + videoEndTime;

                    table2102BuffVideo.append(table2102Video
                            .replaceAll("###recordIndex###", (index++) + "")
                            .replaceAll("###VideoFileName###", getStringByEnter(30, videoFileName))
                            .replaceAll("###relatePictureUrl###", "<img border=0 width=79 height=57 src=\"" + relatePictureUrl + "\"/>")
                            .replaceAll("###recordTimePeriod###", recordTimePeriod)
                            .replaceAll("###recordCname###", getStringByEnter(30, cameraName))
                            .replaceAll("###isProof###", isProofStr)
                            .replaceAll("###happenPeriod###", happenPeriodStr));
                }

            }
        }
        modelFile = modelFile.replaceAll("###table221-02###", table2102BuffVideo.toString());


        String trackImageTag = "";

        if (StringUtil.isNotNull(textData.getTrackImageUrl())) {
            trackImageTag = "<img border=0 width=560 height=300 src=\"http://" + serverIp + ":" + port + request.getContextPath() + "/rest/getPersistPicture?id=" + textData.getTrackImageUrl() + "\" />";
        }
        modelFile = modelFile.replaceAll("####2####", trackImageTag);//轨迹图片
        byte[] content = modelFile.getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(content);

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return null;
    }

    public String getStringByEnter(int length, String str) {
        String result = str;
        try {
            for (int i = 1; i <= str.length(); i++) {
                if (str.substring(0, i).getBytes("utf-8").length > length) {
                    return str.substring(0, i - 1) + "\n" +
                            getStringByEnter(length, str.substring(i - 1));
                }
            }
        } catch (Exception e) {
            log.error("字符串换行出错！");
        }
        return result;
    }

    /**
     * 获取服务器IP　注意：如果前台是通过127.0.0.1（localhost）访问则无法获取
     *
     * @param request
     * @return
     */
    private String getServerIpByRequest(HttpServletRequest request) {
        String serverIp = "";
        if ("1".equals(PropertiesUtil.getParameterPackey("open.tecent"))) {
            serverIp = PropertiesUtil.getParameterPackey("tecent.address");
        } else {
            serverIp = request.getLocalAddr();
        }
        return serverIp;
    }

    @ApiOperation("上传案件归档数据到佳都云平台 [佳都]")
    @PostMapping("/uploadCaseData")
    @ApiImplicitParam(name = "caseCode", value = "案件的编号")
    public R uploadCaseData(String caseCode) {
        try {
            // 根据案件编号获取所有关联的图片 归档数据
            List<TbCaseArchivePic> caseArchivePics = tbCaseArchivePicService.list(new QueryWrapper<TbCaseArchivePic>().eq("case_code", caseCode).eq("deleted", 0));
            Set<Long> cameraIds = new HashSet<>();

            SysUser sysUser = getUser();
            String userId = String.valueOf(sysUser.getUserId());

            UploadDataToJdVo uploadJdBo = new UploadDataToJdVo();
            uploadJdBo.setRemoteUser(userId);
            uploadJdBo.setCaseId(caseCode);

            // 上传图片
            for (TbCaseArchivePic caseArchivePic : caseArchivePics) {
                Long cameraId = caseArchivePic.getCameraId();
                cameraIds.add(cameraId);
                packageUploadImageData(uploadJdBo, caseArchivePic);
                boolean uploadImage = uploadDataToJdService.uploadData(UploadConstants.UPLOAD_IMAGE_TYPE, uploadJdBo, sysUser);
                if (!uploadImage) {
                    log.error("上传归档数图片据到佳都云平台出错！caseCode : " + caseCode);
                    return R.error("上传归档图片数据失败！");
                }
            }
            // 根据案件编号获取归档视频数据
            List<TbCaseArchiveVideo> caseArchiveVideos = tbCaseArchiveVideoService.list(new QueryWrapper<TbCaseArchiveVideo>().eq("case_code", caseCode).eq("deleted", 0));
            // 上传视频
            for (TbCaseArchiveVideo caseArchiveVideo : caseArchiveVideos) {
                Long cameraId = caseArchiveVideo.getCameraId();
                cameraIds.add(cameraId);

                // 视频下载成功后fileLocalPath会有值
                String fileLocalPath = caseArchiveVideo.getFileLocalPath();
                if (StringUtils.isNotEmptyString(fileLocalPath)) {
                    packageUploadVideoData(uploadJdBo, caseArchiveVideo);
                    boolean uploadVideo = uploadDataToJdService.uploadData(UploadConstants.UPLOAD_VIDEO_TYPE, uploadJdBo, sysUser);

                    if (!uploadVideo) {
                        log.error("上传归档视频片据到佳都云平台出错！caseCode : " + caseCode);
                        return R.error("上传归档视频数据失败！");
                    }
                }
            }
            // 上传点位数据
            for (Long cameraId : cameraIds) {
                CameraVo camera = cameraService.selectByPrimaryKey(String.valueOf(cameraId));
                if (camera != null) {
                    packageUploadVideoPointData(uploadJdBo, camera);

                    boolean uploadVideoPoint = uploadDataToJdService.uploadData(UploadConstants.UPLOAD_VIDEO_POINT_TYPE, uploadJdBo, sysUser);
                    if (!uploadVideoPoint) {
                        log.error("上传归档点位据到佳都云平台出错！caseCode : " + caseCode);
                        return R.error("上传归档点位据失败！");
                    }
                }
            }
            if (cameraIds.size() == 0 && ListUtil.isNull(caseArchiveVideos) && ListUtil.isNull(caseArchivePics)) {
                return R.error("此案件无归档数据，请先添加归档数据！");
            }
        } catch (Exception e) {
            log.error("上传归档数据到佳都云平台出错！caseCode : " + caseCode);
            log.error(e.getMessage(), e);
            return R.error("上传失败");
        }
        return R.ok("上传成功");
    }

    /**
     * 封装图片上传数据
     *
     * @param uploadDataToJdBo
     * @param caseArchivePic
     * @return
     */
    public void packageUploadImageData(UploadDataToJdVo uploadDataToJdBo, TbCaseArchivePic caseArchivePic) {


        uploadDataToJdBo.setFileName(caseArchivePic.getFileName());
        uploadDataToJdBo.setCclj(caseArchivePic.getPicBigPath());
        // 文件来源（0 未知 1 公安来源 2 社会来源)
        uploadDataToJdBo.setFileSource("0");
        uploadDataToJdBo.setLabel(caseArchivePic.getHappenPeriod() + "");

        uploadDataToJdBo.setFileType(1);

    }

    /**
     * 封装视频上传数据
     *
     * @param uploadDataToJdBo
     * @param caseArchiveVideo
     * @return
     */
    public void packageUploadVideoData(UploadDataToJdVo uploadDataToJdBo, TbCaseArchiveVideo caseArchiveVideo) {

        uploadDataToJdBo.setFileName(caseArchiveVideo.getFileName());
        String fileRemotePath = caseArchiveVideo.getFileLocalPath();
        uploadDataToJdBo.setCclj(fileRemotePath);
        // 获取 http路径
        String relatePictureUrl = caseArchiveVideo.getRelatePictureUrl();

        uploadDataToJdBo.setSltcclj(relatePictureUrl);
        // 文件来源（0 未知 1 公安来源 2 社会来源)
        uploadDataToJdBo.setFileSource("0");
        uploadDataToJdBo.setLabel(caseArchiveVideo.getHappenPeriod() + "");

        int index = fileRemotePath.lastIndexOf(".");
        String ext = fileRemotePath.substring(index);
        uploadDataToJdBo.setFileExt(ext);
        uploadDataToJdBo.setIsEvidence(caseArchiveVideo.getIsProof());

    }

    /**
     * 封装点位上传数据
     *
     * @param uploadDataToJdBo
     * @param camera
     * @return
     */
    public void packageUploadVideoPointData(UploadDataToJdVo uploadDataToJdBo, CameraVo camera) {

        uploadDataToJdBo.setDeviceName(camera.getName());
        uploadDataToJdBo.setDeviceCode(String.valueOf(camera.getId()));

        if (StringUtils.isNotEmptyString(camera.getAddress())) {
            uploadDataToJdBo.setDeviceAddr(camera.getAddress());
        }
        String longitude = camera.getLongitude();
        if (StringUtils.isEmptyString(longitude)) {
            uploadDataToJdBo.setLongitude(0);
        } else {
            uploadDataToJdBo.setLongitude(Float.parseFloat(longitude));
        }

        String latitude = camera.getLatitude();
        if (StringUtils.isEmptyString(latitude)) {
            uploadDataToJdBo.setLatitude(0);
        } else {
            uploadDataToJdBo.setLatitude(Float.parseFloat(latitude));
        }

    }

    @ApiOperation("添加到案件【佳都】")
    @PostMapping(value = "/saveCaseArchive")
    public R saveCaseArchive(@RequestBody CaseArchiveVo caseArchiveVo) {
        try {
            String objtype = caseArchiveVo.getObjtype();
            String resultId = caseArchiveVo.getResultId();

            ResultQueryVo resultBo = resultService.getResultBoById(objtype, resultId);
            if (resultBo == null) {
                return R.error("图片信息不存在");
            }
            // 案件编号
            String caseCode = caseArchiveVo.getCaseCode();

            if (StringUtils.isEmptyString(caseCode)) {
                return R.error("案件编号为空");
            }
            SysUser sysUser = getUser();
            //判断图片是否已经添加
            String picName = caseArchiveVo.getPicName();
            String picState = caseArchiveVo.getPicState();
            // 图片归档数据
            TbCaseArchivePic caseArchivePic = null;
            if (StringUtils.isNotEmpty(picName) && StringUtils.isNotEmpty(picState)) {
                caseArchivePic = this.packageQueryImageTbCaseArchive(caseCode, resultBo);

                if (picName.length() > 50) {
                    return R.error("图片名称太长！");
                }

                int count = tbCaseArchivePicService.selectExistsArchivePic(caseArchivePic);
                if (count >= 1) {
                    return R.error("该图片已在此案件中！");
                }
            } else {
                return R.error("图片名称和发生时间必填！");
            }
            //判断视频是否已经添加
            String videoName = caseArchiveVo.getVideoName();
            String videoState = caseArchiveVo.getVideoState();
            String videoProof = caseArchiveVo.getVideoProof();
            String videoStartTime = caseArchiveVo.getVideoStartTime();
            String videoEndTime = caseArchiveVo.getVideoEndTime();

            TbCaseArchiveVideo caseArchiveVideo = null;
            // 视频归档数据
            Boolean insertFlag = true;
            if (StringUtils.isNotEmpty(videoName) && StringUtils.isNotEmpty(videoState) &&
                    StringUtils.isNotEmpty(videoProof) && StringUtils.isNotEmpty(videoStartTime) &&
                    StringUtils.isNotEmpty(videoEndTime)) {
                if (videoName.length() > 50) {
                    return R.error("视频名称太长！");
                }

                caseArchiveVideo = this.packageQueryVideoTbCaseArchive(caseArchiveVo, resultBo);
                // 检查此视频片段是否已经存在，若存在，则更新视频关联的图片封面
                TbCaseArchiveVideo cav = tbCaseArchiveVideoService.selectExistsArchiveVideo(caseArchiveVideo);
                if (cav != null) {
                    // 更新视频的封面图片
                    String relatePicturePath = uploadDataToJdService.saveImage(caseArchiveVo.getCaseCode(), resultBo.getImgurl(), UploadConstants.VIDEO_COVER_TYPE);
                    cav.setRelatePictureUrl(relatePicturePath);
                    cav.setFileName(videoName);
                    cav.setHappenPeriod(Integer.parseInt(videoState));
                    cav.setIsProof(Integer.parseInt(videoProof));
                    tbCaseArchiveVideoService.updateById(cav);
                    insertFlag = false;
                }

            }

            String userName = sysUser.getUsername();
            // 添加视频
            if (caseArchiveVideo != null && insertFlag) {
                Long cameraId = caseArchiveVideo.getCameraId();
                CameraVo camera = cameraService.selectByPrimaryKey(String.valueOf(cameraId));
                String vasUrl = camera.getUrl();

                if (StringUtils.isNotEmptyString(vasUrl) && vasUrl.contains("vas://")) {
                    caseArchiveVideo = this.getCaseVideoArchive(caseArchiveVo, userName, resultBo);
                    tbCaseArchiveVideoService.insertCaseArchiveVideo(caseArchiveVideo);
                } else {
                    return R.error("此监控点视频不能下载！");
                }
            }
            //添加图片
            if (caseArchivePic != null) {
                caseArchivePic = this.getCaseImageArchive(caseArchiveVo, userName, resultBo);
                log.info("添加关联的图片数据----> caseArchivePic : " + JSON.toJSONString(caseArchivePic));
                caseArchivePic.setDeleted(0);
                caseArchivePic.setVersion(0);
                tbCaseArchivePicService.save(caseArchivePic);
            }
        } catch (Exception e) {
            log.error("案件归档出错 " + e);
            return R.error("操作失败,请重试！");
        }
        return R.ok();
    }

    /**
     * 封装案视频关联对象，用于校验同一个视频片段重复关联案件[佳都]
     */
    private TbCaseArchiveVideo packageQueryVideoTbCaseArchive(CaseArchiveVo caseArchiveVo, ResultQueryVo resultBo) {
        String videoStartTime = caseArchiveVo.getVideoStartTime();
        String videoEndTime = caseArchiveVo.getVideoEndTime();
        TbCaseArchiveVideo caseArchive = new TbCaseArchiveVideo();
        caseArchive.setCaseCode(caseArchiveVo.getCaseCode());
        caseArchive.setVideoStartTime(DateTimeUtils.formatDate(videoStartTime, DateTimeUtils.DEFAULT_FORMAT_DATE));
        caseArchive.setVideoEndTime(DateTimeUtils.formatDate(videoEndTime, DateTimeUtils.DEFAULT_FORMAT_DATE));
        caseArchive.setSerialnumber(resultBo.getSerialnumber());
        if (StringUtils.isNotEmptyString(resultBo.getCameraId())) {
            caseArchive.setCameraId(Long.parseLong(resultBo.getCameraId()));
        }
        caseArchive.setDeleted(0);
        return caseArchive;
    }


    /**
     * 封装案件图片关联对象，用于保存[佳都]
     */
    private TbCaseArchiveVideo getCaseVideoArchive(CaseArchiveVo caseArchiveVo, String userName, ResultQueryVo resultBo) {
        String videoName = caseArchiveVo.getVideoName();
        String videoState = caseArchiveVo.getVideoState();
        String videoProof = caseArchiveVo.getVideoProof();
        String videoStartTime = caseArchiveVo.getVideoStartTime();
        String videoEndTime = caseArchiveVo.getVideoEndTime();


        TbCaseArchiveVideo caseArchive = new TbCaseArchiveVideo();
        caseArchive.setCaseCode(caseArchiveVo.getCaseCode());
        caseArchive.setFileName(videoName);
        caseArchive.setHappenPeriod(Integer.parseInt(videoState));
        caseArchive.setIsProof(Integer.parseInt(videoProof));
        caseArchive.setVideoStartTime(DateTimeUtils.formatDate(videoStartTime, DateTimeUtils.DEFAULT_FORMAT_DATE));
        caseArchive.setVideoEndTime(DateTimeUtils.formatDate(videoEndTime, DateTimeUtils.DEFAULT_FORMAT_DATE));

        caseArchive.setSerialnumber(resultBo.getSerialnumber());

        // 视频关联的图片地址
        String relatePicturePath = uploadDataToJdService.saveImage(caseArchiveVo.getCaseCode(), resultBo.getImgurl(), UploadConstants.VIDEO_COVER_TYPE);

        caseArchive.setRelatePictureUrl(relatePicturePath);

        caseArchive.setFileType(1);
        caseArchive.setCameraId(Long.parseLong(resultBo.getCameraId()));
        caseArchive.setCreateUser(userName);
        caseArchive.setCreateTime(new Date());
        caseArchive.setLastUpdateTime(new Date());

        return caseArchive;
    }

    /**
     * 封装案件图片关联对象，用于校验同一张图片重复关联案件[佳都]
     */
    private TbCaseArchivePic packageQueryImageTbCaseArchive(String caseCode, ResultQueryVo resultBo) {
        TbCaseArchivePic caseArchive = new TbCaseArchivePic();
        caseArchive.setCaseCode(caseCode);
        caseArchive.setSerialnumber(resultBo.getSerialnumber());
        caseArchive.setPicId(resultBo.getId());
        caseArchive.setObjType(Integer.valueOf(resultBo.getObjtype()));

        if (StringUtils.isNotEmptyString(resultBo.getCameraId())) {
            caseArchive.setCameraId(Long.parseLong(resultBo.getCameraId()));
        }
        //图片出现的时间
        caseArchive.setCreateTime(resultBo.getCreatetime());

        caseArchive.setDeleted(0);

        return caseArchive;
    }

    /**
     * 封装案件图片关联对象，用于保存[佳都]
     */
    private TbCaseArchivePic getCaseImageArchive(CaseArchiveVo caseArchiveVo, String userName, ResultQueryVo resultBo) {
        TbCaseArchivePic caseArchive = new TbCaseArchivePic();
        String caseCode = caseArchiveVo.getCaseCode();
        caseArchive.setCaseCode(caseCode);
        caseArchive.setSerialnumber(resultBo.getSerialnumber());

        // 目标图片的大图和小图，需要转换到/case 路径 持久化保存
        String picThumbPath = uploadDataToJdService.saveImage(caseCode, resultBo.getImgurl(), UploadConstants.PIC_THUMB_TYPE);
        String picBigPath = uploadDataToJdService.saveImage(caseCode, resultBo.getBigImgurl(), UploadConstants.PIC_BG_TYPE);

        caseArchive.setPicThumbPath(picThumbPath);
        caseArchive.setPicBigPath(picBigPath);

        caseArchive.setPicId(resultBo.getId());
        String resultBoJsonStr = JSON.toJSONString(resultBo);
        caseArchive.setPicInfo(resultBoJsonStr);


        //图片出现的时间
        caseArchive.setCreateTime(resultBo.getCreatetime());
        caseArchive.setCreateUser(userName);
        caseArchive.setLastUpdateTime(new Date());
        caseArchive.setObjType(Integer.valueOf(resultBo.getObjtype()));

        // 文件名称和类型
        caseArchive.setFileName(caseArchiveVo.getPicName());

        //发生的时间
        caseArchive.setHappenPeriod(Integer.parseInt(caseArchiveVo.getPicState()));

        //获取视频记录点ID
        String cameraId = resultBo.getCameraId();
        if (StringUtils.isNotEmptyString(cameraId)) {
            caseArchive.setCameraId(Long.valueOf(cameraId));
        }

        return caseArchive;
    }

}
