package com.keensense.picturestream.util;

import cn.jiuling.plugin.extend.FaceConstant;
import cn.jiuling.plugin.extend.picrecog.FaceAppMain;
import com.keensense.common.config.SpringContext;
import com.keensense.picturestream.config.NacosConfig;
import com.loocme.sys.datastruct.Var;

/**
 * @Description: 人脸结构化工具类
 * @Author: wujw
 * @CreateDate: 2019/12/5 14:55
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class FaceUtil {

    private FaceUtil(){}

    private static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    public static FaceAppMain getFaceAppMain(){
        Var param = Var.newObject();
        int faceType = FaceConstant.TYPE_COMPANY_QST;

        if (nacosConfig.getFaceClassPath().toLowerCase().contains("ks")) {
            faceType = FaceConstant.TYPE_COMPANY_KS;
            param.set("faceUrl", nacosConfig.getFaceServiceUrl());
        } else if (nacosConfig.getFaceClassPath().toLowerCase().contains("gl")) {
            //格林算法
            faceType = FaceConstant.TYPE_COMPANY_GLST;
            String ip = nacosConfig.getFaceServiceUrl();
            param.set("faceUrl", ip.substring(0, ip.length() - 1) + ":" + nacosConfig.getGlStructPort() + "/");
        } else if (nacosConfig.getFaceClassPath().toLowerCase().contains("box")) {
            //商汤盒子算法
            faceType = FaceConstant.TYPE_COMPANY_STM;
            param.set("faceUrl", nacosConfig.getFaceServiceUrl());
        } else if (nacosConfig.getFaceClassPath().toLowerCase().contains("st")) {
            //商汤算法
            faceType = FaceConstant.TYPE_COMPANY_ST;
            param.set("faceUrl", nacosConfig.getFaceServiceUrl());
        }
        return FaceAppMain.getInstance(faceType, param);
    }
}
