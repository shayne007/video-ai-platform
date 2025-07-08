package com.keensense.commonlib.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.ReponseCode;
import com.keensense.commonlib.constants.CommonLibConstant;
import com.keensense.commonlib.entity.CommonFeatureInfo;
import com.keensense.commonlib.entity.dto.CommonFeatureQueryDto;
import com.keensense.commonlib.entity.dto.CommonLibFeatureDTO;
import com.keensense.commonlib.entity.CommonFeatureRecord;
import com.keensense.commonlib.entity.dto.PageDto;
import com.keensense.commonlib.feign.IFeignService;
import com.keensense.commonlib.mapper.CommonFeatureMapper;
import com.keensense.commonlib.mapper.CommonFeatureRecordMapper;
import com.keensense.commonlib.service.ICommonFeatureService;
import com.keensense.commonlib.util.IDUtil;
import com.keensense.sdk.constants.BodyConstant;
import com.keensense.sdk.constants.FaceConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.PatternMatchUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/17 16:05
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service
public class CommonFeatureServiceImpl extends ServiceImpl<CommonFeatureMapper,CommonFeatureInfo>
    implements ICommonFeatureService{

    @Resource
    IFeignService feignService;

    @Resource
    CommonFeatureRecordMapper recordMapper;

    @Override
    @Transactional
    public String addFeature(CommonLibFeatureDTO commonLibFeatureDTO, Integer type) throws VideoException{

        String libId = commonLibFeatureDTO.getId();
        String featureId = StringUtils.EMPTY;
        Map<String,Object> rsltVar;
        switch (type){
            case CommonLibConstant.LIBRARY_TYPE_HUMAN:
            case CommonLibConstant.LIBRARY_TYPE_VEHICLE:
            case CommonLibConstant.LIBRARY_TYPE_BIKE:
                rsltVar = BodyConstant.getBodySdkInvoke().getPicAnalyzeOne(type,commonLibFeatureDTO.getBaseData());
                if(rsltVar==null) {
                    throw new VideoException(-1,"getPicAnalyzeOne is null,type is"+type);
                }
                featureId = BodyConstant.getBodySdkInvoke().addBodyToLib(libId, IDUtil.uuid(), (Integer) type,
                        (String) rsltVar.get("featureVector"));
                break;
            case CommonLibConstant.LIBRARY_TYPE_FACE:
                rsltVar = FaceConstant.getFaceSdkInvoke().getPicAnalyzeOne(commonLibFeatureDTO.getBaseData());
                if(rsltVar==null) {
                    throw new VideoException(-1,"getPicAnalyzeOne is null,type is"+type);
                }
                String faceFeature = (String) rsltVar.get("featureVector");
                featureId = FaceConstant.getFaceSdkInvoke().addFaceToLib(libId, faceFeature, null);
                break;
            default: break;
        }
        if(StringUtils.isBlank(featureId)){
            throw new VideoException(-1,"featureid is null,type is"+type);
        }
        CommonFeatureInfo commonFeatureInfo = new CommonFeatureInfo(featureId,getImgUrl(commonLibFeatureDTO.getBaseData()),libId,type,new Date());
        baseMapper.insert(commonFeatureInfo);
        triggerRecord(commonFeatureInfo, false);
        return featureId;
    }

    private String getImgUrl(String data) {
        if(PatternMatchUtils.simpleMatch(data, "^(http|ftp).*$")) {
            return data;
        }
        JSONObject imgObj = JSON.parseObject(feignService.getImg(IDUtil.uuid(),data));
        return imgObj.getString("ImageUrl");
    }

    /**
     * 记录特征变更记录
     */
    private void triggerRecord(CommonFeatureInfo featureInfo,boolean deleted) {
        CommonFeatureRecord record = new CommonFeatureRecord();
        BeanUtils.copyProperties(featureInfo, record);
        record.setUpdateTime(new Date());
        record.setDeleted(deleted);
        recordMapper.insert(record);
    }

    @Override
    public Map<String,String> deleteFeature(String libId,Integer type,String[] idArr) {
        Map<String,String> idRslts = new HashMap<>(idArr.length);
        List<CommonFeatureInfo> commonFeatureInfos = baseMapper.selectList(new QueryWrapper<CommonFeatureInfo>()
            .in("id", Arrays.asList(idArr)).eq("library_id",libId));
        if(CollectionUtils.isEmpty(commonFeatureInfos)){
            for (String id:idArr){
                idRslts.put(id, ReponseCode.CODE_4.getCode()+","+ReponseCode.CODE_4.getMsg());
            }
            return idRslts;
        }
        Map<String,CommonFeatureInfo> backup = new HashMap<>();
        for (CommonFeatureInfo commonFeatureInfo : commonFeatureInfos){
            deleteFeatureToRslt(type,commonFeatureInfo.getId(),libId,idRslts);
            backup.put(commonFeatureInfo.getId(), commonFeatureInfo);
        }
        if (idArr.length>commonFeatureInfos.size()){

            for(String id :idArr){
                if(!idRslts.containsKey(id)){
                    idRslts.put(id,ReponseCode.CODE_4.getCode()+","+ReponseCode.CODE_4.getMsg());
                }
            }
        }
        idRslts.forEach((k,v) ->{
            if (v.contains("OK")) {
                triggerRecord(backup.get(k),true);
            }
        });

        return idRslts;
    }

    @Override
    public IPage<CommonFeatureRecord> listCommonFeature(CommonFeatureQueryDto dto, PageDto page) {
        LambdaQueryWrapper<CommonFeatureRecord> wrapper = Wrappers.lambdaQuery();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getId())) {
            wrapper = wrapper.eq(CommonFeatureRecord::getId, dto.getId());
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getImgUrl())) {
            wrapper = wrapper.eq(CommonFeatureRecord::getImgUrl, dto.getImgUrl());
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getLibraryId())) {
            wrapper = wrapper.eq(CommonFeatureRecord::getLibraryId, dto.getLibraryId());
        }
        if (dto.getFeatureType() !=null) {
            wrapper = wrapper.eq(CommonFeatureRecord::getFeatureType, dto.getFeatureType());
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getStartTime())) {
            wrapper = wrapper.ge(CommonFeatureRecord::getUpdateTime, dto.getStartTime());
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getEndTime())) {
            wrapper = wrapper.le(CommonFeatureRecord::getUpdateTime, dto.getEndTime());
        }
        return recordMapper.selectPage(new Page<>((page.getPageNo()-1)*page.getPageSize(),page.getPageSize()), wrapper);
    }

    public void deleteFeatureToRslt(Integer type,String id,String libId, Map<String,String> idRslts){
        String msg;
        try{

            if(type.equals(CommonLibConstant.LIBRARY_TYPE_FACE)){
                FaceConstant.getFaceSdkInvoke().delFaceFromLib(libId,id);
            }else{
                BodyConstant.getBodySdkInvoke().delBodyFromLib(libId,type,id);
            }
            baseMapper.deleteById(id);
            msg = ReponseCode.CODE_0.getCode()+","+ReponseCode.CODE_0.getMsg();
        }catch (VideoException e){
            log.error("deleteFeatureToRslt error",e);
            msg = ReponseCode.CODE_4.getCode()+","+ReponseCode.CODE_4.getMsg();
        }
        idRslts.put(id,msg);
    }
}

