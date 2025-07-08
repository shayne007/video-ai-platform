package com.keensense.densecrowd.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.densecrowd.entity.task.Camera;
import com.keensense.densecrowd.vo.CameraVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 10:52 2019/9/25
 * @Version v0.1
 */
@Mapper
public interface CameraMapper extends BaseMapper<Camera> {

    CameraVo selectByPrimaryKey(Long id);

    List<CameraVo> selectCameraIsvalidList(@Param("param") Map<String, String> param);

    List<CameraVo> selectNewStartCamera(Integer num);

    String queryNameBySerialnumber(String serialnumber);

    /**
     * 根据指定条件查询监控点
     * @return
     */
    List<CameraVo> selectCameraByCameraType();

    /**
     * 查询实时点位信息
     * @param page
     * @param params
     * @return
     */
    List<CameraVo> selectOnlineAndIpCCameraList(Page<CameraVo> page, @Param("params") Map<String, Object> params);
}
