package com.keensense.picturestream.algorithm.impl;

import com.keensense.picturestream.algorithm.IObjextStruct;
import com.keensense.picturestream.entity.PictureInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/7/6 10:51
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class ObjextStructImpl implements IObjextStruct {

    /**
     * 格林接口地址
     */
    private static String objextUrl = "http://127.0.0.1:10901/images/recog";
    private static Integer objextSubClass = 1;
    private static Integer objextFace = 1;
    public static final String OBJEXT_REQUEST_SUCCESS = "200";


    @Override
    public void init(Map<String,Object> params) {
        String url = (String) params.get("objext_picture_recog.objextUrl");
        Integer subClass = (Integer) params.get("objext_picture_recog.output.SubClass");
        Integer face = (Integer) params.get("objext_picture_recog.output.Face");
        if(StringUtils.isNotEmpty(url)){
            setObjextUrl(url+"/images/recog");
        }
        setObjextSubClass(subClass);
        setObjextFace(face);
    }

    @Override
    public void recog(PictureInfo pictureInfo) {
//        if(StringUtil.isNull(pictureInfo.getPicBase64())){
//            return ;
//        }
//        Map<String,Object> respVar = getStructInfo(pictureInfo.getPicBase64());
//        if(respVar!=null && !respVar.isNull()){
//            respVar.foreach(new IVarForeachHandler() {
//
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public void execute(String index, Var resultVar)
//                {
//                    resultVar.set("AlgoSource",PictureInfo.RECOG_TYPE_OBJEXT);
//                    pictureInfo.addResult(resultVar);
//                }
//            });
//        }
    }

    @Override
    public void recog(List<PictureInfo> pictureList) {
        if(CollectionUtils.isEmpty(pictureList)){
            return ;
        }
        String[] picBaseArray = new String[pictureList.size()];
        int i=0;
        for (PictureInfo pictureInfo:pictureList){
            picBaseArray[i] = pictureInfo.getPicBase64();
            i++;
        }
//        Var respVar = getStructInfo(picBaseArray);
//        if(respVar!=null && !respVar.isNull()){
//
//            respVar.foreach(new IVarForeachHandler() {
//
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public void execute(String index, Var resultVar)
//                {
//                    int imageId = resultVar.getInt("ImageID");
//                    resultVar.set("AlgoSource",PictureInfo.RECOG_TYPE_OBJEXT);
//                    pictureList.get(imageId-1).addResult(resultVar);
//                }
//            });
//        }
    }

    @Override
    public int getBatchSize() {
        return 8;
    }

//    public static Map<String,Object> getStructInfo(String... picBase64){
//        try{
//            String rslt = PostUtil.requestContent(objextUrl,"application/json",
//                getStructParams(picBase64));
//            log.info("picture jiegouhua:" + rslt);
//            if(StringUtil.isNull(rslt)){
//                return null;
//            }
//            Var objextsVar = Var.fromJson(rslt.replaceAll("\n","").replaceAll("\t",""));
//            if(OBJEXT_REQUEST_SUCCESS.equals((objextsVar.getString("ret")))){
//                return objextsVar.get("ObjectList");
//            }
//        }catch (Exception e){
//            log.error("objext getStructInfo error",e);
//        }
//        return null;
//    }
//
//    /**
//     * 获取图片特征参数
//     * @param arrs 图片base64编码字符串，支持多个
//     * */
//    public static String getStructParams(String ... arrs){
//        Var paramVar = Var.newObject();
//        paramVar.set("Output.SubClass",objextSubClass);
//        paramVar.set("Output.Face",objextFace);
//        //固定List长度
//        List<Map<String,String>> imageList = new ArrayList<>(arrs.length);
//        for (int i = 0 ; i < arrs.length ; i++){
//            //固定Map长度
//            Map<String,String> map = new HashMap<>(2);
//            map.put("ImageID",""+(i+1));
//            map.put("Data",arrs[i]);
//            imageList.add(map);
//        }
//        paramVar.set("ImageList",imageList);
//        paramVar.set("apiout","1");
//        return paramVar.toString();
//    }

    public static void setObjextUrl(String objextUrl) {
        ObjextStructImpl.objextUrl = objextUrl;
    }

    public static void setObjextSubClass(Integer objextSubClass) {
        ObjextStructImpl.objextSubClass = objextSubClass;
    }

    public static void setObjextFace(Integer objextFace) {
        ObjextStructImpl.objextFace = objextFace;
    }
}

