package com.keensense.alarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.alarm.dto.DispositionNotification;
import com.keensense.alarm.entity.DispositionNotificationEntity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ycl
 * @since 2019-05-14
 */
public interface IDispositionNotificationService extends IService<DispositionNotificationEntity> {


    /**
     * 查找告警消息
     * @param param url参数
     * @return 告警对象(包含告警目标）
     */
    List<DispositionNotification> findDispositionByParam(String param);


    /**
     * 添加告警消息
     * @param dtos dto
     * @return boolean
     */
    boolean addDispositionNotifications(List<DispositionNotification> dtos);

    /**
     * 生成告警消息
     * @param msg producer msg
     * @return 告警对象
     */
    List<DispositionNotificationEntity> generateNotifications(String msg);



    List<DispositionNotificationEntity> generateNotifications(List<String> msg);

}
