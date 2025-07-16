package com.keensense.image.service;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.util.ResponseUtil;
import com.keensense.image.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhanx xiaohui on 2019-07-22.
 */
@Service
@Slf4j
public class ImageService {
    @Resource(name = "fastDfsImageRepository2")
    ImageRepository imageRepository;

    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    private static final String SUCCESS = "Success";


    /**
     * @param id
     * @param imageBase64
     * @param fileExtentionName
     * @param urlName
     * @return
     */
    public String image(String id, String imageBase64, String fileExtentionName, String urlName, String requestUrl) {
        log.info("handler data , id is {}, fileExtention is {}", id, fileExtentionName);
        JSONObject object = ResponseUtil.createSuccessResponse(id, getRequestUrl(requestUrl));

        if (StringUtils.isEmpty(imageBase64)) {
//            HttpServletResponse response = servletRequestAttributes.getResponse();
//            response.setStatus(557);
            log.error("imageBase64 is null, id is {} ", id);
        } else {
            String imageUrl = imageRepository.saveToFileServer(imageBase64, fileExtentionName, id);

            if (StringUtils.isEmpty(imageUrl)) {
//                HttpServletResponse response = servletRequestAttributes.getResponse();
//                response.setStatus(556);
            } else {
                object.put(urlName, imageUrl);
            }
        }

        return object.toJSONString();
    }

    private String getRequestUrl(String url) {
        if (!StringUtils.isEmpty(url)) {
            return url;
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String requestUrl = request.getRequestURL().toString();
        return requestUrl;
    }

    public String batchDeleteImage(String serialnumber, String time) {
        if (StringUtils.isEmpty(serialnumber) && StringUtils.isEmpty(time)) {
            return ResponseUtil.generatorDeleteResposnse("-1", "Failed", "the parameter is empty");
        }

        pool.execute(() -> imageRepository.batchDelete(serialnumber, time));
        return ResponseUtil.generatorDeleteResposnse("0", SUCCESS, null);
    }
}