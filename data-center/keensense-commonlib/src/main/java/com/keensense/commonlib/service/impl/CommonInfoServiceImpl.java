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
import com.keensense.common.util.HttpClientUtil;
import com.keensense.common.util.ReponseCode;
import com.keensense.commonlib.config.NacosConfig;
import com.keensense.commonlib.constants.CommonLibConstant;
import com.keensense.commonlib.entity.CommonFeatureInfo;
import com.keensense.commonlib.entity.CommonInfo;
import com.keensense.commonlib.entity.dto.CommonLibQueryDto;
import com.keensense.commonlib.entity.dto.CommonSearchResultDTO;
import com.keensense.commonlib.entity.dto.CommonLibDTO;
import com.keensense.commonlib.entity.dto.PageDto;
import com.keensense.commonlib.entity.vo.CommonLibVO;
import com.keensense.commonlib.mapper.CommonFeatureMapper;
import com.keensense.commonlib.mapper.CommonInfoMapper;
import com.keensense.commonlib.service.ICommonInfoService;
import com.keensense.commonlib.util.AlgoSearchUtil;
import com.keensense.sdk.algorithm.IBodySdkInvoke;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.algorithm.impl.QstFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.StFaceSdkInvokeImpl;
import com.keensense.sdk.constants.BodyConstant;
import com.keensense.sdk.constants.CommonConst;
import com.keensense.sdk.constants.FaceConstant;

