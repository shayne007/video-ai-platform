package com.keensense.sdk.algorithm.init;

import com.alibaba.fastjson.JSONObject;
import com.keensense.sdk.algorithm.IBodySdkInvoke;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.algorithm.impl.GLQstFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.GlstFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.KsFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.StFaceSdkInvokeImpl;
import com.keensense.sdk.constants.BodyConstant;
import com.keensense.sdk.constants.CommonConst;
import com.keensense.sdk.constants.FaceConstant;
import com.keensense.sdk.jni.U2sRecogHttp;
import com.keensense.sdk.util.SDKUtils;
import com.loocme.sys.datastruct.Var;
import lombok.extern.slf4j.Slf4j;

/**
 * 初始化加载类
 * @description:
 * @author: jingege
 * @createDate:2019年5月9日 下午2:39:05
 * @company:
 */
@Slf4j
public class AlgoStartup {

    /**
     * @description:仅支持HTTP
     * @return: void
     */

    public static void initAlgo(JSONObject jsonObject) {
        Boolean algoSwitch = jsonObject.getBoolean("algoSwitch");
        String bodyClasspath = jsonObject.getString("bodyClassPath");
        String bodyServiceUrl = jsonObject.getString("bodyServiceUrl");
        String faceClasspath = jsonObject.getString("faceClassPath");
        String faceServiceUrl = jsonObject.getString("faceServiceUrl");

        String featureExtractUrl = jsonObject.getString("featureExtractUrl");
        String faceFeatureExtractUrl = jsonObject.getString("faceFeatureExtractUrl");
        U2sRecogHttp.initUrl(featureExtractUrl,faceFeatureExtractUrl);

        SDKUtils.setFeatureExtractType(CommonConst.FEATURE_EXTRACT_TYPE_HTTP);
        if(algoSwitch){
            //人形算法
            BodyConstant.setBodySdkInvoke(bodyClasspath);
            IBodySdkInvoke bodySdkInvoke = BodyConstant.getBodySdkInvoke();
            if(bodySdkInvoke != null){
                Var body =  Var.newObject();
                body.set("bodyServiceUrl","http://" + featureExtractUrl.split(":")[0] + ":39082/");
                bodySdkInvoke.initParams(body);
            }
            //人脸
            FaceConstant.setFaceSdkInvoke(faceClasspath);
            IFaceSdkInvoke sdkInvoke = FaceConstant.getFaceSdkInvoke();
            //加载featureVersion
            Var face = Var.newObject();
            face.set("faceServiceUrl",faceServiceUrl);
            if (sdkInvoke instanceof KsFaceSdkInvokeImpl) {
                FaceConstant.reloadFeatureVersion(faceServiceUrl + FaceConstant.KSAPP_VERSION );
            }
            if (sdkInvoke instanceof GlstFaceSdkInvokeImpl){
                face.set("faceGlstTempPath",jsonObject.getString("faceGlstTempPath"));
            }
            if (sdkInvoke instanceof StFaceSdkInvokeImpl) {
                face.set("stFastSearch", jsonObject.getString("stFastSearch"));
            }
            if (sdkInvoke instanceof GLQstFaceSdkInvokeImpl){
                face.set("glStructPort",jsonObject.getString("glStructPort"));
                face.set("glComparePort",jsonObject.getString("glComparePort"));
                face.set("glStructDetectMode",jsonObject.getString("glStructDetectMode"));
            }
            sdkInvoke.initParams(face);
        }
    }


}