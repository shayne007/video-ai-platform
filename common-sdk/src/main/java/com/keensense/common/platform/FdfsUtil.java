package com.keensense.common.platform;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.common.platform.constant.WebserviceConstant;
import com.keensense.common.util.HttpU2sGetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:28 2019/12/28
 * @Version v0.1
 */
@Slf4j
public class FdfsUtil {
    public static String saveImage(String requestUrl, String imageBase64) {
        String imageUrl = "";
        requestUrl += WebserviceConstant.SAVE_FDFS_IMAGE + IdUtil.randomUUID().replaceAll("-", "") + "/Data";
        String resultResponse = HttpU2sGetUtil.postContent(requestUrl, imageBase64);
        try {
            JSONObject jsonObject = JSONObject.parseObject(resultResponse);
            imageUrl = jsonObject.getString("ImageUrl");
        } catch (Exception e) {
            log.error("调用FDFS存储图片数据失败:" + e);
            throw new VideoException("调用FDFS存储图片数据错误");
        }
        return imageUrl;
    }
}
