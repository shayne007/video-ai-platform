package com.keensense.admin.controller.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.config.ServerConfig;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.entity.lawcase.AppLawcaseBasicoption;
import com.keensense.admin.entity.lawcase.AppPoliceComprehensive;
import com.keensense.admin.entity.lawcase.CaseCameraMedia;
import com.keensense.admin.entity.sys.SysUser;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.request.CaseQueryRequest;
import com.keensense.admin.request.PoliceComprehensiveRequest;
import com.keensense.admin.service.lawcase.IAppLawcaseBasicoptionService;
import com.keensense.admin.service.lawcase.IAppPoliceComprehensiveService;
import com.keensense.admin.service.lawcase.ICaseCameraMediaService;
import com.keensense.admin.service.task.CaseService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.service.util.ImageService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.FileUtils;
import com.keensense.admin.util.IpUtils;
import com.keensense.admin.util.PropertiesUtil;
import com.keensense.admin.util.QuerySqlUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CaseCameraMediaVo;
import com.keensense.admin.vo.PoliceComprehensiveVo;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.admin.enums.CaseCommonState;
import com.keensense.common.util.ImageUtils;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 案件录入控制器
 **/
@Slf4j
@RestController
@RequestMapping("/lawCase")
@Api(tags = "案件管理-常规")
public class CaseController extends BaseController {
    @Resource
    private CaseService caseService;

    @Resource
    private ICameraService cameraService;

    @Resource
    private ResultService resultService;

    @Resource
    private IAppPoliceComprehensiveService appPoliceComprehensiveService;

    @Resource
    private IAppLawcaseBasicoptionService appLawcaseBasicoptionService;

    @Resource
    private ICaseCameraMediaService caseCameraMediaService;

    @Resource
    private ImageService imageService;

    @Resource
    private ServerConfig serverConfig;