import java.util.*;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/17 16:05
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service
@Slf4j
public class CommonInfoServiceImpl extends ServiceImpl<CommonInfoMapper,CommonInfo>
    implements ICommonInfoService{

    @Autowired
    private CommonFeatureMapper commonFeatureMapper;

    @Resource
    private NacosConfig configCenter;

    @Override
    @Transactional
    public String createLibrary(CommonLibDTO commonLibDTO) throws VideoException{

        String libId;
        int type = commonLibDTO.getType();
        switch (type){
            case CommonLibConstant.LIBRARY_TYPE_HUMAN:
            case CommonLibConstant.LIBRARY_TYPE_VEHICLE:
            case CommonLibConstant.LIBRARY_TYPE_BIKE:
                libId = BodyConstant.getBodySdkInvoke().createRegistLib();
                break;
            case CommonLibConstant.LIBRARY_TYPE_FACE:
                libId = FaceConstant.getFaceSdkInvoke().createRegistLib();
                break;
            default:
                throw  new VideoException(-1,"type error");
        }
        CommonInfo commonInfo = new CommonInfo(libId,commonLibDTO.getName(),new Date(),commonLibDTO.getType());
        baseMapper.insert(commonInfo);
        return libId;
    }

    @Override
    @Transactional
    public Map<String,String> deleteLibrary(String[] idArr){
        Map<String,String> idRslts = new HashMap<>();
        List<CommonInfo> CommonInfoList = baseMapper.selectList(new QueryWrapper<CommonInfo>()
            .orderByAsc("type").in("id",Arrays.asList(idArr)));

        if(CollectionUtils.isEmpty(CommonInfoList)){
            for (String id:idArr){
                idRslts.put(id, ReponseCode.CODE_4.getCode()+","+ReponseCode.CODE_4.getMsg());
            }
            return idRslts;
        }
        for (CommonInfo commonInfo : CommonInfoList){
            deleteLibToRslt(commonInfo.getType(),commonInfo.getId(),idRslts);
        }
        if (idArr.length>CommonInfoList.size()){

            for(String id :idArr){
                if(!idRslts.containsKey(id)){
                    idRslts.put(id,ReponseCode.CODE_4.getCode()+","+ReponseCode.CODE_4.getMsg());
                }
            }
        }
        return idRslts;
    }

    public void deleteLibToRslt(Integer type,String id, Map<String,String> idRslts){
        String msg;
        try{

            if(type.equals(CommonLibConstant.LIBRARY_TYPE_FACE)){
                FaceConstant.getFaceSdkInvoke().deleteRegistLib(id);
            }else{
                BodyConstant.getBodySdkInvoke().deleteRegistLib(id);
            }
            baseMapper.deleteById(id);
            commonFeatureMapper.delete(new QueryWrapper<CommonFeatureInfo>()
                .eq("library_id",id));
            msg = ReponseCode.CODE_0.getCode()+","+ReponseCode.CODE_0.getMsg();
        }catch (VideoException e){
            log.error("deleteLibToRslt error",e);
            msg =  ReponseCode.CODE_3.getCode()+","+ReponseCode.CODE_3.getMsg();
        }
        idRslts.put(id,msg);
    }
    /**
     * @description:
     * @param commonLibDTO
     * @return: java.util.List<com.keensense.commonlib.entity.dto.CommonSearchResultDTO>
     */
    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED,rollbackFor = Exception.class)
    public List<CommonSearchResultDTO> searchLibrary(CommonLibDTO commonLibDTO) throws VideoException{
        List<CommonSearchResultDTO> resultList;
        String LibIds = commonLibDTO.getLibIDList();
        int type = commonLibDTO.getType();
        Float threshold = commonLibDTO.getThreshold();
        Integer maxResult = commonLibDTO.getMaxResult();
        String searchFeature = StringUtils.isEmpty(commonLibDTO.getFeature()) ? "" : commonLibDTO.getFeature();
        if (type == CommonConst.OBJ_TYPE_FACE && !(FaceConstant.getFaceSdkInvoke() instanceof QstFaceSdkInvokeImpl)) {
            if (StringUtils.isEmpty(searchFeature)) {
                IFaceSdkInvoke faceSdk = FaceConstant.getFaceSdkInvoke();
                Map<String,Object> faceInfoVar = faceSdk.getPicAnalyzeOne(commonLibDTO.getBaseData());
                if (faceInfoVar == null) {
                    throw new VideoException(-1, "图片未提取到特征");
                }
                searchFeature = (String) faceInfoVar.get("featureVector");
            }
            resultList = AlgoSearchUtil.getNonQstFacesByFeature(LibIds, searchFeature, threshold, maxResult);
        } else {
            if (StringUtils.isEmpty(searchFeature)) {
                IBodySdkInvoke bodySdk = BodyConstant.getBodySdkInvoke();
                Map<String,Object> bodyFeatureInfo = bodySdk.getPicAnalyzeOne(type, commonLibDTO.getBaseData());
                if (bodyFeatureInfo == null) {
                    throw new VideoException(-1, "图片未提取到特征");
                }
                searchFeature = (String) bodyFeatureInfo.get("featureVector");
            }

            resultList = AlgoSearchUtil.getQstSearchByParams(LibIds, searchFeature, threshold, type, maxResult);
        }

        return resultList;
    }

    @Override
    public IPage<CommonLibVO> listLibrary(CommonLibQueryDto dto, PageDto page) {
        LambdaQueryWrapper<CommonInfo> wrapper = Wrappers.lambdaQuery();

        if (StringUtils.isNotEmpty(dto.getId())) {
            wrapper = wrapper.eq(CommonInfo::getId, dto.getId());
        }
        if (StringUtils.isNotEmpty(dto.getName())) {
            wrapper = wrapper.eq(CommonInfo::getName, dto.getName());
        }
        IPage<CommonInfo> iPage = baseMapper.selectPage(new Page<>((page.getPageNo() - 1) * page.getPageSize(), page.getPageSize()), wrapper);
        List<CommonLibVO> commonLibVos = new ArrayList<>();
        iPage.getRecords().forEach(commonInfo ->{
            CommonLibVO vo = new CommonLibVO();
            commonLibVos.add(vo);
            BeanUtils.copyProperties(commonInfo, vo);
            vo.setIsAllowDel(false);
            vo.setRemark("");
        });
        IPage<CommonLibVO> vos =new Page<>();
        vos.setRecords(commonLibVos);
        return vos;
    }

    @Override
    public boolean uploadToDevice(String repo, Integer type) {
        Map<String,Object> params = new HashMap<>();
        params.put("repo", repo);
        params.put("type", type);
        params.put("deviceID", 0);

        String resp = "";
        String url = "http://" + configCenter.getFeatureExtractUrl().split(":")[0] + ":39082/uploadtodevice";
        resp = HttpClientUtil
                .requestPost(url, "application/json;charset=utf-8", params.toString());
        JSONObject jsonObject = JSON.parseObject(resp);
        if (jsonObject.getInteger("error_code") == 0) {
            return true;
        }
        return false;
    }
}

