package com.keensense.job.service;

/**
 * @Author cuiss
 * @Description cron操作的服务类
 * @Date 2018/11/6
 */
public interface ICronService {

    /**
     * 获取cron表达式
     * @return
     */
    public String getCronScript();

}
