package com.keensense.admin.service.ext;

import java.util.Map;

/**
 * @Author cuiss
 * @Description // 任务暂停、重启、删除接口
 * @Date 2018/10/13
 */
public interface PictureObjectTaskService {
    public String startPictureTaskService(Map<String,Object> paramMap);

    public String stopPictureTaskService(Map<String,Object> paramMap);

    public String deletePictureTaskService(Map<String,Object> paramMap);
}
