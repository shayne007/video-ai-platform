package com.keensense.job.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keensense.job.entity.Cron;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author cuiss
 * @Description //操作cron表的Mapper
 * @Date 2018/11/6
 */
@Mapper
public interface CronMapper extends BaseMapper<Cron> {

    /**
     * 获取cron表达式
     * @return
     */
    public String getCronScript();

}
