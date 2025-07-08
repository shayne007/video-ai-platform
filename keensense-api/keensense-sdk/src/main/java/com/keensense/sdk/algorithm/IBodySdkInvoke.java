package com.keensense.sdk.algorithm;

import com.keensense.common.exception.VideoException;


import java.util.Map;

/**
 * @description:
 * @author jingege
 * @return:
 */
public interface IBodySdkInvoke
{
    
    void initParams(Map<String,Object> param);

    String createRegistLib() throws VideoException;

    String createRegistLib(String id) throws VideoException;

    String deleteRegistLib(String repoId) throws VideoException;

    Map<String,Object> getPicAnalyze(int objType, String picture) throws VideoException;

    Map<String,Object> getPicAnalyzeOne(int objType, String picture) throws VideoException;

    String addBodyToLib(String repoId, String uuid, int objType, String feature) throws VideoException;

    String addBodyToLib(String repoId, String uuid, int objType, String feature,Integer firm) throws VideoException;

    String delBodyFromLib(String repoId, int objType, String featureId) throws VideoException;

    Map<String,Object> getSimilars(int objType, String regIds, String feature, Float threshold,
        int maxResult, boolean reRank) throws VideoException;

    Map<String,Object> getSimilars(int objType, String regIds, String feature, Float threshold,
                    int maxResult, boolean reRank,String beginTime,String endTime,int firm) throws VideoException;

    float compareFeature(int objType, String feature1, String feature2) throws VideoException;
}
