package com.keensense.admin.service.lawcase.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.admin.constants.UploadConstants;
import com.keensense.admin.entity.sys.SysUser;
import com.keensense.admin.service.lawcase.UploadDataToJdService;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.FileUtils;
import com.keensense.admin.util.IpUtils;
import com.keensense.admin.util.PropertiesUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.UploadDataToJdVo;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.exception.HttpConnectionException;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.HttpsUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用佳都提供的接口上传归档数据
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2018/12/19
 */
@Service
public class UploadDataToJdServiceImpl implements UploadDataToJdService {

    private static final Logger logger = Logger.getLogger(UploadDataToJdServiceImpl.class);

    @Override
    public boolean uploadData(String type, UploadDataToJdVo uploadDataToJdBo, SysUser sysUser) {
        String requestUrl = "";
        Boolean resFlag = true;
        Map<String, String> paramsMap = new HashMap<>();
        try {
            // 佳都用户视图的USER_CODE，存在user表的username
            String remoteUser = sysUser.getUsername();
            String caseId = uploadDataToJdBo.getCaseId();

            paramsMap.put("remoteUser", remoteUser);
            paramsMap.put("CASE_ID", caseId);

            if (UploadConstants.UPLOAD_IMAGE_TYPE.equals(type)) {
                requestUrl = UploadConstants.UPLOAD_IMAGE_URL;
                initImageRequestParams(paramsMap, uploadDataToJdBo);
            }

            if (UploadConstants.UPLOAD_VIDEO_TYPE.equals(type)) {
                requestUrl = UploadConstants.UPLOAD_VIDEO_URL;
                initVideoRequestParams(paramsMap, uploadDataToJdBo);
            }

            if (UploadConstants.UPLOAD_VIDEO_POINT_TYPE.equals(type)) {
                requestUrl = UploadConstants.UPLOAD_VIDEO_POINT_URL;
                initVideoPointRequestParams(paramsMap, uploadDataToJdBo);
            }

            String resJson = "";

            logger.info("requestUrl:" + requestUrl + " ,inParam:" + JSON.toJSONString(paramsMap));
            try {

                resJson = HttpsUtil.post(HttpsUtil.getClient(), requestUrl, paramsMap);

            } catch (HttpConnectionException e) {
                resFlag = false;
                logger.error("接口调用异常: errMsg : " + e.getMessage());
            }

            if (StringUtils.isEmptyString(resJson)) {
                logger.error("返回的resJson数据为空, type :" + type + " ,CaseId : " + caseId + ", remoteUser : " + remoteUser);
                resFlag = false;
                return resFlag;
            }

            logger.info("案件编号CaseId : " + caseId + ",上传的返回结果 ：" + resJson);

            JSONObject jsonObj = JSON.parseObject(resJson);

            Long retCode = jsonObj.getLong("CODE");

            if (!UploadConstants.SUCCESSCODE.equals(retCode)) {
                logger.error("返回码不正确，当前上传的CaseId : " + caseId + ", remoteUser : " + remoteUser + ", 返回数据：" + jsonObj.toJSONString());
                resFlag = false;
                return resFlag;
            }

        } catch (Exception e) {
            logger.error("上传案件归档数据系统出错！", e);
            resFlag = false;
        }
        return resFlag;
    }

    /**
     * 封装上传图片数据
     *
     * @param paramsMap
     * @param uploadDataToJdBo
     */
    public void initImageRequestParams(Map<String, String> paramsMap, UploadDataToJdVo uploadDataToJdBo) {

        paramsMap.put("FILE_NAME", uploadDataToJdBo.getFileName());
        paramsMap.put("CCLJ", uploadDataToJdBo.getCclj());
        paramsMap.put("FILE_SOURCE", uploadDataToJdBo.getFileSource());
        paramsMap.put("LABEL", uploadDataToJdBo.getLabel());
        paramsMap.put("FILE_TYPE", uploadDataToJdBo.getFileType() + "");
    }

