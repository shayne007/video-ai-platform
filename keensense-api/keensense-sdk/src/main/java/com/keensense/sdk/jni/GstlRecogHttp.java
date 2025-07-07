package com.keensense.sdk.jni;

import com.keensense.sdk.constants.CommonConst;
import com.keensense.sdk.util.OESObjectHandleManagerUtil;
import com.loocme.security.encrypt.Base64;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.exception.HttpConnectionException;
import com.loocme.sys.util.PostUtil;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GSTL 获取图片特征接口
 * */
@Slf4j
public class GstlRecogHttp {

    //获取车辆特征的类型参数
    private static final int[] CAR_FUNCTION_TYPE = {100,101,102,103,104,105,106,107,108,109};

    /**
     * 通过Http获取图片特征
     * @param objType 目前只支持查询车辆信息，固定为1
     * @param picBy 图片的Base64编码
     * @return 图片特征
     * */
    public static String getExtractFromPictureByGstl(Integer objType,byte[] picBy){
        String featureStr = null;
        try {
            //传入图片二进制base64编码后字符串
            Var paramVar = getFeatureParams(objType,new String(Base64.encode(picBy)));
            String resultData = requestGstl(paramVar.toString());
            Var objextsVar = Var.fromJson(resultData);
            if(CommonConst.GSTL_REQUEST_SUCCESS.equals((objextsVar.getString("Context.Status")))){
                featureStr = objextsVar.getString("Results[0].Vehicles[0].Features");
                if(StringUtil.isNull(featureStr)){
                    log.error("featureData from gstl can not get feature");
                    return null;
                }else{
                    log.info( "----featureData from gstl string length:" + featureStr.length());
                }
            }else{
                log.error("rest获取图片矢量特征失败："+ objextsVar.getString("Context.Status"));
                log.error(String.valueOf(objextsVar.getString("Context.Message")));
            }
        } catch (HttpConnectionException e) {
            log.error("getExtractFromPictureByGstl error",e);
            log.error("rest获取图片特征失败");
        }
        return featureStr;
    }

    public static String requestGstl(String params) throws HttpConnectionException {
         return PostUtil.requestContent(
                OESObjectHandleManagerUtil.getGstlUrl(),"application/json",params);
    }

    /**
     * 获取图片特征body参数
     * @param objType 类型，目前固定为1
     * @param arrs 图片base64编码字符串，支持多个
     * */
    public static Var getFeatureParams(Integer objType,String ... arrs){
        Var paramVar = Var.newObject();
        paramVar.set("Context.Functions",CAR_FUNCTION_TYPE);
        paramVar.set("Context.Type",objType);
        List<Map<String,Map<String,String>>> imageList = new ArrayList<>(arrs.length);//固定List长度
        for (int i = 0 ; i < arrs.length ; i++){
            Map<String,String> map = new HashMap<>(2);      //固定Map长度
            map.put("Id",(i+1)+"");
            map.put("BinData",arrs[i]);
            Map<String,Map<String,String>> data = new HashMap<>(1);      //固定Map长度
            data.put("Data",map);
            imageList.add(data);
        }
        paramVar.set("Images",imageList);
        return paramVar;
    }

   /* public static Var getFeatureParamsByList(List<PictureInfo> list){
        Var paramVar = Var.newObject();
        paramVar.set("Context.Functions",CAR_FUNCTION_TYPE);
        paramVar.set("Context.Type",1);
        List<Map<String,Map<String,String>>> imageList = new ArrayList<>(list.size());//固定List长度
        for(PictureInfo pictureInfo : list){
            Map<String,String> map = new HashMap<>(2);      //固定Map长度
            map.put("Id",pictureInfo.getId()+"");
            map.put("BinData",pictureInfo.getPicBase64());
            Map<String,Map<String,String>> data = new HashMap<>(1);      //固定Map长度
            data.put("Data",map);
            imageList.add(data);
        }
        paramVar.set("Images",imageList);
        return paramVar;
    }*/

    public static Var getFeatureParamsByMap(List<Map<String,String>> paramList){
        Var paramVar = Var.newObject();
        paramVar.set("Context.Functions",CAR_FUNCTION_TYPE);
        paramVar.set("Context.Type",1);
        List<Map<String,Map<String,String>>> imageList = new ArrayList<>(paramList.size());//固定List长度
        for(Map<String,String> map : paramList){
            Map<String,Map<String,String>> data = new HashMap<>(1);      //固定Map长度
            data.put("Data",map);
            imageList.add(data);
        }
        paramVar.set("Images",imageList);
        return paramVar;
    }

//    public static void main(String[] args) {
//        System.out.println(getExtractFromPictureByGstl(1,getImgStr("C:\\Users\\kellen\\Desktop\\picture\\car1.jpg")));
//    }

//    /**
//     * 将图片转换成Base64编码
//     * @param imgFile 待处理图片
//     * @return
//     */
//    public static byte[]  getImgStr(String imgFile){
//        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
//        java.io.InputStream in = null;
//        byte[] data = null;
//        //读取图片字节数组
//        try{
//            in = new java.io.FileInputStream(imgFile);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        }catch (java.io.IOException e){
//            e.printStackTrace();
//        }
//        return data;
//    }
}
