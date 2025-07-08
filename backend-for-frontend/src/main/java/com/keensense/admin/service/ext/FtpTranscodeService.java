package com.keensense.admin.service.ext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.admin.constants.InterfaceConstants;
import com.keensense.admin.util.FtpHttpGetUtils;
import com.keensense.admin.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
     * 查询转码状态
     *
     * @param transcodingId
     * @return
     */
    public String queryTranscodeStatus(String transcodingId) {
        LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();
        paramMap.put("id", transcodingId);
        String serviceName = InterfaceConstants.QUERY_TRANSCODE_STATUS;
        String respTransProgress = FtpHttpGetUtils.getHttp(serviceName, paramMap);
        return respTransProgress;
    }

    public String getTranscodingProgress(String transcodingId) {
        String respTransProgress = queryTranscodeStatus(transcodingId);
        log.info("----->transcodingId:" + transcodingId + ",----> respTransProgress : " + respTransProgress);
        respTransProgress = checkJsonString(respTransProgress);
        return respTransProgress;
    }

    /**
     * 保证JSON字符串的正确
     *
     * @param respTransProgress
     * @return
     */
    public String checkJsonString(String respTransProgress) {
        if (com.keensense.admin.util.StringUtils.isNotEmptyString(respTransProgress)) {
            for (int i = 0; i < respTransProgress.length(); i++) {
                if ('{' == respTransProgress.charAt(i)) {
                    respTransProgress = respTransProgress.substring(i);
                    break;
                }
            }
        }
        return respTransProgress;
    }

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
        String serviceName = InterfaceConstants.GET_REAL_CAMERA_SNAPSHOT;
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

    public String addTranscodeTask(String fileName, String videoType) {
        LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();
        paramMap.put("filename", fileName);
        // 视频类型
        paramMap.put("video_type", videoType);
        String serviceName = InterfaceConstants.ADD_TRANSCODE_TASK;
        String addtranscodetaskReponse = FtpHttpGetUtils.getHttp(serviceName, paramMap);
        log.info("转码文件: " + fileName + ",视频类型: " + videoType + ",添加转码返回报文：" + addtranscodetaskReponse);
        return checkJsonString(addtranscodetaskReponse);
    }

    /**
     * 上传文件(注：删除返回数据无法json解析！！)
     *
     * @param id
     * @return
     */
    public String deleteTransCodeTask(String id) {
        log.info("删除id:" + id);
        LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();
        paramMap.put("id", id);
        String serviceName = InterfaceConstants.DELETE_TRANSCODE_TASK;
        String addtranscodetaskReponse = FtpHttpGetUtils.getHttp(serviceName, paramMap);
        addtranscodetaskReponse = checkJsonString(addtranscodetaskReponse);
        return addtranscodetaskReponse;
    }

}
