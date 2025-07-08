package com.keensense.densecrowd.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.common.util.DateTime;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.mapper.task.VsdTaskRelationMapper;
import com.keensense.densecrowd.service.ext.VideoObjextTaskService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.util.IdUtils;
import com.keensense.densecrowd.util.StringUtils;
import com.keensense.densecrowd.util.ValidateHelper;
import com.keensense.densecrowd.vo.AlarmTaskQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 14:34 2019/9/25
 * @Version v0.1
 */
@Service("vsdTaskRelationService")
public class VsdTaskRelationServiceImpl
        extends ServiceImpl<VsdTaskRelationMapper, VsdTaskRelation> implements IVsdTaskRelationService {

    @Resource
    VsdTaskRelationMapper vsdTaskRelationMapper;

    @Autowired
    VideoObjextTaskService videoObjextTaskService;

    @Override
    public VsdTaskRelation queryVsdTaskRelationByCameraFileId(String cameraFileId, String taskUserId) {
        List<VsdTaskRelation> relations = vsdTaskRelationMapper.selectList(new QueryWrapper<VsdTaskRelation>()
                .eq("camera_file_id", cameraFileId)
                .eq(StringUtils.isNotEmptyString(taskUserId), "task_user_id", taskUserId));
        if (relations != null && !relations.isEmpty()) {
            VsdTaskRelation relation = relations.get(0);
            return relation;
        }
        return null;
    }

    @Override
    public String getSerialnumber() {
        Long serialnumberL = 0L;
        List<VsdTaskRelation> vsdTaskList = null;
        do {
            serialnumberL = IdUtils.getTaskId();
            vsdTaskList = baseMapper.selectList(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumberL));
        } while (ValidateHelper.isNotEmptyList(vsdTaskList));
        String serialnumber = serialnumberL + "";
        return serialnumber;
    }

    @Override
    public boolean insertTaskRelation(String serialnumber, String fileId, Integer fromType, String c1) {
        VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
        vsdTaskRelation.setId(System.currentTimeMillis());
        vsdTaskRelation.setTaskId(System.currentTimeMillis());
        vsdTaskRelation.setSerialnumber(serialnumber);
        vsdTaskRelation.setCreatetime(DateTime.getCurrentTime());
        vsdTaskRelation.setLastUpdateTime(vsdTaskRelation.getCreatetime());
        vsdTaskRelation.setCameraFileId(Long.valueOf(fileId));
        vsdTaskRelation.setFromType(Long.valueOf(fromType));
        vsdTaskRelation.setCreateuser(1L);
        vsdTaskRelation.setC1(c1);
        baseMapper.insert(vsdTaskRelation);
        return true;
    }

    @Override
    public int countRealTask(long isvalid) {
        return baseMapper.selectCount(new QueryWrapper<VsdTaskRelation>().eq("isvalid", isvalid));
    }

    @Override
    public List<VsdTaskRelation> queryListByDeviceIds(String[] deviceIds) {
        return baseMapper.selectList(new QueryWrapper<VsdTaskRelation>().in(deviceIds.length > 0, "camera_id", deviceIds));
    }


    @Override
    public IPage<VsdTaskRelation> queryListByDeviceIds(AlarmTaskQueryRequest alarmTaskQueryRequest) {
        return baseMapper.selectPage(new Page<VsdTaskRelation>(alarmTaskQueryRequest.getCurrentPage(), alarmTaskQueryRequest.getPageSize()), new QueryWrapper<VsdTaskRelation>().ne("task_status", -1).eq(StringUtils.isNotEmpty(alarmTaskQueryRequest.getDeviceId()), "camera_id", alarmTaskQueryRequest.getDeviceId()));
    }

    @Override
    public VsdTaskRelation queryVsdTaskRelationBySerialnumber(String serialnumber) {
        return baseMapper.selectOne(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber));
    }

}
