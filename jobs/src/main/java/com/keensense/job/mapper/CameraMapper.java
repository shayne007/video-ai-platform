package com.keensense.job.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keensense.job.entity.Camera;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 视频点位表 Mapper 接口
 * </p>
 *
 * @author cuiss
 * @since 2018-11-05
 */
@Mapper
public interface CameraMapper extends BaseMapper<Camera> {

    /**
     * 查询所有的Camaera记录
     * @return
     */
    public List<Camera> selectAllCameras();

    /**
     * 新增记录
     */
    public void insertCamera();

    /**
     * 更新记录
     */
    public void updateCameraByPrimaryKey();

}
