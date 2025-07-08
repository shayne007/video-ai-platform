package com.keensense.densecrowd.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.common.exception.VideoException;
import com.keensense.common.platform.TaskUtil;
import com.keensense.common.util.MasSnapshotUtil;
import com.keensense.common.util.R;
import com.keensense.common.util.SnapshotUtil;
import com.keensense.densecrowd.constant.VideoTaskConstant;
import com.keensense.densecrowd.dto.TreeNode;
import com.keensense.densecrowd.entity.task.Camera;
import com.keensense.densecrowd.entity.task.CtrlUnit;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.mapper.task.CameraMapper;
import com.keensense.densecrowd.mapper.task.CtrlUnitMapper;
import com.keensense.densecrowd.mapper.task.VsdTaskRelationMapper;
import com.keensense.densecrowd.request.CameraRequest;
import com.keensense.densecrowd.service.ext.FtpTranscodeService;
import com.keensense.densecrowd.service.ext.VideoObjextTaskService;
import com.keensense.densecrowd.service.sys.ICfgMemPropsService;
import com.keensense.densecrowd.service.task.ICameraService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.service.task.IVsdTaskService;
import com.keensense.densecrowd.util.CameraCacheConstants;
import com.keensense.densecrowd.util.CameraConstants;
import com.keensense.densecrowd.util.EntityObjectConverter;
import com.keensense.densecrowd.util.PolygonUtil;
import com.keensense.densecrowd.util.StringUtils;
import com.keensense.densecrowd.util.VsdTaskUtil;
import com.keensense.densecrowd.vo.AlarmDeviceRequest;
import com.keensense.densecrowd.vo.CameraVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:47 2019/9/26
 * @Version v0.1
 */
@Slf4j
@Service("cameraService")
public class CameraServiceImpl extends ServiceImpl<CameraMapper, Camera> implements ICameraService {

    @Autowired
    IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    FtpTranscodeService ftpTranscodeService;

    @Resource
    CtrlUnitMapper ctrlUnitMapper;

    @Autowired
    ICfgMemPropsService cfgMemPropsService;

    @Resource
    private IVsdTaskService vsdTaskService;

    @Autowired
    @Qualifier("videoObjextTaskService")
    private VideoObjextTaskService videoObjextTaskService;

    @Resource
    private VsdTaskRelationMapper vsdTaskRelationMapper;

    @Override
    public List<CameraVo> selectCameraByName(String name, String areaId, String isAnalysis) {
        List<CameraVo> cameraVoList = new ArrayList<CameraVo>();
        Map<String, String> param = new HashMap<String, String>();

        if (StringUtils.isNotEmptyString(name)) {
            param.put("name", "%" + name + "%");
        } else {
            param.put("name", null);
        }
        param.put("areaId", areaId);
        param.put("isAnalysis", isAnalysis);
        cameraVoList = baseMapper.selectCameraIsvalidList(param);
        return removal(cameraVoList);
    }

    @Override
    public List<CameraVo> selectCameraByAreaId(String areaId, String isAnalysis) {
        List<CameraVo> cameraVoList = new ArrayList<CameraVo>();
        Map<String, String> param = new HashMap<String, String>();
        param.put("name", null);
        param.put("areaId", areaId);
        param.put("isAnalysis", isAnalysis);
        cameraVoList = baseMapper.selectCameraIsvalidList(param);
        return removal(cameraVoList);
    }