    @ApiOperation(value = "新增案件")
    @PostMapping(value = "/submitCase")
    public R submitCase(@RequestBody PoliceComprehensiveRequest police){
        try {
            if (StringUtils.isEmpty(police.getCaseName())) {
                return R.error("案件名称不能为空");
            }
            if (StringUtils.isEmpty(police.getCaseName().trim())) {
                return R.error("不允许全为空格");
            }
            if (police.getCaseName().length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
                return R.error("案件名称超过32位");
            }
            if (StringUtils.checkRegex_false(police.getCaseName(),
                    CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
                return R.error("案件名称不符合规则");
            }
            if (StringUtils.isNotEmptyString(police.getLocationName()) &&
                    police.getLocationName().length() > CommonConstants.REGEX.CASE_LOCATION_NAME_LENGTH) {
                return R.error("案件地点名称过长");
            }
            if (StringUtils.isNotEmptyString(police.getCaseDesc()) &&
                    police.getCaseDesc().length() > CommonConstants.REGEX.CASE_DESC_LENGTH) {
                return R.error("案件简介过长");
            }
            Date startTime = police.getStartTime();
            if (startTime != null && startTime.after(new Date())){
                return R.error("案发时间不能大于当前时间");
            }
            //police.setLawcaseId(System.currentTimeMillis() + "");
            police.setCreateTime(new Date());
            police.setCaseState(String.valueOf(CaseCommonState.PROCESS.value));
            SysUser sysUser = getUser();
            police.setCreatorId(sysUser.getUserId());
            AppPoliceComprehensive appPoliceComprehensive = EntityObjectConverter.getObject(police, AppPoliceComprehensive.class);
            caseService.addCase(appPoliceComprehensive);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    /**
     * 案件详情
     */
    @ApiOperation(value = "案件详情")
    @PostMapping(value = "/queryBaseCaseInfo")
    @ApiImplicitParam(name = "lawcaseId", value = "案件Id")
    public R queryBaseCaseInfo(String lawcaseId){
        R result = R.ok();
        try {
            AppPoliceComprehensive policeComprehensive = appPoliceComprehensiveService.getById(lawcaseId);
            PoliceComprehensiveVo lawCase = EntityObjectConverter.getObject(policeComprehensive, PoliceComprehensiveVo.class);
            if (lawCase.getStartTime() != null) {
                lawCase.setStartTimeStr(DateTimeUtils.formatDate(lawCase.getStartTime(), null));
            }
            if (lawCase.getEndTime() != null) {
                lawCase.setEndTimeStr(DateTimeUtils.formatDate(lawCase.getEndTime(), null));
            }
            List<CaseCameraMediaVo> clues = caseService.selectCluesByCaseId(lawcaseId);
            if (StringUtils.isNotEmptyString(lawCase.getCategoryValue())) {
                AppLawcaseBasicoption appLawcaseBasicoption = appLawcaseBasicoptionService.getOne(new QueryWrapper<AppLawcaseBasicoption>().eq("OPTIONS_VALUE", lawCase.getCategoryValue()));
                if (appLawcaseBasicoption != null){
                    String categoryName = appLawcaseBasicoption.getOptionsName();
                    lawCase.setCategory(categoryName);
                }
            }
            result.put("lawCase", lawCase);
            result.put("clues", clues);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "编辑案件")
    @PostMapping(value = "/updateCase")
    @ApiImplicitParam(name = "lawcaseId", value = "案件Id")
    public R updateCase(@RequestBody PoliceComprehensiveRequest police){
        try {
            if (StringUtils.isEmpty(police.getCaseName().trim())) {
                return R.error("不允许全为空格");
            }
            if (police.getCaseName().length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
                return R.error("案件名称超过32位");
            }
            if (StringUtils.checkRegex_false(police.getCaseName(),
                    CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
                return R.error("案件名称不符合规则");
            }
            if (StringUtils.isNotEmptyString(police.getLocationName()) &&
                    police.getLocationName().length() > CommonConstants.REGEX.CASE_LOCATION_NAME_LENGTH) {
                return R.error("案件地点名称过长");
            }
            if (StringUtils.isNotEmptyString(police.getCaseDesc()) &&
                    police.getCaseDesc().length() > CommonConstants.REGEX.CASE_DESC_LENGTH) {
                return R.error("案件简介过长");
            }
            Date startTime = police.getStartTime();
            if (startTime != null && startTime.after(new Date())){
                return R.error("案发时间不能大于当前时间");
            }
            Date endTime = police.getEndTime();
            if (startTime != null && endTime != null && startTime.after(endTime)){
                return R.error("案发时间不能大于结案时间");
            }
            AppPoliceComprehensive appPoliceComprehensive = EntityObjectConverter.getObject(police, AppPoliceComprehensive.class);
            caseService.updateCase(appPoliceComprehensive);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok();
    }


    /**
     * 结案
     */
    @ApiOperation(value = "结案")
    @PostMapping(value = "/closeCase")
    @ApiImplicitParam(name = "lawcaseId", value = "案件Id")
    public R closeCase(String lawcaseId) {
        try {
            caseService.closeCase(lawcaseId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    /**
     * 删除案件
     */
    @ApiOperation(value = "删除案件")
    @PostMapping(value = "/deleteCase")
    @ApiImplicitParam(name = "lawcaseId", value = "案件Id")
    public R deleteCase(String lawcaseId){
        try {
            caseService.deleteCase(lawcaseId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    /**
     * 删除案件图片
     *
     */
    @ApiOperation(value = "图侦线索图片删除")
    @PostMapping(value = "/deleteClue")
    @ApiImplicitParam(name = "clueId", value = "clueId")
    public R deleteClue(String clueId) {
        try {
            caseService.deleteClue(clueId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    @ApiOperation(value = "目标轨迹")
    @PostMapping(value = "/selectByList")
    @ApiImplicitParam(name = "lawcaseId", value = "案件Id")
    public R selectayList(String lawcaseId){
        R result = R.ok();
        try {
            List<CaseCameraMediaVo> clues = caseService.queryCurrentTaskCameras(lawcaseId);
            result.put("clues", clues);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "保存目标轨迹")
    @PostMapping("/saveTrackImage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lawcaseId", value = "案件Id"),
            @ApiImplicitParam(name = "base64Data", value = "base64Data数据")
    })
    public R saveImage(String lawcaseId, String base64Data){
        R result = R.ok();
        try{
            if (StringUtils.isEmptyString(base64Data)){
                return R.error("base64Data数据不能为空");
            }
            if (StringUtils.isEmptyString(lawcaseId)){
                return R.error("案件Id不能为空");
            }
            String persistPictureId = caseService.addPersistPicture(base64Data);
            if (StringUtil.isNotNull(persistPictureId)) {
                AppPoliceComprehensive appPoliceComprehensive = new AppPoliceComprehensive();
                appPoliceComprehensive.setTrackImageUrl(persistPictureId);
                boolean update = appPoliceComprehensiveService.update(appPoliceComprehensive, new QueryWrapper<AppPoliceComprehensive>().eq("LAWCASE_ID", lawcaseId));
                if (update) {
                    result.put("result", persistPictureId);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return result;
    }

    /**
     * 查任务总数
     * 录入员只能看到自己录入的任务
     * 审核员能看到已经审核完成的任务
     */
    @ApiOperation(value = "查询案件（列表/名称/状态）")
    @PostMapping(value = "/queryCasePage")
    public R queryCasePage(@RequestBody CaseQueryRequest caseQueryRequest) {
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
        R result = R.ok();
        try {
            String caseName = caseQueryRequest.getCaseName();
            String caseState = caseQueryRequest.getCaseState();
            int page = caseQueryRequest.getPage();
            int rows = caseQueryRequest.getRows();
            Page<PoliceComprehensiveVo> pages = new Page<PoliceComprehensiveVo>(page, rows);
            Map<String, Object> params = new HashMap<String, Object>();
            if (StringUtils.isNotEmptyString(caseState)) {
                params.put("caseState", caseState);
            }
            String retCaseName = QuerySqlUtil.replaceQueryName(caseName);
            if (StringUtils.isNotEmpty(caseName)) {
                params.put("caseName", "%" + retCaseName + "%");
            }
            Page<PoliceComprehensiveVo> pageResult = caseService.selectCaseByPage(pages, params);
            result.put("page", new PageUtils(pageResult));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return R.error(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "导出报告")
    @GetMapping("downLoadCaseRptDoc")
    @ApiImplicitParam(name = "lawcaseId", value = "案件Id")
    public R downWordReport(HttpServletRequest request, HttpServletResponse response,String lawcaseId) throws Exception {
        //String modelPath = CaseController.class.getResource("/").getPath() + "word/case-word-model-001.html";
        //modelPath = modelPath.substring(1);
        //String modelFile = FileUtil.read(new File(modelPath));
        String modelFile = FileUtils.getFileString("word/case-word-model-001.html");
        AppPoliceComprehensive textData = appPoliceComprehensiveService.getById(lawcaseId);
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/msword");

        String caseName = textData.getCaseName();

        String ua = request.getHeader("User-Agent");
        if (ua != null) {
            if ((ua.toLowerCase().indexOf("firefox") > 0) || ua.toLowerCase().indexOf("mozilla") > 0) {
                //解决火狐浏览器下载文件名乱码问题 (20150619 new)
                String userAgent = request.getHeader("User-Agent");
                byte[] bytes = userAgent.contains("MSIE") ? caseName.getBytes() : caseName.getBytes("UTF-8"); // name.getBytes("UTF-8")处理safari的乱码问题
                caseName = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码
                response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", (caseName + ".doc"))); // 文件名外的双引号处理firefox的空格截断问题
            } else {
                //设置响应头，控制浏览器下载该文件 ,仅此火狐下会出现下载文件中文乱码的问题   ，ie chrome正常
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(caseName, "UTF-8").replace("+", "%20") + ".doc");
            }
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(caseName, "UTF-8").replace("+", "%20") + ".doc");
        }
        SysUser sysUser = getUser();
        String userName = null == sysUser ? "" : sysUser.getRealName();
        if( ua != null && ( (ua.toLowerCase().indexOf("firefox") > 0) ||  (ua.toLowerCase().indexOf("mozilla") > 0)) ){
            byte[] bytes = caseName.getBytes("ISO-8859-1");
            caseName = new String(bytes,"UTF-8");
        }

        if(StringUtils.isNotEmptyString(caseName)){
            caseName = java.util.regex.Matcher.quoteReplacement(caseName);
        }
        modelFile = modelFile.replaceAll("##caseName##", caseName);
        modelFile = modelFile.replaceAll("##recieveCode##", textData.getLawcaseId());//${createAccount}

        modelFile = modelFile.replaceAll("##caseCode##", textData.getLawcaseId());
        String caseDesc = textData.getCaseDesc();
        if(!StringUtils.isEmptyString(caseDesc)){
            caseDesc = java.util.regex.Matcher.quoteReplacement(caseDesc);
        }
        modelFile = modelFile.replaceAll("##caseDesc##", caseDesc==null?"":caseDesc);
        AppLawcaseBasicoption appLawcaseBasicoption = appLawcaseBasicoptionService.getOne(new QueryWrapper<AppLawcaseBasicoption>().eq("OPTIONS_VALUE", textData.getCategoryValue()));
        if (appLawcaseBasicoption != null){
            modelFile = modelFile.replaceAll("##caseTypeName##", null ==  appLawcaseBasicoption.getOptionsName() ? "" :  appLawcaseBasicoption.getOptionsName());
        }else{
            modelFile = modelFile.replaceAll("##caseTypeName##","");
        }
        modelFile = modelFile.replaceAll("##caseStartTime##", null == textData.getStartTime() ? "" : DateUtil.getFormat(textData.getStartTime(), "yyyy年MM月dd日 HH时mm分ss秒"));
        modelFile = modelFile.replaceAll("##caseEndTime##", null == textData.getEndTime() ? "" : DateUtil.getFormat(textData.getEndTime(), "yyyy年MM月dd日 HH时mm分ss秒"));
        String locationDetail = textData.getLocationDetail();
        if(StringUtils.isNotEmptyString(locationDetail)){
            locationDetail = java.util.regex.Matcher.quoteReplacement(locationDetail);
        }
        modelFile = modelFile.replaceAll("##casePlace##", locationDetail==null?"":locationDetail);
        modelFile = modelFile.replaceAll("##casePoints##", (null == textData.getLontitude() ? "" : textData.getLontitude()) + "," + (null == textData.getLatitude() ? "" : textData.getLatitude()));

        modelFile = modelFile.replaceAll("#userName#", userName);
        modelFile = modelFile.replaceAll("#currentYmd#", DateUtil.getFormat(new Date(), "yyyy年MM月dd日"));

        String table2101Temp = FileUtils.getFileString("word/case001-table21-01.txt");
        //List<CaseCameraMediaVo> clues = caseService.selectCluesByCaseId(lawcaseId);
        List<CaseCameraMedia> clues = caseCameraMediaService.list(new QueryWrapper<CaseCameraMedia>().eq("lawcaseId", lawcaseId).orderByAsc("C7"));
        StringBuffer table2101Buff = new StringBuffer();
        if (ListUtil.isNotNull(clues)) {
            CaseCameraMedia cmedia = null;
            for (int i = 0; i < clues.size(); i++) {
                cmedia = clues.get(i);
                String imgUrl = "http://" + IpUtils.getRealIpAddr() + ":" + request.getLocalPort() + request.getContextPath() + "/getPersistPicture?id=" + cmedia.getFileNameafterupload();
                log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<获取图片url路径>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + imgUrl);
                table2101Buff.append(table2101Temp
                        .replaceAll("###recordIndex###", (i + 1) + "")
                        .replaceAll("###recordImgUrl###", "<img border=0 width=79 height=57 src='" + imgUrl + "'/>")
                        .replaceAll("###recordTime###",
                                DateUtil.getFormat(cmedia.getC7(),
                                        "yyyy年MM月dd日 HH时mm分ss秒"))
                        .replaceAll("###recordCname###", cmedia.getC3()));
            }
        }
        modelFile = modelFile.replaceAll("###table221-01###", table2101Buff.toString());

        List<CaseCameraMediaVo> data = caseService.selectByList(lawcaseId);
        modelFile = modelFile.replaceAll("###caseCamera###", buildCameraBody(data));

        //modelFile = modelFile.replaceAll("####1####", buildCuleByType("1", lawcaseId, basePath));
        String trackImageTag = "";
        if (StringUtil.isNotNull(textData.getTrackImageUrl())) {
            /**
            String picture = caseService.getPictureStr(textData.getTrackImageUrl());
            String imgUrl = serverConfig.getUrl() + request.getContextPath() + "/getPersistPicture?id=" + textData.getTrackImageUrl();
            picture = Base64Encoder.encode(picture.getBytes()).replace("\n", "\r\n").replace("\r\r\n", "\r\n");;
            trackImageTag = "<img border=0 width=560 height=300 src= 'data:image/png;base64," + picture + "'/>";
             */
            String imgUrl = "http://" + IpUtils.getRealIpAddr() + ":" + request.getLocalPort() + request.getContextPath() + "/getPersistPicture?id=" + textData.getTrackImageUrl();
            trackImageTag = "<img border=0 width=560 height=300 src=\"" + imgUrl + "\"/>";
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
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null){
                bis.close();
            }
            if (bos != null){
                bos.close();
            }
        }
        return null;
    }

    /**
     * 添加到案件
     *
     * @param objtype
     * @param resultId
     * @param lawcaseId
     * @param caseName
     * @return
     */
    @ApiOperation(value = "添加到案件")
    @PostMapping(value = "/saveToCase")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "objtype", value = "objextType 1"),
            @ApiImplicitParam(name = "resultId", value = "uuid"),
            @ApiImplicitParam(name = "lawcaseId", value = "案件Id"),
            @ApiImplicitParam(name = "caseName", value = "案件名称")
    })
    public Map<String, Object> saveToCase(String objtype, String resultId, String lawcaseId, String caseName) {
        try {
            //若案件不存在则添加案件
            SysUser sysUser = getUser();
            if (StringUtils.isEmptyString(lawcaseId) && StringUtils.isNotEmpty(caseName)) {
                lawcaseId = addPoliceComprehensive(sysUser.getUserId(),caseName);
            }
            if (StringUtils.isEmptyString(lawcaseId)) {
                return R.error("案件编号为空");
            }

            String[] uuidList = resultId.split(",");
            String[] objtypeList = objtype.split(",");
            for (int i = 0; i < uuidList.length; i++) {
                String uuidStr = uuidList[i];
                String objtypeStr = objtypeList[i];
                ResultQueryVo resultBo = resultService.getResultBoById(objtypeStr,uuidStr);
                if (resultBo == null) {
                    continue;
                }
                CaseCameraMedia cameraMedia = null;
                cameraMedia = this.packageQueryCaseCameraMedia(lawcaseId,sysUser.getUserId(),resultBo);
                Integer count = caseService.selectExistsImage(cameraMedia);
                if (count >= 1) {
                    continue;
                }
                String dateNow = DateUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
                cameraMedia = this.getCaseCameraMedia(lawcaseId,sysUser.getUserId(),dateNow,resultBo);
                cameraMedia.setC5(objtypeStr);
                cameraMedia.setFileNameafterupload(caseService.addPersistPicture(ImageUtils.getURLImage(resultBo.getImgurl())));
                cameraMedia.setFileBigImage(caseService.addPersistPicture(ImageUtils.getURLImage(resultBo.getBigImgurl())));
                cameraMedia.setPicInfo(JSON.toJSONString(resultBo));
                // 图片的ID
                cameraMedia.setC8(resultBo.getResultId());
                caseCameraMediaService.save(cameraMedia);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return R.error("操作失败！");
        }
        return R.ok();
    }

    /**添加案件信息*/
    private String addPoliceComprehensive(Long userId,String caseName){
        AppPoliceComprehensive police = new AppPoliceComprehensive();
        police.setLawcaseId(System.currentTimeMillis() + "");
        police.setCaseName(caseName);
        police.setCreateTime(new Date());
        police.setCaseState(String.valueOf(CaseCommonState.PROCESS.value));
        police.setCreatorId(new BigDecimal(userId));
        appPoliceComprehensiveService.save(police);
        return police.getLawcaseId();
    }



    /**封装案件图片关联对象，用于校验同一张图片重复关联案件*/
    private CaseCameraMedia packageQueryCaseCameraMedia(String lawcaseId,Long userId,ResultQueryVo resultQueryVo){
        CaseCameraMedia cameraMedia = new CaseCameraMedia();
        cameraMedia.setLawcaseid(lawcaseId);
        cameraMedia.setC4(resultQueryVo.getSerialnumber());
        cameraMedia.setFilePathafterupload(resultQueryVo.getImgurl());
        cameraMedia.setC3(resultQueryVo.getCameraName());
        cameraMedia.setC5(resultQueryVo.getObjtype()+"");
        //监控点ID
        cameraMedia.setC6(resultQueryVo.getCameraId());
        //图片出现的时间
        cameraMedia.setC7(resultQueryVo.getCreatetimeStr());
        cameraMedia.setCreateUserid(userId.toString());
        return cameraMedia;
    }

    /**获取案件图片关联对象*/
    private CaseCameraMedia getCaseCameraMedia(String lawcaseId,Long userId,String dateNow,ResultQueryVo resultQueryVo){
        CaseCameraMedia cameraMedia = new CaseCameraMedia();
        cameraMedia.setLawcaseid(lawcaseId);
        cameraMedia.setC4(resultQueryVo.getSerialnumber());
        cameraMedia.setFilePathafterupload(resultQueryVo.getImgurl());
        cameraMedia.setId(RandomUtils.get24TimeRandom());
        cameraMedia.setC3(resultQueryVo.getCameraName());
        cameraMedia.setC5(resultQueryVo.getObjtype() + "");
        //监控点ID;
        String cameraId = resultQueryVo.getCameraId();
        cameraMedia.setC6(cameraId);
        //监控点被删后展示，需把经纬度存起来
        Camera camera = cameraService.getById(cameraId);
        if (camera != null){
            cameraMedia.setLatitude(camera.getLatitude());
            cameraMedia.setLongitude(camera.getLongitude());
        }
        //图片出现的时间
        cameraMedia.setC7(resultQueryVo.getCreatetimeStr());
        cameraMedia.setCreateUserid(userId.toString());
        cameraMedia.setCreateTime(dateNow);
        return cameraMedia;
    }

    private String getBasePath(HttpServletRequest request) {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        return basePath;
    }

    private String buildCameraBody(List<CaseCameraMediaVo> list) {
        String html = "";
        for (CaseCameraMediaVo bo : list) {
            html += "<tr style='mso-yfti-irow:1;height:1.0cm'>";
            html += "  <td width=139 style='width:104.45pt;border:solid black 1.0pt;border-top:none;";
            html += "  mso-border-top-alt:solid black .5pt;mso-border-alt:solid black .5pt;";
            html += "  padding:0cm 5.4pt 0cm 5.4pt;height:1.0cm'>";
            html += "<p class=MsoNormal align=center style='margin-right:-5.4pt;text-align:center'>";
            html += "		  <span style='mso-bidi-font-size:10.5pt;";
            html += "		  font-family:宋体;mso-ascii-font-family:'Times New Roman';mso-hansi-font-family:";
            html += "		  'Times New Roman''>" + bo.getC3() + "</span><b style='mso-bidi-font-weight:normal'><span";
            html += "		  lang=EN-US style='mso-bidi-font-size:10.5pt;font-family:'Times New Roman','serif''><o:p></o:p></span></b></p>";
            html += "  </td>";
            html += "  <td width=139 style='width:104.45pt;border:solid black 1.0pt;border-top:none;";
            html += "  mso-border-top-alt:solid black .5pt;mso-border-alt:solid black .5pt;";
            html += "  padding:0cm 5.4pt 0cm 5.4pt;height:1.0cm'>";
            html += "<p class=MsoNormal align=center style='margin-right:-5.4pt;text-align:center'>";
            html += "		  <span style='mso-bidi-font-size:10.5pt;";
            html += "		  font-family:宋体;mso-ascii-font-family:'Times New Roman';mso-hansi-font-family:";
            html += "		  'Times New Roman''>" + bo.getC8() + "~" + bo.getC7() + "</span><b style='mso-bidi-font-weight:normal'><span";
            html += "		  lang=EN-US style='mso-bidi-font-size:10.5pt;font-family:'Times New Roman','serif''><o:p></o:p></span></b></p>";
            html += "  </td>";
            html += "  <td width=139 style='width:104.45pt;border:solid black 1.0pt;border-top:none;";
            html += "  mso-border-top-alt:solid black .5pt;mso-border-alt:solid black .5pt;";
            html += "  padding:0cm 5.4pt 0cm 5.4pt;height:1.0cm'>";
            html += "<p class=MsoNormal align=center style='margin-right:-5.4pt;text-align:center'>";
            html += "		  <span style='mso-bidi-font-size:10.5pt;";
            html += "		  font-family:宋体;mso-ascii-font-family:'Times New Roman';mso-hansi-font-family:";
            html += "		  'Times New Roman''>" + bo.getFileName() + "</span><b style='mso-bidi-font-weight:normal'><span";
            html += "		  lang=EN-US style='mso-bidi-font-size:10.5pt;font-family:'Times New Roman','serif''><o:p></o:p></span></b></p>";
            html += "  </td>";
            html += "  <td width=105 style='width:78.75pt;border-top:none;border-left:none;";
            html += "  border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:";
            html += "	  solid black .5pt;mso-border-left-alt:solid black .5pt;mso-border-alt:solid black .5pt;";
            html += "  padding:0cm 5.4pt 0cm 5.4pt;height:1.0cm'>";
            html += "	  </td>";
            html += " </tr>";
        }

        return html;
    }

    private String buildCuleByType(String type, String lawcaseId, String basePath) {
        List<CaseCameraMediaVo> data = caseService.selectCluesByCaseId(lawcaseId);
        String html = "";
        for (CaseCameraMediaVo imageClue : data) {
            String picture = caseService.getPictureStr(imageClue.getFileNameafterupload());
            html += "<img src='data:image/png;base64," + picture + "'/>";
            html += "<p><o:p></o:p></p>";

            html += "<p class=MsoListParagraph align=left style='margin-right:1.3pt;text-align:left;";
            html += "		text-indent:32.0pt;line-height:28.0pt;mso-line-height-rule:exactly'><st1:chsdate";
            html += "		IsROCDate='False' IsLunarDate='False' Day='30' Month='9' Year='2014' w:st='on'><span";
            html += "		 lang=EN-US style='font-size:16.0pt;font-family:'Times New Roman','serif';";
            html += "		 mso-fareast-font-family:仿宋_GB2312'>点位名称：" + (StringUtils.isEmptyString(imageClue.getC3()) ? "未知" : imageClue.getC3()) + "</span></b></p>";
            html += "		 <p class=MsoListParagraph align=left style='margin-right:1.3pt;text-align:left;";
            html += "		text-indent:32.0pt;line-height:28.0pt;mso-line-height-rule:exactly'><st1:chsdate";
            html += "		IsROCDate='False' IsLunarDate='False' Day='30' Month='9' Year='2014' w:st='on'><span";
            html += "		 lang=EN-US style='font-size:16.0pt;font-family:'Times New Roman','serif';";
            html += "		 mso-fareast-font-family:仿宋_GB2312'>目标出现时间：" + (StringUtils.isEmptyString(imageClue.getC7()) ? "未知" : imageClue.getC7()) + "</span></b></p></br>";
        }
        return html;
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

    @ApiOperation(value = "获取案件类别菜单")
    @PostMapping("/getTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父Id"),
            @ApiImplicitParam(name = "level", value = "等级"),
            @ApiImplicitParam(name = "searchKeyWord", value = "关键字搜素")
    })
    public R moduleTree(String parentId, String level, String searchKeyWord){
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        if(StringUtils.isNotEmptyString(searchKeyWord)){
            List<AppLawcaseBasicoption> mList = appLawcaseBasicoptionService.list(new QueryWrapper<AppLawcaseBasicoption>().like("OPTIONS_NAME",searchKeyWord).eq("COL_NAME","CATEGORY_VALUE"));
            listData = bulidTree(mList, parentId, 0);
            return R.ok().put("list",listData);
        }
        if (StringUtils.isEmptyString(parentId)) {
            List<AppLawcaseBasicoption> mList = new ArrayList<AppLawcaseBasicoption>();
            AppLawcaseBasicoption t1 = new AppLawcaseBasicoption();
            t1.setOptionsValue("000000");
            t1.setOptionsName("刑事案件");
            mList.add(t1);
            t1 = new AppLawcaseBasicoption();
            t1.setOptionsValue("200000");
            t1.setOptionsName("行政案件");
            mList.add(t1);
            listData = bulidTree(mList, "", 1);
        } else {
            String typeCode = "";
            if("1".equals(level)){
                typeCode = parentId.substring(0, 1) + "%" + parentId.substring(2);
            }else if("2".equals(level)){
                typeCode = parentId.substring(0, 2) + "%%" + parentId.substring(4);
            }else if("3".equals(level)){
                typeCode = parentId.substring(0, 4) + "%%";
            }
            //List<AppLawcaseBasicoption> mList = appLawcaseBasicoptionService.list(new QueryWrapper<AppLawcaseBasicoption>().likeRight("OPTIONS_VALUE",typeCode).eq("COL_NAME","CATEGORY_VALUE"));
            List<AppLawcaseBasicoption> mList = appLawcaseBasicoptionService.queryAppLawcaseList(typeCode);
            listData = bulidTree(mList, parentId, Integer.parseInt(level) + 1);
        }
        return R.ok().put("list",listData);
    }
    /**
     * 构建树
     * @param tlist
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> bulidTree(List<AppLawcaseBasicoption> tlist, String parentId, Integer level) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (AppLawcaseBasicoption module : tlist) {
            if(module.getOptionsValue().equals(parentId)){
                continue;
            }
            List<AppLawcaseBasicoption> mList = new ArrayList<AppLawcaseBasicoption>();
            if (level > 0) {
                String typeCode = "";
                if(1 == level){
                    typeCode = module.getOptionsValue().substring(0, 1) + "%" + module.getOptionsValue().substring(2);
                }else if(2 == level){
                    typeCode = module.getOptionsValue().substring(0, 2) + "%%" + module.getOptionsValue().substring(4);
                }else if(3 == level){
                    typeCode = module.getOptionsValue().substring(0, 4) + "%%";
                }
                mList = appLawcaseBasicoptionService.queryAppLawcaseList(typeCode);
            }
            Map<String, Object> treeNode = new HashMap<String, Object>();
            treeNode.put("moduleId", module.getOptionsValue());
            treeNode.put("moduleName", module.getOptionsName());
            treeNode.put("level",level);
            treeNode.put("operation", "<span style=\"color: #4f98f7;cursor: pointer;\" onclick='chooseType(\""+ module.getOptionsValue() +"\",\""+ module.getOptionsName() +"\",\""+ level +"\",\""+ parentId +"\");'>选择</span>");
            if (mList.size() > 1 || 0 == level) {
                treeNode.put("state", "closed");
            } else {
                treeNode.put("state","open");
            }

            list.add(treeNode);
        }
        return list;
    }

    @ApiOperation("线索图片批量下载")
    @GetMapping(value = "/batchDownLoadImg")
    public R batchDownLoadImg(HttpServletResponse response, String fileIds) {
        if (StringUtils.isEmptyString(fileIds)){
            return R.error("图片文件Id不能为空");
        }
        return imageService.caseBatchDownLoadImg(response,fileIds);
    }
}
