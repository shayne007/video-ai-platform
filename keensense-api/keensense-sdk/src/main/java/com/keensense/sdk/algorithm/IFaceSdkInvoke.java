package com.keensense.sdk.algorithm;
import com.keensense.common.exception.VideoException;
import com.loocme.sys.datastruct.Var;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author jingege
 * @return:
 */
public interface IFaceSdkInvoke
{
    
    void initParams(Var param);

    String createRegistLib() throws VideoException;

    String deleteRegistLib(String repoId) throws VideoException;

    String getRegistLib(String repoId) throws VideoException;

    Var getPicAnalyze(String picture) throws VideoException;
    
    Var getPicAnalyzeOne(String picture) throws VideoException;

    String addFaceToLib(String repoId, String feature, String url) throws VideoException;
    String addFaceToLib(String repoId, String feature, String url,String time) throws VideoException;

    String delFaceFromLib(String repoId, String featureId) throws VideoException;

    String getFaceFeature(String repoId, String featureId) throws VideoException;

    Var getSimilars(String regIds, String feature, float threshold,
        int maxResult) throws VideoException;
    Var getSimilars(String regIds, String feature, float threshold,
                    int maxResult,String startTime,String endTime) throws VideoException;

    float compareFeature(String feature1, String feature2) throws VideoException;

}
