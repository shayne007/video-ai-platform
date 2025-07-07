package com.keensense.sdk.algorithm;

import com.keensense.common.exception.VideoException;
import com.loocme.sys.datastruct.Var;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author jingege
 * @return:
 */
public interface IBodySdkInvoke
{
    
    void initParams(Var param);

    String createRegistLib() throws VideoException;

    String createRegistLib(String id) throws VideoException;

    String deleteRegistLib(String repoId) throws VideoException;

    Var getPicAnalyze(int objType, String picture) throws VideoException;
    
    Var getPicAnalyzeOne(int objType, String picture) throws VideoException;

    String addBodyToLib(String repoId, String uuid, int objType, String feature) throws VideoException;

    String addBodyToLib(String repoId, String uuid, int objType, String feature,Integer firm) throws VideoException;

    String delBodyFromLib(String repoId, int objType, String featureId) throws VideoException;

    Var getSimilars(int objType, String regIds, String feature, Float threshold,
        int maxResult, boolean reRank) throws VideoException;

    Var getSimilars(int objType, String regIds, String feature, Float threshold,
                    int maxResult, boolean reRank,String beginTime,String endTime,int firm) throws VideoException;

    float compareFeature(int objType, String feature1, String feature2) throws VideoException;
}
