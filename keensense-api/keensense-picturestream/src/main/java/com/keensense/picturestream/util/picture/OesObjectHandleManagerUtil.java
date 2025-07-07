package com.keensense.picturestream.util.picture;

import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.FileUtil;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

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

}
