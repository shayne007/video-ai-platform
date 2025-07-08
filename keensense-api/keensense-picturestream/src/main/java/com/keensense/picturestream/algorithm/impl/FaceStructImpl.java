package com.keensense.picturestream.algorithm.impl;/**
 * Created by zhanx xiaohui on 2019/7/6.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.util.HttpClientUtil;
import com.keensense.picturestream.algorithm.IFaceStruct;
import com.keensense.picturestream.entity.PictureInfo;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/7/6 10:51
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class FaceStructImpl implements IFaceStruct {

    private static String faceUrl = "http://127.0.0.1:18051/keensense/qstface/getFacesOnImageBase64";

    @Override
    public void init(Map<String,Object> params) {
        String url = (String) params.get("face_qst_recog.face_url");
        if (StringUtils.isNotEmpty(url)) {
            setFaceUrl(url + "/keensense/qstface/getFacesOnImageBase64");
        }
    }

    @Override
    public void recog(PictureInfo pictureInfo) {
        if (StringUtils.isEmpty(pictureInfo.getPicBase64())) {
            return;
        }
        Map<String,Object> respVar = getStructInfo(pictureInfo.getPicBase64());
        if (respVar != null && !respVar.isEmpty()) {
//            getFaceArray(respVar).foreach(new IVarForeachHandler() {
//
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public void execute(String index, Var resultVar) {
//                    resultVar.set("AlgoSource", PictureInfo.RECOG_TYPE_FACE);
//                    pictureInfo.addResult(resultVar);
//                }
//            });
        }
    }

    @Override
    public void recog(List<PictureInfo> pictureList) {
        if (CollectionUtils.isEmpty(pictureList)) {
            return;
        }
        String[] picBaseArray = new String[pictureList.size()];
        int i = 0;
        for (PictureInfo pictureInfo : pictureList) {
            picBaseArray[i] = pictureInfo.getPicBase64();
            i++;
        }
        Map<String,Object> respVar = getStructInfo(picBaseArray);
        if (respVar != null && !respVar.isEmpty()) {
//            getFaceArray(respVar).foreach(new IVarForeachHandler() {
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public void execute(String index, Map<String,Object> resultVar) {
//                    resultVar.put("AlgoSource", PictureInfo.RECOG_TYPE_FACE);
//                    int indexInt = Integer.parseInt(index);
//                    if (indexInt < pictureList.size()) {
//                        pictureList.get(indexInt).addResult(resultVar);
//                    } else {
//                        log.error("face struct error indexInt=" + indexInt + "//json " + respVar.toString());
//                    }
//                }
//            });
        }
    }

    @Override
    public int getBatchSize() {
        return 8;
    }

    /**
     * 根据base64图片数据，调用人脸识别接口
     *
     * @param picBase64
     * @return
     */
    public static Map<String,Object> getStructInfo(String... picBase64) {
        Map<String,Object> returnVar = new HashMap<>();
        try {
            String rslt = HttpClientUtil.requestPost(faceUrl, "application/json", getStructParams(picBase64));
            if (StringUtils.isEmpty(rslt)) {
                return null;
            }
            JSONObject objextsVar = (JSONObject) JSONObject.parseObject(rslt.replaceAll("\n", "")
                    .replaceAll("\t", "")).get("list");
            if (objextsVar != null && !objextsVar.isEmpty()) {
//                objextsVar.foreach(new IVarForeachHandler() {
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    public void execute(String index, Var resultVar) {
//                        String faces = "faces";
//                        returnVar.set(faces, resultVar.get(faces));
//                    }
//                });
            }
        } catch (Exception e) {
            log.error("objExt getStructInfo error", e);
        }
        return returnVar;
    }

    /**
     * 获取图片特征参数
     *
     * @param arrs 图片base64编码字符串，支持多个
     */
    public static String getStructParams(String... arrs) {
        Map<String,Object> paramVar = new HashMap<>();
        //固定List长度
        List<String> imageList = new ArrayList<>(Arrays.asList(arrs));
        paramVar.put("images", imageList);
        return paramVar.toString();
    }
    
    /***
     * @description: 获取json中的人脸信息
     * @param var   json
     * @return: com.loocme.sys.datastruct.WeekArray
     */
    private JSONArray getFaceArray(Map<String,Object> var){
        return (JSONArray) var.get("faces");
    }

    public static void setFaceUrl(String faceUrl) {
        FaceStructImpl.faceUrl = faceUrl;
    }
}

