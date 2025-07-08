package com.keensense.task.util.oldclean;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * @Description: 老版本删除工具类，切换为fastdfs后可删除
 * @Author: wujw
 * @CreateDate: 2019/9/28 15:19
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class ShellUtil {

    private ShellUtil(){}

    private static final String DELETE_PICTURE_OBJEXT = "/home/task/deletePictureBySernum.sh";

    /**输出插件的app配置文件地址*/
    public static final String PICTURE_ROOT = "/home/task/objext/objext_result/";

    /***
     * @description: 清理结构化快照
     * @param serialnumber 删除任务号
     * @param ymd 删除时间
     * @return: java.lang.String
     */
    public static boolean deletePicture(String serialnumber,String ymd) {

        if(!fileExists(DELETE_PICTURE_OBJEXT)){
            log.error("shell is not exitst! file path = " + DELETE_PICTURE_OBJEXT);
            return false;
        }

        String command = "sh " + DELETE_PICTURE_OBJEXT +" ";
        if(StringUtils.isNotEmpty(ymd)){
            command += PICTURE_ROOT + "/" + ymd + "/ " + serialnumber;
        }else{
            command +=  PICTURE_ROOT + " " + serialnumber;
        }
        log.info("serialnumber : "+serialnumber+",getDeleteObjext command : " + command);
        try {
            LinuxUtils.execueteCommand(command);
            return true;
        } catch (IOException e) {
            log.error("execute shell failed！ command = " + command);
            return false;
        }
    }

    /***
     * @description: 判断文件是否存在
     * @param filePath 文件路径
     * @return:
     */
    private static boolean fileExists(String filePath){
        return new File(filePath).exists();
    }
}
