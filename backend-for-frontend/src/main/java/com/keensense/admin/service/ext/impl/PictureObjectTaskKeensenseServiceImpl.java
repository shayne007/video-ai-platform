package com.keensense.admin.service.ext.impl;

import com.keensense.admin.service.ext.PictureObjectTaskService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author cuiss
 * @Description // 任务暂停、重启、删除接口
 * @Date 2018/10/13
 */
@Service("pictureObjectTaskService"+ AbstractService.KS)
public class PictureObjectTaskKeensenseServiceImpl extends AbstractService implements PictureObjectTaskService{
    @Override
    public String startPictureTaskService(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public String stopPictureTaskService(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public String deletePictureTaskService(Map<String, Object> paramMap) {
        return null;
    }
}
