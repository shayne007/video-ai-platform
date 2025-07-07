package com.keensense.sdk.util;

import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.FileUtil;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
@Slf4j
public class OESObjectHandleManagerUtil {


    private static boolean IS_GSTL = false;
    private static String GSTL_URL = "http://127.0.0.1:6521/rec/image/batch";
    private static String LOCAL_RUL;
    private static String HTTP_RUL;

    public static boolean getIsGstl(){
        return IS_GSTL;
    }

    public static String getGstlUrl(){
        return GSTL_URL;
    }

    public static String getLocalRul() {
        return LOCAL_RUL;
    }

    public static String getHttpRul() {
        return HTTP_RUL;
    }

    private final static int GSTL_TYPE = 2; //格林车辆识别编码为2

    public static void initGstl(){
        String filePath = "/u2s/slave/objext/objext/OESObjectHandlerManager.json";
//        String filePath = "D:\\项目\\jm-config\\OESObjectHandlerManager.json";
        File file = new File(filePath);
        if(file.exists()){
            Var manageConfigVar = Var.fromJson(FileUtil.read(file));
            String enable = manageConfigVar.getString("vlpr_further_recog.enable");
            String replaceFature = manageConfigVar.getString("vlpr_further_recog.replace_feature");
            if(StringUtil.isNotNull(enable) && StringUtil.isNotNull(replaceFature)
                    && Boolean.valueOf(enable) && Integer.valueOf(replaceFature) == GSTL_TYPE){
                IS_GSTL = true;
                GSTL_URL = manageConfigVar.getString("vlpr_further_recog.vlpr_url");
            }
            LOCAL_RUL = manageConfigVar.getString("storage.local")+"/";
            HTTP_RUL = manageConfigVar.getString("storage.url");
        }else{
            log.error("can not find file where file path = "+filePath);
        }
    }
}
