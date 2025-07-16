package com.keensense.commonlib.util;

import com.keensense.common.exception.VideoException;
import com.keensense.commonlib.entity.dto.CommonSearchResultDTO;
import com.keensense.sdk.constants.BodyConstant;
import com.keensense.sdk.constants.FaceConstant;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/21 15:49
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class AlgoSearchUtil {

    private AlgoSearchUtil(){}
    /**
     * 根据条件进行1:N搜图
     * @param regIds 库IDs
     * @param feature 图片特征
     * @param threshold 阈值
     * @param objType 搜索类型
     * @param maxResult 最多返回结果
     * @return 第一张人脸对象
     * */

    public static List<CommonSearchResultDTO> getQstSearchByParams(String regIds, String feature,
        Float threshold,int objType,int maxResult) throws VideoException{

        Map<String,Object> var = BodyConstant
            .getBodySdkInvoke().getSimilars(objType, regIds, feature, threshold*100, maxResult, false);
        if(var ==null) {

            log.error("getSearchByParams, id = " + regIds + " , sdkResult = null");
            return Collections.emptyList();
        }
        List<CommonSearchResultDTO> resultList = getQstSearchResultList(var,threshold);
        return resultList;
    }

    /**
     * 根据特征获取人脸1:N搜图
     * @param regId 库ID
     * @param feature 图片特征
     * @param threshold 阈值
     * @param maxResult 最大返回值
     * @return 第一张人脸对象
     * */
    public static List<CommonSearchResultDTO> getNonQstFacesByFeature(String regId, String feature, Float threshold,int maxResult){
        Map<String,Object> var = FaceConstant.getFaceSdkInvoke().getSimilars(regId, feature, threshold*100, maxResult);
        if(var == null) {
        	return Collections.emptyList();
        }
        List<CommonSearchResultDTO> resultList = getNonQstSearchResultList(var);
        return resultList;
    }

    private static List<CommonSearchResultDTO> getNonQstSearchResultList(Map<String,Object> var){

        JSONArray array = JSON.parseArray(var.toString());
        List<CommonSearchResultDTO> resultList = new ArrayList<>(array.size());
        String id;
        String score;
        String groupId;
        for (int i = 0; i < array.size(); i++) {
            String str = array.get(i)+"";
            JSONObject object = JSON.parseObject(str);
            JSONObject object1 =JSON.parseObject(object.getString("face"));
            id = object1.getString("id");
            score = object.getString("score");
            groupId = object1.getString("faceGroupId");
            if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(score)){
                Float scoreF = Float.valueOf(score);
                resultList.add(new CommonSearchResultDTO(id,scoreF,groupId));
            }
        }
        if(CollectionUtils.isNotEmpty(resultList)) {
            descResultFace(resultList);
        }
        return resultList;
    }
    /**
     * 解析qst搜图结果
     * */
    private static List<CommonSearchResultDTO> getQstSearchResultList(Map<String,Object> var,Float threshold){

        JSONArray array = JSON.parseArray(var.toString());
        List<CommonSearchResultDTO> resultList = new ArrayList<>(array.size());
        String id;
        String score;
        String groupId;
        for (int i = 0; i < array.size(); i++) {
            String str = array.get(i)+"";
            JSONObject object = JSON.parseObject(str);
            id = object.getString("uuid");
            score = object.getString("score");
            groupId = object.getString("task");
            if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(score) &&
                Float.valueOf(score)-threshold>=0){
                Float scoreF = Float.valueOf(score);
                resultList.add(new CommonSearchResultDTO(id,scoreF,groupId));
            }
        }
        return resultList;
    }

    /**
     * 新增排序方法
     * @author jingege
     * @param faces
     */
    private static void descResultFace(List<CommonSearchResultDTO> faces){
        Collections.sort(faces, (CommonSearchResultDTO o1, CommonSearchResultDTO o2)->{
            if (o1.getScore() > o2.getScore()) {
                return -1;
            }
            if (o1.getScore().equals(o2.getScore())) {
                return 0;
            }
            return 1;
        });
    }

}
