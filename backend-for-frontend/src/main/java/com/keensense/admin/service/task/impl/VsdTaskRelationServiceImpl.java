package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.mapper.task.VsdTaskRelationMapper;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.util.DateTime;
import com.keensense.admin.util.IdUtils;
import com.keensense.admin.util.QueryPage;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.util.ValidateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service("vsdTaskRelationService")
public class VsdTaskRelationServiceImpl extends ServiceImpl<VsdTaskRelationMapper, VsdTaskRelation> implements IVsdTaskRelationService {

    @Resource
    VsdTaskRelationMapper vsdTaskRelationMapper;

    @Autowired
    ICameraService cameraService;

    @Override
    public VsdTaskRelation queryVsdTaskRelationBySerialnumber(String serialnumber) {
        return baseMapper.selectOne(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber));
    }

    @Override
    public List<VsdTaskRelation> queryVsdTaskRelationByCameraId(String cameras) {
        return baseMapper.selectList(new QueryWrapper<VsdTaskRelation>().in(StringUtils.isNotEmpty(cameras), "camera_id", cameras.split(",")));
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
    public VsdTaskRelation queryVsdTaskRelationByCameraFileId(String cameraFileId, String taskUserId) {
        List<VsdTaskRelation> relations = vsdTaskRelationMapper.selectList(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraFileId).eq(StringUtils.isNotEmptyString(taskUserId), "task_user_id", taskUserId));
        if (relations != null && !relations.isEmpty()) {
            VsdTaskRelation relation = relations.get(0);
            return relation;
        }
        return null;
    }

    @Override
    public IPage<VsdTaskRelation> queryVsdTaskAllService(Map<String, Object> requestParams) {
        Short status = (Short) requestParams.get("status");
        String startTime = (String) requestParams.get("startTime");
        String endTime = (String) requestParams.get("endTime");
        String taskUserId = null;
        String taskName = null;
        if (null != requestParams.get("taskName")) {
            taskName = requestParams.get("taskName").toString();
        }
        String cameraName = null;
        if (null != requestParams.get("cameraName")) {
            cameraName = requestParams.get("cameraName").toString();
        }
        if (null != requestParams.get("userId")) {
            taskUserId = requestParams.get("userId").toString();
        }
        List<Camera> cameraList = cameraService.list(new QueryWrapper<Camera>().like(StringUtils.isNotEmpty(cameraName), "name", cameraName));
        if (cameraList != null && cameraList.isEmpty()) {
            return new Page<>();
        }
        Set<Long> cameraIds = new HashSet<>();
        for (Camera camera : cameraList) {
            cameraIds.add(camera.getId());
        }
        IPage<VsdTaskRelation> page = this.page(
                new QueryPage<VsdTaskRelation>().getPage(requestParams),
                new QueryWrapper<VsdTaskRelation>()
                        .eq(status != null, "task_status", status)
                        .eq(StringUtils.isNotEmptyString(taskUserId), "task_user_id", taskUserId)
                        .ge(StringUtils.isNotEmpty(startTime), "createtime", startTime)
                        .le(StringUtils.isNotEmpty(endTime), "createtime", endTime)
                        .like(StringUtils.isNotEmptyString(taskName), "task_name", taskName)
                        .in("camera_id", cameraIds)
                        .notIn("task_status", -1).orderByDesc("createtime")
        );
        return page;
    }

    @Override
    public int countRealTask(long isvalid) {
        return baseMapper.selectCount(new QueryWrapper<VsdTaskRelation>().eq("isvalid", isvalid));
    }

    @Override
    public String selectOverlineTypeSerialNumber(Integer overlineType) {
        List<VsdTaskRelation> vsdList = baseMapper.selectList(new QueryWrapper<VsdTaskRelation>().
                eq("overline_type", overlineType));
        String serialNUmbers = "";
        if (null != vsdList && vsdList.size() > 0) {
            for (int i = 0; i < vsdList.size(); i++) {
                if (i == 0) {
                    serialNUmbers = vsdList.get(i).getSerialnumber();
                } else {
                    serialNUmbers = serialNUmbers + "," + vsdList.get(i).getSerialnumber();
                }
            }
        }
        return serialNUmbers;
    }
}