    @Override
    public List<TreeNode> bulidCameraTree(List<CtrlUnit> areaList, List<CameraVo> cameraList) {
        List<TreeNode> list = new ArrayList<TreeNode>();
        for (CtrlUnit area : areaList) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(area.getUnitIdentity());
            treeNode.setName(area.getDisplayName());
            treeNode.setpId(area.getUnitParentId());
            treeNode.setUnitLevel(area.getUnitLevel().toString());
            treeNode.setNodeType(1);
            if (hasCameraByArea(area.getUnitIdentity())) {
                treeNode.setIsParent(true);
            } else {
                if (area.getIsLeaf() == 1) {
                    treeNode.setIsParent(false);
                } else {
                    treeNode.setIsParent(true);
                }
            }
            list.add(treeNode);
        }
        if (cameraList != null && cameraList.size() > 0) {
            for (CameraVo camera : cameraList) {
                TreeNode treeNode = new TreeNode();
                treeNode.setId(camera.getId().toString());
                treeNode.setName(camera.getName());
                treeNode.setpId(camera.getRegion());
                treeNode.setIsParent(false);
                treeNode.setNodeType(2);
                treeNode.setUrl(camera.getUrl());
                treeNode.setSerialnumber(camera.getSerialnumber());
                treeNode.setIsvalid(camera.getIsvalid());
                treeNode.setThumbNail(camera.getThumbNail());
                treeNode.setTaskId(camera.getSerialnumber());
                list.add(treeNode);
            }
        }
        return list;
    }

    public boolean hasCameraByArea(String areaId) {
        List<CameraVo> cameraList = selectCameraByAreaId(areaId, null);
        if (cameraList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<TreeNode> selectBulidTree(List<CtrlUnit> tlist) {
        List<TreeNode> list = new ArrayList<TreeNode>();
        for (CtrlUnit area : tlist) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(area.getUnitIdentity());
            treeNode.setName(area.getDisplayName());
            treeNode.setpId(area.getUnitParentId());
            treeNode.setUnitLevel(area.getUnitLevel().toString());
            treeNode.setNodeType(1);

            if (area.getIsLeaf() == 1) {
                List<CameraVo> cameras = selectCameraByAreaId(area.getUnitIdentity().toString(), null);
                if (cameras.size() > 0) {
                    treeNode.setIsParent(true);
                } else {
                    treeNode.setIsParent(false);
                }
            } else {
                treeNode.setIsParent(true);
            }
            list.add(treeNode);
        }
        return list;
    }

    @Override
    public List<CameraVo> selectNewStartCamera(Integer num) {
        return baseMapper.selectNewStartCamera(num);
    }

    @Override
    public String selectNameBySerialnumber(String serialnumber) {
        return baseMapper.queryNameBySerialnumber(serialnumber);
    }

    @Override
    public String getCameraSnapshotByUrl(String cameraId) {
        Camera camera = baseMapper.selectById(cameraId);
        if (camera == null) {
            throw new VideoException("获取快照失败,请检查监控点是否存在！");
        }
//        String cameraUrl = CameraConstants.transUrl(cfgMemPropsService.getWs2ServerIp(), cfgMemPropsService.getWs2ServerPort(), camera.getUrl(), "real_Platform");
//        if (StringUtils.isEmptyString(cameraUrl)) {
//            throw new VideoException("获取快照失败，请检查实时流地址是否正确！");
//        }
//        String camerasnapshot = ftpTranscodeService.getRealCameraSnapshot(cameraUrl);
        String camerasnapshot = getRealCameraUrl(camera.getUrl());
        if (StringUtils.isNotEmptyString(camerasnapshot)) {
            Camera cameraUpdate = new Camera();
            cameraUpdate.setThumbNail(camerasnapshot);
            cameraUpdate.setId(camera.getId());
            updateById(cameraUpdate);
        }
        return camerasnapshot;
    }

    //获取mas, rtsp快照
    public String getRealCameraUrl(String url) {
        String imageBase64 = "";

        if (url == null) {
            throw new VideoException("监控点URL地址");
        }
        if (url.indexOf("vas") == 0) {
            try {
//                String[] masInfos = PatternMatchUtils.simpleMatch(url, "vas://name=([^&]*)&psw=([^&]*)&srvip=([^&]*)&srvport=([^&]*)&devid=([^&]*)&");
                String[] masInfos = new String[6];
                if (masInfos == null || org.apache.commons.lang3.StringUtils.isEmpty(masInfos[3]) || org.apache.commons.lang3.StringUtils.isEmpty(masInfos[4]) || StringUtils.isEmpty(masInfos[5])) {
                    throw new VideoException("vas点位格式错误");
                }
                String masMasterIp = cfgMemPropsService.getWs2ServerIp();
                String masMasterPort = cfgMemPropsService.getWs2ServerPort();
                String channelId = masInfos[5];

                imageBase64 = MasSnapshotUtil.getMasSnapshotBase64(masMasterIp, masMasterPort, channelId);
            } catch (Exception e) {
                log.error("获取流异常:" + e);
                throw new VideoException("获取图片流异常");
            }

        } else {
            imageBase64 = SnapshotUtil.catSnapshotBase64(url);
        }
        String imageUrl = videoObjextTaskService.saveImageToFdfs(imageBase64);
        return imageUrl;
    }

    @Override
    public IPage<Camera> selectCameraList(AlarmDeviceRequest deviceRequest) {
        String deviceId = deviceRequest.getDeviceId();
        String areaId = deviceRequest.getAreaId();
        int currentPage = deviceRequest.getCurrentPage();
        int pageSize = deviceRequest.getPageSize();
        Page<Camera> pages = new Page<>(currentPage, pageSize);
        IPage<Camera> page = baseMapper.selectPage(pages, new QueryWrapper<Camera>().eq(StringUtils.isNotEmpty(deviceId), "id", deviceId));
        return page;

    }

    @Override
    public List<CameraVo> selectCameraAll(String status, List<Point2D.Double> polygonList) {
        List<CameraVo> data = baseMapper.selectCameraByCameraType();
        List<CameraVo> resultData = new ArrayList<CameraVo>();
        if (data != null && data.size() > 0) {
            for (CameraVo camera : data) {
                String lat = camera.getLatitude();
                String lon = camera.getLongitude();
                if (StringUtils.isNotEmptyString(lon) && StringUtils.isNotEmptyString(lat)) {
                    Point2D.Double point = PolygonUtil.pareseDouble(lon, lat);
                    boolean isContains = PolygonUtil.checkWithJdkGeneralPath(point, polygonList);
                    if (isContains) {
                        resultData.add(camera);
                    }
                }
            }
        }
        //状态 [0:离线，1:在线]
        if (StringUtils.isEmptyString(status)) {
            return resultData;
        }
        List<CameraVo> finalResultData = new ArrayList<>();
        if (resultData != null && !resultData.isEmpty()) {
            for (CameraVo cameraVo : resultData) {
                if (status.equals(cameraVo.getStatus().toString())) {
                    finalResultData.add(cameraVo);
                }
            }
        }
        return finalResultData;
    }

    @Override
    public List<Camera> selectCameraByArea(String id) {
        int[] cameratype = {1, 2};
        List<Camera> mList = baseMapper.selectList(new QueryWrapper<Camera>().eq("region", id).in("cameratype", cameratype));
        List<CtrlUnit> cList = ctrlUnitMapper.selectList(new QueryWrapper<CtrlUnit>().eq("unit_parent_id", id));
        if (cList != null && !cList.isEmpty()) {
            for (CtrlUnit ctrlUnit : cList) {
                List<Camera> mList_1 = this.selectCameraByArea(ctrlUnit.getUnitIdentity());
                mList.addAll(mList_1);
            }
        }
        return mList;
    }

    @Override
    public Page<CameraVo> selectOnlineAndIpCCameraListByPage(Page<CameraVo> pages, Map<String, Object> map) {
        List<CameraVo> records = baseMapper.selectOnlineAndIpCCameraList(pages, map);
        if (records != null && !records.isEmpty()) {
            for (CameraVo record : records) {
                CtrlUnit ctrlUnit = ctrlUnitMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_identity", record.getRegion()));
                if (ctrlUnit != null) {
                    record.setRegionName(ctrlUnit.getUnitName());
                }
            }
        }
        pages.setRecords(records);
        return pages;
    }

    @Override
    public List<Camera> selectCameraByDeviceId(String deviceId) {
        return baseMapper.selectList(new QueryWrapper<Camera>().eq("id", deviceId));
    }

    @Override
    public List<Camera> selectTumbNailCameraList() {
        return baseMapper.selectList(new QueryWrapper<Camera>().isNull("thumb_nail"));
    }

    public List<CameraVo> removal(List<CameraVo> mList) {
        List<CameraVo> aList = new ArrayList<>();
        Set<Long> sets = new HashSet<>();
        for (CameraVo cameraVo : mList) {
            //如果可以加进去，说明没有重复
            if (sets.add(cameraVo.getId())) {
                sets.add(cameraVo.getId());
                aList.add(cameraVo);
            }
        }
        return aList;
    }

    @Override
    public R updateCamera(CameraRequest cameraRequest) {
        Long cameraId = cameraRequest.getId();
        if (null == cameraId) {
            return R.error("监控点Id不能为空");
        }
        Camera dataCamera = baseMapper.selectById(cameraId);
        if (dataCamera != null) {
            cameraRequest.setId(Long.valueOf(cameraId));
            int updateCount = baseMapper.updateById(EntityObjectConverter.getObject(cameraRequest, Camera.class));
            if (updateCount > 0) {
                CameraCacheConstants.cleanCameraCacheByCameraType(String.valueOf(dataCamera.getCameratype()));
                VsdTaskUtil.clearTaskName();
                List<VsdTaskRelation> relationList = vsdTaskRelationMapper.selectList(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraId).in("from_type", VideoTaskConstant.TASK_TYPE.REAL, VideoTaskConstant.TASK_TYPE.IPC, VideoTaskConstant.TASK_TYPE.CAPTURE));
                for (VsdTaskRelation vsdTaskRelation : relationList) {
                    //更新完后停止任务
                    vsdTaskService.stopRealtimeTask(vsdTaskRelation.getSerialnumber());
                }
            }
        }
        return R.ok();
    }

    @Override
    public void deleteCameraAndTaskById(String cameraId) {
        //先删任务再删点位
        Camera camera = baseMapper.selectById(cameraId);
        if (camera == null) {
            return;
        }
        Long cameratype = camera.getCameratype();
        if (cameratype == null) {
            return;
        }
        if (cameratype == CameraConstants.CameraType.CAPTURE) {
            return;
        }
        List<VsdTaskRelation> taskRelations = vsdTaskRelationMapper.selectList(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraId));
        if (taskRelations.size() > 0) {
            for (VsdTaskRelation relation : taskRelations) {
                String serialnumber = relation.getSerialnumber();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("serialnumber", serialnumber);
//                videoObjextTaskService.deleteVsdTaskService(paramMap);
            }
            vsdTaskRelationMapper.delete(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraId));
        }
        baseMapper.deleteById(cameraId);
        // 删除监控点，清除缓存
        CameraCacheConstants.cleanCameraCacheByCameraType(String.valueOf(camera.getCameratype()));
    }
}
