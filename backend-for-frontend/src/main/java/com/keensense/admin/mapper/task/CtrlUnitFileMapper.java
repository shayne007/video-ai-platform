package com.keensense.admin.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.entity.task.CtrlUnitFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface CtrlUnitFileMapper extends BaseMapper<CtrlUnitFile> {
    List<CtrlUnitFile> queryOfflineVideo(Page<CtrlUnitFile> pages, @Param("params") Map<String,Object> params);

    CtrlUnitFile queryOfflineVideoByFileId(String fileId);

    List<CtrlUnitFile> selectCtrlUnitFileByTask();

    CtrlUnitFile selectRecentInterest(String cameraId);
}
