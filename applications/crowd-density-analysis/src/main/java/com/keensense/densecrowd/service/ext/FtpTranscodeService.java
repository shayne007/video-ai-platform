package com.keensense.densecrowd.service.ext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.densecrowd.util.CommonConstants;
import com.keensense.densecrowd.util.FtpHttpGetUtils;
import com.keensense.densecrowd.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:41 2019/7/20
 * @Version v0.1
 */
@Service("ftpTranscodeService")
@Slf4j
public class FtpTranscodeService {

    /**
     * 获取快照
     *
     * @param cameraUrl
     * @return
     */
    public String getRealCameraSnapshot(String cameraUrl) {
        String camerasnapshot = "";
        log.info("getCameraSnapshot ---> cameraUrl : " + cameraUrl);
        String param = "refresh=1&url=" + cameraUrl;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("refresh", "1");
        paramMap.put("url", cameraUrl);
        String serviceName = CommonConstants.GET_REAL_CAMERA_SNAPSHOT;
        String resultJson = FtpHttpGetUtils.getHttp(serviceName, paramMap);
//        log.info("getCameraSnapshot---> restult resultJson : " + resultJson);
        if (StringUtils.isNotEmpty(resultJson)) {
            JSONObject json = JSON.parseObject(resultJson);
            Integer ret = json.getInteger("ret");
            String snapshotUrl = json.getString("url");
            if (ret != null && ret == 0) {
                camerasnapshot = snapshotUrl;
            }
        }
        return camerasnapshot;
    }

}
