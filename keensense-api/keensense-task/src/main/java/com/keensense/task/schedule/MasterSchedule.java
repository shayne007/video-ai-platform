package com.keensense.task.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.dataconvert.framework.common.utils.file.FileUtil;
import com.keensense.task.util.IpUtils;
import com.keensense.task.util.ValidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/9/28 16:50
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
@Component
public class MasterSchedule {

    /**objext的app配置文件地址*/
    private static final String OBJEXT_PATH = "/home/task/objext/VideoObjExtService.json";
    private static String masterIp = "127.0.0.1";

    private static String masterId;

   // @Scheduled(cron = "* 0/1 * * * ?")
    public static void reloadLocalIp() {
//        if(MysqlUtil.isMysql()){
//            getLocalIp();
//        }else {
            String ip = IpUtils.getLocalIp();
            log.info(">>>>>local ip:"+ip);
            if(!StringUtils.isEmpty(ip)) {
                masterIp = ip;
            }
 //       }
    }

    @Deprecated
    public static void getLocalIp() throws IOException {
        //读取objext的配置文件
        String fileStr = FileUtil.txt2String(new File(OBJEXT_PATH));
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(fileStr)) {
            JSONObject jsonObject = JSON.parseObject(fileStr);
            String ip = jsonObject.getString("ip");
            if(ValidUtil.isIp(ip)){
                masterIp = ip;
            }else{
                log.error("can not load ip with file = " + OBJEXT_PATH);
            }
        }else{
            log.error("can not load config file = " + OBJEXT_PATH);
        }
    }

    public static String getMasterIp() {
        return masterIp;
    }

    public static String getMasterId() {
        return masterId;
    }

    public static void setMasterId(String masteId) {
        MasterSchedule.masterId = masteId;
    }
}
