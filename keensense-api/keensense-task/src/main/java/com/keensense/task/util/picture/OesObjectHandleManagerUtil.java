package com.keensense.task.util.picture;

import com.alibaba.fastjson.JSONObject;
import com.keensense.dataconvert.framework.common.utils.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * @Description: 获取配置信息
 * @Author: wujw
 * @CreateDate: 2019/5/27 15:02
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class OesObjectHandleManagerUtil {

    private OesObjectHandleManagerUtil(){}

    private static final String OBJEXT_JSON = "/u2s/slave/objext/objext/OESObjectHandlerManager.json";

    private static final String SUMMARY_JSON = "/u2s/slave/objext/VideoSummaryExtraction/OESObjectHandlerManager.json";

    /**是否是格林算法*/
    private static boolean isGstl = false;
    /**格林接口地址*/
    private static String gstlUrl = "http://127.0.0.1:6521/rec/image/batch";

    public static boolean getIsGstl(){
        return isGstl;
    }

    public static String getGstlUrl(){
        return gstlUrl;
    }
    /**格林车辆识别编码为2*/
    private static final int GSTL_TYPE = 2;

    public static void initGstl() throws IOException {
        File file = new File(OBJEXT_JSON);
        if(!file.exists()){
            file = new File(SUMMARY_JSON);
        }
        if(file.exists()){
            JSONObject manageConfigVar = JSONObject.parseObject(FileUtil.txt2String(file));
            String enable = manageConfigVar.getString("vlpr_further_recog.enable");
            String replaceFature = manageConfigVar.getString("vlpr_further_recog.replace_feature");
            if(StringUtils.isNotEmpty(enable) && StringUtils.isNotEmpty(replaceFature)
                    && Boolean.valueOf(enable) && Integer.valueOf(replaceFature) == GSTL_TYPE){
                isGstl = true;
                gstlUrl = manageConfigVar.getString("vlpr_further_recog.vlpr_url");
            }
        }else{
            log.error("can not find file where file path = "+file.getPath());
        }
    }
}
