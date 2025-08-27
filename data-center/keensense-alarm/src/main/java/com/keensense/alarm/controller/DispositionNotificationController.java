package com.keensense.alarm.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.alarm.dto.DispositionNotification;
import com.keensense.alarm.entity.DispositionNotificationEntity;
import com.keensense.alarm.service.IDispositionNotificationService;
import com.keensense.common.annotation.VIID;
import com.keensense.common.base.BaseController;
import com.keensense.common.util.ApiResponseEnum;
import com.keensense.common.util.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ycl
 * @since 2019-05-14
 */
@RestController
@RequestMapping("/VIID/DispositionNotifications")
@Slf4j
@VIID
public class DispositionNotificationController extends BaseController {

    @Autowired
    private IDispositionNotificationService notificationService;

    @GetMapping
    public List<DispositionNotification> listDispositionNotifications() {
        return notificationService.findDispositionByParam(super.getUrlParam());
    }

    @PostMapping
    public List<ResponseStatus> addDispositionNotifications(@RequestBody List<DispositionNotification> dtos) {
        boolean result = notificationService.addDispositionNotifications(dtos);
        int size = dtos.size();
        String[] ids = new String[size];
        for (int i = 0; i < size; i++) {
            ids[i] = dtos.get(i).getNotificationId();
        }
        return result ? generateSuccessList(ids) : generateFailList(ids);
    }

    @DeleteMapping
    public List<ResponseStatus> delDispositionNotifications(@RequestParam("IDList") String ids) {

        boolean result = notificationService.removeByIds(Arrays.asList(ids.split(",")));
        String[] delIds = ids.split(",");
        return result ? generateSuccessList(delIds) : generateFailList(delIds);
    }

    @PutMapping
    public ResponseStatus updateDispositionNotifications(@RequestBody DispositionNotification dispositionNotification) {
        DispositionNotificationEntity disp = new DispositionNotificationEntity();
        boolean result = true;
        BeanUtils.copyProperties(dispositionNotification, disp);
        if (StringUtils.isNotBlank(disp.getNotificationId())){
            String notificationIds = disp.getNotificationId();//支持id逗号分隔修改数据
            String[] split = notificationIds.split(",");
            for (String notificationId : split) {
                disp.setNotificationId(notificationId);
                result = notificationService.updateById(disp);
            }
        }
        return generateResponse(result ? ApiResponseEnum.SUCCESS : ApiResponseEnum.FAIL);
    }

    @PostMapping("/noViewNotificationCount")
    public int noViewNotificationCount(){
        int count = notificationService.count(new QueryWrapper<DispositionNotificationEntity>().eq("disposition_category", 2).eq("view_state", 0));
        return count;
    }
}
