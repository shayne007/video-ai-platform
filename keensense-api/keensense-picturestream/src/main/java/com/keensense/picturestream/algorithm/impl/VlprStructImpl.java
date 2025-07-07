package com.keensense.picturestream.algorithm.impl;

import com.keensense.picturestream.algorithm.IVlprStruct;
import com.keensense.picturestream.entity.PictureInfo;
import com.loocme.sys.datastruct.IVarForeachHandler;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.PostUtil;
import com.loocme.sys.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/7/6 10:51
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@Slf4j
public class VlprStructImpl implements IVlprStruct{


    /**格林接口地址*/
    private static String gstlUrl = "http://127.0.0.1:6521/rec/image/batch";
    /**格林车辆识别编码为2*/
    private static final int GSTL_TYPE = 2;
    /**格林车辆类型为1*/
    private static final int GSTL_VLPR_TYPE = 1;
    /**获取车辆特征的类型参数*/
    private static final int[] CAR_FUNCTION_TYPE = {100,101,102,103,104,105,106,107,108,109};

    public static final String GSTL_REQUEST_SUCCESS = "200";

    @Override
    public void init(Var params){
        String replaceFature = params.getString("vlpr_further_recog.replace_feature");
        if(StringUtil.isNotNull(replaceFature) && Integer.valueOf(replaceFature) == GSTL_TYPE){
            setGstlUrl(params.getString("vlpr_further_recog.vlpr_url")+"/rec/image/batch");
        }
    }

    @Override
    public void recog(PictureInfo pictureInfo){
        if(StringUtil.isNull(pictureInfo.getPicBase64())){
            return ;
        }
        Var respVar = getStructInfo(GSTL_VLPR_TYPE,pictureInfo.getPicBase64());
        if(respVar!=null && !respVar.isNull()){
            respVar.foreach(new IVarForeachHandler() {

                private static final long serialVersionUID = 1L;

                @Override
                public void execute(String index, Var faceVar)
                {
                    faceVar.set("AlgoSource",PictureInfo.RECOG_TYPE_THIRD_VLPR);
                    pictureInfo.addResult(faceVar);
                }
            });
        }
    }

    @Override
    public void recog(List<PictureInfo> pictureList){
        if(CollectionUtils.isEmpty(pictureList)){
            return ;
        }
        String[] picBaseArray = new String[pictureList.size()];
        int i=0;
        for (PictureInfo pictureInfo:pictureList){
            picBaseArray[i] = pictureInfo.getPicBase64();
            i++;
        }
        Var respVar = getStructInfo(GSTL_VLPR_TYPE,picBaseArray);
        if(respVar!=null && !respVar.isNull()){
            respVar.foreach(new IVarForeachHandler() {

                private static final long serialVersionUID = 1L;

                @Override
                public void execute(String index, Var faceVar)
                {
                    int imageId = faceVar.getInt("Image.Data.Id");
                    faceVar.set("AlgoSource",PictureInfo.RECOG_TYPE_THIRD_VLPR);
                    pictureList.get(imageId-1).addResult(faceVar);
                }
            });
        }
    }

    @Override
    public int getBatchSize(){
        return 8;
    }

    public static Var getStructInfo(Integer type,String... picBase64){
        try{

            String rslt = PostUtil.requestContent(gstlUrl,"application/json",
                getStructParams(type,picBase64));
            if(StringUtil.isNull(rslt)){
                return null;
            }
            Var objextsVar = Var.fromJson(rslt);
            if(GSTL_REQUEST_SUCCESS.equals((objextsVar.getString("Context.Status")))){
                return objextsVar.get("Results");
            }
        }catch (Exception e){
            log.error("glst vlpr getStructInfo error",e);
        }
        return null;
    }

    /**
     * 获取图片特征参数
     * @param objType 类型，目前只支持查询车辆信息,类型为1
     * @param arrs 图片base64编码字符串，支持多个
     * */
    public static String getStructParams(Integer objType,String... arrs){
        Var paramVar = Var.newObject();
        paramVar.set("Context.Functions",CAR_FUNCTION_TYPE);
        paramVar.set("Context.Type",objType);
        //固定List长度
        List<Map<String,Map<String,String>>> imageList = new ArrayList<>(arrs.length);
        for (int i = 0 ; i < arrs.length ; i++){
            //固定Map长度
            Map<String,String> map = new HashMap<>(2);
            map.put("Id",""+(i+1));
            map.put("BinData",arrs[i]);
            //固定Map长度
            Map<String,Map<String,String>> data = new HashMap<>(1);
            data.put("Data",map);
            imageList.add(data);
        }
        paramVar.set("Images",imageList);
        return paramVar.toString();
    }

    public static void setGstlUrl(String url) {
        gstlUrl = url;
    }
}

