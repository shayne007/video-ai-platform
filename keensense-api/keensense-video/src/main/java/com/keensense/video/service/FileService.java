package com.keensense.video.service;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.ResponseUtil;
import com.keensense.video.repository.FastDfsFileRepository;

import java.text.ParseException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by zhanx xiaohui on 2019-12-17.
 */
@Service
@Slf4j
public class FileService {
    @Autowired
    FastDfsFileRepository fastDfsFileRepository;

    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public String upload(String id, byte[] stream, String extension, String urlName) {
        log.info("handler data , file id is {}, file extension is {}", id, extension);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String requestUrl = request.getRequestURL().toString();

        String imageUrl = fastDfsFileRepository.getUrlAfterUploaded(stream,
                StringUtils.isEmpty(extension) ? "mp4" : extension);

        JSONObject object = ResponseUtil.createSuccessResponse(id, requestUrl);
        object.put(urlName, imageUrl);

        return object.toJSONString();
    }

    public String batchDeleteImage(String serialnumber, String time) {
        try {
            if (StringUtils.isEmpty(serialnumber) && StringUtils.isEmpty(time)) {
                throw new VideoException("the parameter is empty");
            }

            pool.execute(() -> {
                try {
                    fastDfsFileRepository.batchDelete(serialnumber, time);
                } catch (ParseException e) {
                    log.error("", e);
                }
            });
        } catch (Exception e) {
            log.error("", e);
            return ResponseUtil.generatorDeleteResposnse("-1", "Failed", e.getMessage());
        }
        return ResponseUtil.generatorDeleteResposnse("0", "Success", null);
    }
}