package com.keensense.commonlib.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.ResponseStatus;
import com.keensense.common.util.*;
import com.keensense.commonlib.entity.CommonFeatureRecord;
import com.keensense.commonlib.entity.CommonInfo;
import com.keensense.commonlib.entity.dto.*;
import com.keensense.commonlib.entity.vo.CommonLibVO;
import com.keensense.commonlib.service.ICommonFeatureService;
import com.keensense.commonlib.service.ICommonInfoService;
import com.keensense.sdk.util.ValidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Map.Entry;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/21 15:49
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@RestController
@RequestMapping("/VIID")
@Api(value = "通用底库",description = "通用底库")
@Slf4j
public class CommonLibController {

    @Autowired
    private ICommonInfoService iCommonInfoService;
    @Autowired
    private ICommonFeatureService iCommonFeatureService;

    private String dateFormat = "yyyyMMddHHmmss";
    private static final String INPUT_COMMON_JSON = "CommonLibObject";

    /**
     * 添加底库
     * name 名称
     * type 类型
     * presetStartTime 开始时间
     * presetEndTime 结束时间
     * */
    @PostMapping(value="/CommonLib",produces = "application/json;charset=UTF-8")
    @ApiOperation(value="创建底库", notes="创建通用底库")
    public ResponseStatusList createLibrary(HttpServletRequest request, @RequestBody String input) {

        JSONObject inputJsonObject = JSON.parseObject(input);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setRequestURL(request.getRequestURI());
        responseStatus.setLocalTime(com.keensense.common.util.DateUtil.formatDate(new Date(),dateFormat));
        try {
            CommonLibDTO commonLibDTO =  CommonLibDTO.createLibInputPatter(inputJsonObject,INPUT_COMMON_JSON);
            String libId = iCommonInfoService.createLibrary(commonLibDTO);
            return getResponseStatusList(responseStatus, ReponseCode.CODE_0.getCode(),ReponseCode.CODE_0.getMsg(),libId);
        }catch (VideoException e){
            log.error("createLibrary error",e);
        }
        return getResponseStatusList(responseStatus,ReponseCode.CODE_4.getCode(),ReponseCode.CODE_4.getMsg(), StringUtils.EMPTY);
    }

    /**
     * 删除底库
     * ids id集合
     * */
    @DeleteMapping(value="/CommonLib",produces = "application/json;charset=UTF-8")
    @ApiOperation(value="删除底库", notes="删除通用底库")
    public ResponseStatusTotal deleteLibrary(HttpServletRequest request, @RequestParam("IDList") String ids) {
        ResponseStatusTotal responseStatusTotal = new ResponseStatusTotal();
        List<ResponseStatus> responseStatusList = new ArrayList<>();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setRequestURL(request.getRequestURI());
        responseStatus.setLocalTime(com.keensense.common.util.DateUtil.formatDate(new Date(),dateFormat));
        if(StringUtils.isBlank(ids)||ids.split(",")==null||ids.split(",").length<=0){
            responseStatus.setId(null);
            responseStatus.setStatusCode(ReponseCode.CODE_4.getCode());
            responseStatus.setStatusString(ReponseCode.CODE_4.getMsg());
            responseStatusList.add(responseStatus);
        }else {
            Map<String, String> idRsltMap = iCommonInfoService.deleteLibrary(ids.split(","));
            for (Entry<String, String> idRslt : idRsltMap.entrySet()) {
                ResponseStatus cloneResponseStatus = cloneResponseStatus(responseStatus);
                cloneResponseStatus.setId(idRslt.getKey());
                cloneResponseStatus.setStatusCode(idRslt.getValue().split(",")[0]);
                cloneResponseStatus.setStatusString(idRslt.getValue().split(",")[1]);
                responseStatusList.add(cloneResponseStatus);
            }
        }
        try {
            responseStatusTotal = ResultUtils.returnStatusList(responseStatusList);
        }catch (Exception e ){
            log.error("deleteLibrary error",e);
        }
        return responseStatusTotal;

    }

