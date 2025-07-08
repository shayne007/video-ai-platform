package com.keensense.sdk.algorithm;

import com.keensense.common.exception.VideoException;

import java.util.Map;

/**
 * @description:
 * @author jingege
 * @return:
 */
public interface IFaceSdkInvoke
{
    
    void initParams(Map<String,Object> param);

    String createRegistLib() throws VideoException;

    String deleteRegistLib(String repoId) throws VideoException;

    String getRegistLib(String repoId) throws VideoException;

    Map<String,Object> getPicAnalyze(String picture) throws VideoException;

    Map<String,Object> getPicAnalyzeOne(String picture) throws VideoException;

    String addFaceToLib(String repoId, String feature, String url) throws VideoException;
    String addFaceToLib(String repoId, String feature, String url,String time) throws VideoException;

    String delFaceFromLib(String repoId, String featureId) throws VideoException;

    String getFaceFeature(String repoId, String featureId) throws VideoException;

    Map<String,Object> getSimilars(String regIds, String feature, float threshold,
        int maxResult) throws VideoException;
    Map<String,Object> getSimilars(String regIds, String feature, float threshold,
                    int maxResult,String startTime,String endTime) throws VideoException;

    float compareFeature(String feature1, String feature2) throws VideoException;

}
