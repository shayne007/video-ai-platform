package com.keensense.admin.service.task;

import com.keensense.admin.dto.FileBo;
import com.keensense.admin.util.Page;
import com.keensense.admin.vo.ResultQueryVo;

import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:36 2019/6/17
 * @Version v0.1
 */
public interface IImageQueryService {
    /**
     * 查询目标结果
     *
     * @param pages
     * @param objtype
     * @param startTime
     * @param endTime
     * @return
     */
    Map<String, Object> initObjextResult(String objtype, Page<FileBo> pages, String startTime, String endTime);

    /**
     * 转换单个result
     *
     * @param bo
     * @return
     */
    void resultHandle(ResultQueryVo bo);


    /**
     * 删除轨迹库信息
     * @param resultid
     * @param objtype
     * @param serialnumber
     */
    int deleteTract(String resultid,String objtype,String serialnumber);
}