    /**
     * 封装上传视频的数据
     *
     * @param paramsMap
     * @param uploadDataToJdBo
     */
    public void initVideoRequestParams(Map<String, String> paramsMap, UploadDataToJdVo uploadDataToJdBo) {

        paramsMap.put("FILE_NAME", uploadDataToJdBo.getFileName());
        paramsMap.put("CCLJ", uploadDataToJdBo.getCclj());
        paramsMap.put("SLTCCLJ", uploadDataToJdBo.getSltcclj());
        paramsMap.put("FILE_SOURCE", uploadDataToJdBo.getFileSource());
        paramsMap.put("LABEL", uploadDataToJdBo.getLabel());
        paramsMap.put("FILE_EXT", uploadDataToJdBo.getFileExt());
        paramsMap.put("IS_EVIDENCE", uploadDataToJdBo.getIsEvidence() + "");
        if (StringUtils.isNotEmptyString(uploadDataToJdBo.getVideoDesc())) {
            paramsMap.put("VIDEO_DESC", uploadDataToJdBo.getVideoDesc());
        }

    }

    /**
     * 封装上传点位的数据
     *
     * @param paramsMap
     * @param uploadDataToJdBo
     */
    public void initVideoPointRequestParams(Map<String, String> paramsMap, UploadDataToJdVo uploadDataToJdBo) {

        paramsMap.put("DEVICE_NAME", uploadDataToJdBo.getDeviceName());
        paramsMap.put("DEVICE_CODE", uploadDataToJdBo.getDeviceCode());
        paramsMap.put("DEVICE_ADDR", uploadDataToJdBo.getDeviceAddr());
        paramsMap.put("LONGITUDE", uploadDataToJdBo.getLongitude() + "");
        paramsMap.put("LATITUDE", uploadDataToJdBo.getLatitude() + "");

    }

    /**
     * 持久化图片数据
     *
     * @param caseCode 案件编号
     * @param imageUrl 图片的url地址
     * @return
     */
    @Override
    public String saveImage(String caseCode, String imageUrl, String pathType) {
        // 获取配置的保存路径
        String caseBasePath = PropertiesUtil.getParameterKey("case_path");

        String nowTime = DateUtil.getFormat(new Date(), DateFormatConst.YMD);
        String picSaveDir = caseBasePath + File.separator + nowTime + File.separator + caseCode;

        String particularPicPath = "";
        if (UploadConstants.PIC_THUMB_TYPE.equals(pathType)) {
            // 小图存放路径
            particularPicPath = picSaveDir + File.separator + "thumb";
        } else if (UploadConstants.PIC_BG_TYPE.equals(pathType)) {
            // 大图存放路径
            particularPicPath = picSaveDir + File.separator + "bg";

        } else if (UploadConstants.VIDEO_COVER_TYPE.equals(pathType)) {
            // 视频封面存放路径
            particularPicPath = picSaveDir + File.separator + "videocover";
        }
        // 创建文件夹
        File bgFile = new File(particularPicPath);
        if (!bgFile.exists()) {
            bgFile.mkdirs();
        }

        String fileOriginalName = "";
        String[] picturUrlArray = imageUrl.split("/");
        if (null != picturUrlArray && picturUrlArray.length > 0) {
            fileOriginalName = picturUrlArray[picturUrlArray.length - 1];
        }

        //图片下载保存的路径
        String picturePath = particularPicPath + File.separator + fileOriginalName;
        FileUtils.dowloadFileFromUrl(imageUrl, picturePath);

        // picturePath = /u2s/case/20181220/caseCode/bg/bike_1215304151-000016_st40_end7480.jpg

        String ngServerPort = DbPropUtil.getString("nginx-server-port");
        if (StringUtils.isEmptyString(ngServerPort)) {
            String hostNginxIp = IpUtils.getRealIpAddr();
            ngServerPort = hostNginxIp + ":8082";
        }
        String picHttpPath = "http://" + ngServerPort + (picturePath.substring(4).replaceAll("\\\\", "/"));

        return picHttpPath;
    }
}
