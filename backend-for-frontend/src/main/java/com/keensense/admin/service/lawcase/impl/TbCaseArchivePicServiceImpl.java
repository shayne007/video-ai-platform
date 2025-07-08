package com.keensense.admin.service.lawcase.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.lawcase.TbCaseArchivePic;
import com.keensense.admin.mapper.lawcase.TbCaseArchivePicMapper;
import com.keensense.admin.mapper.task.CameraMapper;
import com.keensense.admin.service.lawcase.ITbCaseArchivePicService;
import com.keensense.admin.util.PropertiesUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.CaseArchivePicVo;
import com.keensense.admin.vo.ResultQueryVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service("tbCaseArchivePicService")
public class TbCaseArchivePicServiceImpl extends ServiceImpl<TbCaseArchivePicMapper, TbCaseArchivePic> implements ITbCaseArchivePicService {

    @Resource
    private CameraMapper cameraMapper;

    /**
     * 案件管理图片详情
     * @param caseCode
     * @return
     */
    @Override
    public List<CaseArchivePicVo> getCaseArchiveInfoPicList(String caseCode) {
        List<CaseArchivePicVo> capList = baseMapper.selectCaseInfoArchivePicList(caseCode);
        for (CaseArchivePicVo caseArchivePic : capList) {
            String picInfo = caseArchivePic.getPicInfo();
            if(StringUtils.isNotEmpty(picInfo)){
                ResultQueryVo resultQueryVo = JSON.parseObject(picInfo, ResultQueryVo.class);
                caseArchivePic.setResultQueryVo(resultQueryVo);
            }
        }
        return capList;
    }

    /**
     * 根据案件编号查询归档图片中的监控点
     */
    @Override
    public List<CaseArchivePicVo> queryCurrentTaskCameras(String caseCode) {

        List<CaseArchivePicVo> result = baseMapper.queryCurrentPicCamerasByCaseCode(caseCode);

        String flag = PropertiesUtil.getParameterPackey("track.permit.active");
        if (StringUtils.isNotEmptyString(flag) && "true".equals(flag)) {
            List<CaseArchivePicVo> temp = new ArrayList<>();
            // 过滤监控点
            String permitMoniters = PropertiesUtil.getParameterPackey("track.permit.moniterids");
            if (CollectionUtils.isNotEmpty(result) && StringUtils.isNotEmptyString(permitMoniters)) {
                String[] ids = permitMoniters.split(",");
                List<String> list = Arrays.asList(ids);
                for (CaseArchivePicVo obj : result) {
                    String cameraId = String.valueOf(obj.getCameraId());
                    for (String str : list) {
                        if (str.equals(cameraId)) {
                            temp.add(obj);
                            break;
                        }
                    }
                }

            }
            return temp;
        }
        // 剔除相邻一致的监控点
        List<CaseArchivePicVo> date = new ArrayList<>();
        long cameraId = 0L;
        for (CaseArchivePicVo objextTrack : result) {
            long current = objextTrack.getCameraId();// 当前监控点
            if (cameraId == current) {
                continue;
            }

            CameraVo camera = cameraMapper.selectByPrimaryKey(current);
            if (camera == null) {
                continue;
            }
            if (StringUtils.isEmptyString(camera.getLongitude()) || StringUtils.isEmptyString(camera.getLatitude())) {
                continue;
            }
            objextTrack.setLongitude(camera.getLongitude());
            objextTrack.setLatitude(camera.getLatitude());
            cameraId = current;
            date.add(objextTrack);
        }

        return date;
    }

    @Override
    public Integer selectExistsArchivePic(TbCaseArchivePic caseArchivePic) {
        return baseMapper.selectExistsArchivePic(caseArchivePic);
    }
}