    /**
     * 修改底库
     * name 名称
     * id 底库ID
     * presetStartTime 开始时间
     * presetEndTime 结束时间
     * */
    @PutMapping(value="/CommonLib",produces = "application/json;charset=UTF-8")
    @ApiOperation(value="修改底库", notes="修改通用底库")
    public ResponseStatusList updateLibrary(HttpServletRequest request, @RequestBody String input) {

        JSONObject inputJsonObject = JSON.parseObject(input);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setRequestURL(request.getRequestURI());
        responseStatus.setLocalTime(com.keensense.common.util.DateUtil.formatDate(new Date(),dateFormat));
        try {
            CommonLibDTO commonLibDTO =  CommonLibDTO.updateLibInputPatter(inputJsonObject,INPUT_COMMON_JSON);
            String id = commonLibDTO.getId();
            CommonInfo commonInfo = iCommonInfoService.getById(id);
            if(commonInfo == null){
                return getResponseStatusList(responseStatus,ReponseCode.CODE_4.getCode(),ReponseCode.CODE_4.getMsg(),id);
            }
            commonInfo.setName(commonLibDTO.getName());
            commonInfo.setUpdateTime(new Date());
            iCommonInfoService.updateById(commonInfo);
            return getResponseStatusList(responseStatus,ReponseCode.CODE_0.getCode(),ReponseCode.CODE_0.getMsg(),id);
        }catch (VideoException e){
            log.error("updateLibrary error",e);
        }
        return getResponseStatusList(responseStatus, ReponseCode.CODE_4.getCode(),ReponseCode.CODE_4.getMsg(),StringUtils.EMPTY);
    }


    /**
     * 新增特征
     * type 类型
     * id 底库ID
     * picture Base64图片
     * */
    @PostMapping(value="/CommonLibFeature",produces = "application/json;charset=UTF-8")
    @ApiOperation(value="新增特征", notes="新增特征")
    public ResponseStatusList addFeature(HttpServletRequest request, @RequestBody String input) {

        JSONObject inputJsonObject = JSON.parseObject(input);
        String inputFeatureJsonName = "CommonLibFeatureObject";
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setRequestURL(request.getRequestURI());
        responseStatus.setLocalTime(com.keensense.common.util.DateUtil.formatDate(new Date(),dateFormat));
        try {
            CommonLibFeatureDTO commonLibFeatureDTO =  CommonLibFeatureDTO.addFeatureInputPatter(inputJsonObject,inputFeatureJsonName);
            String libId = commonLibFeatureDTO.getId();
            CommonInfo commonInfo = iCommonInfoService.getById(libId);
            if(commonInfo == null){
                return getResponseStatusList(responseStatus,ReponseCode.CODE_4.getCode(),ReponseCode.CODE_4.getMsg(),StringUtils.EMPTY);
            }
            String libFeatureId = iCommonFeatureService.addFeature(commonLibFeatureDTO,commonInfo.getType());
            return getResponseStatusList(responseStatus,ReponseCode.CODE_0.getCode(),ReponseCode.CODE_0.getMsg(),libFeatureId);
        }catch (VideoException e){
            log.error("addFeature error",e);
        }
        return getResponseStatusList(responseStatus,ReponseCode.CODE_4.getCode(),ReponseCode.CODE_4.getMsg(),StringUtils.EMPTY);
    }

    @DeleteMapping(value="/CommonLibFeature",produces = "application/json;charset=UTF-8")
    @ApiOperation(value="删除特征", notes="删除特征")
    public ResponseStatusTotal deleteFeature(HttpServletRequest request, @RequestParam("LibID") String libId,
        @RequestParam("IDList") String ids) {

        ResponseStatusTotal responseStatusTotal = new ResponseStatusTotal();
        List<ResponseStatus> responseStatusList = new ArrayList<>();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setRequestURL(request.getRequestURI());
        responseStatus.setLocalTime(com.keensense.common.util.DateUtil.formatDate(new Date(),dateFormat));
        responseStatus.setStatusCode(ReponseCode.CODE_4.getCode());
        responseStatus.setId(StringUtils.EMPTY);
        if(StringUtils.isBlank(libId)||StringUtils.isBlank(ids)||ids.split(",")==null ||
            ids.split(",").length<=0){
            responseStatus.setStatusString(ReponseCode.CODE_4.getMsg());
            responseStatusList.add(responseStatus);
        }else {

            CommonInfo commonInfo = iCommonInfoService.getById(libId);
            if (commonInfo == null) {
                responseStatus.setStatusString(ReponseCode.CODE_4.getMsg());
                responseStatusList.add(responseStatus);
            } else {
                Map<String, String> idRsltMap = iCommonFeatureService.deleteFeature(libId, commonInfo.getType(), ids.split(","));
                for (Entry<String, String> idRslt : idRsltMap.entrySet()) {
                    ResponseStatus cloneResponseStatus = cloneResponseStatus(responseStatus);
                    cloneResponseStatus.setId(idRslt.getKey());
                    cloneResponseStatus.setStatusCode(idRslt.getValue().split(",")[0]);
                    cloneResponseStatus.setStatusString(idRslt.getValue().split(",")[1]);
                    responseStatusList.add(cloneResponseStatus);
                }
            }
        }
        try {
            responseStatusTotal = ResultUtils.returnStatusList(responseStatusList);
        } catch (Exception e) {
            log.error("deleteFeature error",e);
        }
        return responseStatusTotal;
    }

