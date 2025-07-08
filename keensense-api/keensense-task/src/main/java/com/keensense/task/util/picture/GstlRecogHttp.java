package com.keensense.task.util.picture;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.util.HttpClientUtil;
import com.keensense.task.constants.PictureConstants;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Description:  GSTL 获取图片特征接口
 * @Author: wujw
 * @CreateDate: 2019/5/27 15:02
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 * */
@Slf4j
public class GstlRecogHttp {

    private GstlRecogHttp(){}

    /**获取车辆特征的类型参数*/
    private static final int[] CAR_FUNCTION_TYPE = {100,101,102,103,104,105,106,107,108,109};

    /**
     * 通过Http获取图片特征
     * @param objType 目前只支持查询车辆信息，固定为1
     * @param picBy 图片的Base64编码
     * @return 图片特征
     * */
    public static String getExtractFromPictureByGstl(Integer objType,byte[] picBy){
        String featureStr = null;
        //传入图片二进制base64编码后字符串
        Map<String,Object> paramVar = getFeatureParams(objType,new String(Base64.encode(picBy)));
        String resultData = requestGstl(paramVar.toString());
        JSONObject objextsVar = JSONObject.parseObject(resultData);
        if(PictureConstants.GSTL_REQUEST_SUCCESS.equals((objextsVar.getString("Context.Status")))){
            featureStr = objextsVar.getString("Results[0].Vehicles[0].Features");
            if(StringUtils.isEmpty(featureStr)){
                log.error("featureData from gstl can not get feature");
                return null;
            }else{
                log.info( "----featureData from gstl string length:" + featureStr.length());
                log.info(Base64.decode(Arrays.toString(featureStr.getBytes())).length+"");
            }
        }else{
            log.error("rest获取图片矢量特征失败："+ objextsVar.getString("Context.Status"));
            log.error(String.valueOf(objextsVar.getString("Context.Message")));
        }
        return featureStr;
    }

    private static String requestGstl(String params) {
         return HttpClientUtil.requestPost(
                OesObjectHandleManagerUtil.getGstlUrl(),"application/json",params);
    }

    /**
     * 获取图片特征body参数
     * @param objType 类型，目前固定为1
     * @param arrs 图片base64编码字符串，支持多个
     * */
    private static Map<String,Object> getFeatureParams(Integer objType, String ... arrs){
        Map<String,Object> paramVar = new HashMap<>();
        paramVar.put("Context.Functions",CAR_FUNCTION_TYPE);
        paramVar.put("Context.Type",objType);
        List<Map<String,Map<String,String>>> imageList = new ArrayList<>(arrs.length);
        for (int i = 0 ; i < arrs.length ; i++){
            Map<String,String> map = new HashMap<>(2);
            map.put("Id",(i+1)+"");
            map.put("BinData",arrs[i]);
            Map<String,Map<String,String>> data = new HashMap<>(1);
            data.put("Data",map);
            imageList.add(data);
        }
        paramVar.put("Images",imageList);
        return paramVar;
    }

}
