package com.keensense.admin.service.ext.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.config.ComConfig;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.service.ext.FeatureSearchService;
import com.keensense.admin.service.sys.ICfgMemPropsService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.common.platform.StandardRequestUtil;
import com.keensense.common.platform.bo.feature.DumpQuery;
import com.keensense.common.platform.bo.video.U2SHttpBo;
import com.keensense.common.platform.constant.StandardUrlConstant;
import com.keensense.common.util.ImageUtils;
import com.keensense.common.util.StandardHttpUtil;
import com.loocme.sys.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author cuiss
 * @Description 以图搜图
 * @Date 2018/10/16
 */
@Service("featureSearchService" + AbstractService.KS)
public class FeatureSearchKeensenseServiceImpl extends AbstractService implements FeatureSearchService {
    @Autowired
    IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    ICfgMemPropsService cfgMemPropsService;

    @Autowired
    private ComConfig comConfig;

    public U2SHttpBo initInputParam(Map<String, Object> paramMap) {
        U2SHttpBo imageSearchParam = new U2SHttpBo();
        if (paramMap.get("resultLimit") != null) {
            imageSearchParam.setLimitNum(MapUtil.getString(paramMap, "resultLimit"));
        }
        if (paramMap.get("similarityLimit") != null) {
            imageSearchParam.setThreshold(MapUtil.getString(paramMap, "similarityLimit"));
        }
        if (paramMap.get("objtype") != null) {
            imageSearchParam.setObjextType(MapUtil.getString(paramMap, "objtype"));
        }
        if (paramMap.get("picture") != null) {
            imageSearchParam.setPicture(MapUtil.getString(paramMap, "picture"));
        }
        if (paramMap.get("feature") != null) {
            imageSearchParam.setFeature(MapUtil.getString(paramMap, "feature"));
        }
        if (paramMap.get("serialnumbers") != null) {
            imageSearchParam.setSerialNumber(MapUtil.getString(paramMap, "serialnumbers"));
        }
        if (StringUtils.isEmpty(imageSearchParam.getSerialNumber()) && StringUtils.isEmpty(imageSearchParam.getCameraId())) {
            List<VsdTaskRelation> taskRelations = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>());
            Set<String> serialnumbers = new HashSet<>();
            for (VsdTaskRelation vsdTaskRelation : taskRelations) {
                serialnumbers.add(vsdTaskRelation.getSerialnumber());
            }
            imageSearchParam.setSerialNumber(String.join(",", serialnumbers));
        }
        if (paramMap.get("starttime") != null) {
            imageSearchParam.setStartTime(MapUtil.getString(paramMap, "starttime"));
        }
        if (paramMap.get("endtime") != null) {
            imageSearchParam.setEndTime(MapUtil.getString(paramMap, "endtime"));
        }
        if (paramMap.get("cameraids") != null) {
            imageSearchParam.setCameraId(MapUtil.getString(paramMap, "cameraids"));
        }
        if (paramMap.get("threshold") != null) {
            imageSearchParam.setTradeOff(MapUtil.getInteger(paramMap, "threshold"));
        }
        return imageSearchParam;
    }

    /**
     * 调用JManager服务
     *
     * @param paramMap
     * @return
     */
    @Override
    public String doSearchService(Map<String, Object> paramMap) {
        U2SHttpBo inParam = initInputParam(paramMap);
        String faceFeature = comConfig.getFaceFeature();
        if (StringUtils.isNotEmptyString(faceFeature) && "1".equals(faceFeature)){
            inParam.setFaceFeature(faceFeature);
        }
        String resultJson = StandardRequestUtil.getImageSearchResults(initKeensenseUrl(), inParam);
        JSONObject resultVar = JSONObject.parseObject(resultJson);
        if (StringUtils.isNotEmpty(resultVar.getString("status"))) {
            resultVar.put("ret", resultVar.getString("status"));
            resultVar.put("msg", resultVar.get("message"));
        } else {
            resultVar.put("ret", 0);
        }
        return resultVar.toString();
    }

    @Override
    public String doExtractFromPictureService(Map<String, Object> paramMap) {
        U2SHttpBo inParam = initInputParam(paramMap);
        return StandardRequestUtil.doExtractFromPictureService(initKeensenseUrl(), inParam);
    }

    @Override
    public String doExtractFromPictureGLFace(String pictureUrl) {
        net.sf.json.JSONObject featureParams = new net.sf.json.JSONObject();
        try {
            featureParams.put("picture", ImageUtils.getURLImage(pictureUrl));
            featureParams.put("detectMode", "1");//小图传1
        } catch (Exception e) {
            e.printStackTrace();
        }
        String featureRequestUrl = DbPropUtil.getString("picturestream.url", "127.0.0.1:8890") + StandardUrlConstant.EXTRACT_FROM_PICTURE_GL_FACE;
        String featureResponse = StandardHttpUtil.postContentWithJson(featureRequestUrl, featureParams.toString());
        return featureResponse;
    }

    @Override
    public String doStructPictureService(Map<String, Object> paramMap) {
        U2SHttpBo inParam = initInputParam(paramMap);
        return StandardRequestUtil.doStructPictureService(initKeensenseUrl(), inParam);
    }

    @Override
    public String doDumpService(DumpQuery dumpQuery) {
        String url = "http://" + cfgMemPropsService.getStandardIp() + ":" + 39081;
        return StandardRequestUtil.doDumpService(url, dumpQuery);
    }

    @Override
    public String doFeatureDumpService(DumpQuery dumpQuery) {
        String url = "http://" + cfgMemPropsService.getStandardIp() + ":" + 9999;
        return StandardRequestUtil.doFeatureDumpService(url, dumpQuery);
    }
}