    /**
     * 特征检索
     * type 类型
     * ids 底库ID
     * picture Base64图片
     * */
    @PostMapping(value="/CommonLib/Search",produces = "application/json;charset=UTF-8")
    @ApiOperation(value="特征检索", notes="特征检索")
    public JSONObject searchLibrary(@RequestBody String input) {

        JSONObject inputJsonObject = JSON.parseObject(input);
        List<CommonSearchResultDTO> commonSearchResultDTOList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        JSONObject subJsonObject = new JSONObject();
        try {
            CommonLibDTO commonLibDTO =  CommonLibDTO.searchLibInputPatter(inputJsonObject,INPUT_COMMON_JSON);
            if(ValidUtil.validImageSize(commonLibDTO.getBaseData(), 2)) {
                String[] libIDList = commonLibDTO.getLibIDList().split(",");
                int libCount = iCommonInfoService.count(new QueryWrapper<CommonInfo>()
                    .eq("type", commonLibDTO.getType())
                    .in("id", Arrays.asList(libIDList)));
                if (libCount == libIDList.length) {
                    commonSearchResultDTOList = iCommonInfoService.searchLibrary(commonLibDTO);
                }
            }
        } catch (VideoException e) {
            log.error("searchLibrary error",e);
        }finally {
            subJsonObject.put("Count", commonSearchResultDTOList.size());
            subJsonObject.put("CommonSearchResultObject", commonSearchResultDTOList);
            jsonObject.put("CommonSearchResultListObject",subJsonObject);
        }
        return jsonObject;
    }

    private ResponseStatusList getResponseStatusList(ResponseStatus responseStatus, String code,String message,String data){
        responseStatus.setStatusCode(code);
        responseStatus.setStatusString(message);
        responseStatus.setId(data);
        ResponseStatusList responseStatusList = new ResponseStatusList();
        try {
             responseStatusList = ResultUtils.returnStatus(responseStatus);
        }catch (Exception e ){
            log.error("getResponseStatusList error",e);
        }
        return responseStatusList;
    }

    private ResponseStatus cloneResponseStatus(ResponseStatus responseStatus){
        ResponseStatus cloneResponseStatus = new ResponseStatus();
        cloneResponseStatus.setRequestURL(responseStatus.getRequestURL());
        cloneResponseStatus.setLocalTime(responseStatus.getLocalTime());
        return cloneResponseStatus;
    }

    @GetMapping(value = "/CommonLib")
    @ApiOperation(value = "查看特征库", notes = "查看特征库")
    public IPage<CommonLibVO> getLibrary(CommonLibQueryDto dto, PageDto page) {
        return iCommonInfoService.listLibrary(dto, page);
    }

    @GetMapping(value = "/CommonLibFeature")
    @ApiOperation(value = "查看特征库特征变更", notes = "查看特征库特征变更")
    public IPage<CommonFeatureRecord> getFeatureRecord(CommonFeatureQueryDto dto, PageDto page) {

        return iCommonFeatureService.listCommonFeature(dto, page);
    }


    @PostMapping(value = "/CommonLib/UploadToDevice")
    @ApiOperation(value = "加载库特征到GPU", notes = "加载库特征到GPU")
    public ResponseStatusTotal upload2Device(HttpServletRequest request, String repo,
        Integer type) {
        boolean success = iCommonInfoService.uploadToDevice(repo, type == null ? 3 : type);
        if (success) {
            return generateResponse(request, ReponseCode.CODE_0, repo);
        }
        return generateResponse(request, ReponseCode.CODE_4, repo);

    }


    private ResponseStatusTotal generateResponse(HttpServletRequest request,
        ReponseCode reponseCode, String id) {
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setId(id);
        responseStatus.setRequestURL(request.getRequestURI());
        responseStatus.setLocalTime(DateUtil.formatDate(new Date(), dateFormat));
        responseStatus.setStatusCode(reponseCode.getCode());
        responseStatus.setStatusString(reponseCode.getMsg());
        List<ResponseStatus> responseStatusList = Lists.newArrayList(responseStatus);
        return ResultUtils.returnStatusList(responseStatusList);
    }
}
