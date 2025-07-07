package com.keensense.picturestream.algorithm.impl;/**
 * Created by zhanx xiaohui on 2019/7/6.
 */

import com.keensense.picturestream.algorithm.IFaceStruct;
import com.keensense.picturestream.entity.PictureInfo;
import com.loocme.sys.datastruct.IVarForeachHandler;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.datastruct.WeekArray;
import com.loocme.sys.util.PostUtil;
import com.loocme.sys.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
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
    public void init(Var params) {
        String url = params.getString("face_qst_recog.face_url");
        if (StringUtil.isNotNull(url)) {
            setFaceUrl(url + "/keensense/qstface/getFacesOnImageBase64");
        }
    }

    @Override
    public void recog(PictureInfo pictureInfo) {
        if (StringUtil.isNull(pictureInfo.getPicBase64())) {
            return;
        }
        Var respVar = getStructInfo(pictureInfo.getPicBase64());
        if (respVar != null && !respVar.isNull()) {
            getFaceArray(respVar).foreach(new IVarForeachHandler() {

                private static final long serialVersionUID = 1L;

                @Override
                public void execute(String index, Var resultVar) {
                    resultVar.set("AlgoSource", PictureInfo.RECOG_TYPE_FACE);
                    pictureInfo.addResult(resultVar);
                }
            });
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
        Var respVar = getStructInfo(picBaseArray);
        if (respVar != null && !respVar.isNull()) {
            getFaceArray(respVar).foreach(new IVarForeachHandler() {
                private static final long serialVersionUID = 1L;

                @Override
                public void execute(String index, Var resultVar) {
                    resultVar.set("AlgoSource", PictureInfo.RECOG_TYPE_FACE);
                    int indexInt = Integer.parseInt(index);
                    if (indexInt < pictureList.size()) {
                        pictureList.get(indexInt).addResult(resultVar);
                    } else {
                        log.error("face struct error indexInt=" + indexInt + "//json " + respVar.toString());
                    }
                }
            });
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
    public static Var getStructInfo(String... picBase64) {
        Var returnVar = Var.newObject();
        try {
            String rslt = PostUtil.requestContent(faceUrl, "application/json", getStructParams(picBase64));
            if (StringUtil.isNull(rslt)) {
                return null;
            }
            Var objextsVar = Var.fromJson(rslt.replaceAll("\n", "")
                    .replaceAll("\t", "")).get("list");
            if (objextsVar != null && !objextsVar.isNull()) {
                objextsVar.foreach(new IVarForeachHandler() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void execute(String index, Var resultVar) {
                        String faces = "faces";
                        returnVar.set(faces, resultVar.get(faces));
                    }
                });
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
        Var paramVar = Var.newObject();
        //固定List长度
        List<String> imageList = new ArrayList<>(Arrays.asList(arrs));
        paramVar.set("images", imageList);
        return paramVar.toString();
    }
    
    /***
     * @description: 获取json中的人脸信息
     * @param var   json
     * @return: com.loocme.sys.datastruct.WeekArray
     */
    private WeekArray getFaceArray(Var var){
        return var.getArray("faces");
    }

    public static void setFaceUrl(String faceUrl) {
        FaceStructImpl.faceUrl = faceUrl;
    }
}

