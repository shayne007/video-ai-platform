package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.dto.FileBo;
import com.keensense.admin.entity.task.ObjextTrack;
import com.keensense.admin.mapper.task.ObjextTrackMapper;
import com.keensense.admin.service.ext.QueryAnalysisResultService;
import com.keensense.admin.service.task.IImageQueryService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.Page;
import com.keensense.admin.vo.ResultQueryVo;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:37 2019/6/17
 * @Version v0.1
 */
@Slf4j
@Service("imageQueryService")
public class ImageQueryServiceImpl implements IImageQueryService {
    @Autowired
    private ResultService resultService;

    @Autowired
    private QueryAnalysisResultService queryAnalysisResultService;

    @Autowired
    private ObjextTrackMapper objextTrackMapper;

    @Override
    public Map<String, Object> initObjextResult(String objtype, Page<FileBo> page, String startTime, String endTime) {
        Map<String, Object> resultMap = new HashMap<>();
        ResultQueryVo paramBo = new ResultQueryVo();
        List<ResultQueryVo> resultList = new ArrayList<>();

        Map<String, Object> pamaMap = new HashMap<>();
        pamaMap.put("rows", page.getPageSize());
        pamaMap.put("page", page.getPageNo());
        pamaMap.put("sorting", "desc");
        pamaMap.put("startTime", startTime);
        pamaMap.put("endTime", endTime);
        pamaMap.put("objType", objtype);
        resultService.dealDataByJManager(pamaMap, resultMap, paramBo, resultList);
        return resultMap;
    }


    @Override
    public void resultHandle(ResultQueryVo task) {
        // 时间转换
        if (null != task.getCreatetime()) {
            task.setCreatetimeStr(DateTimeUtils.formatDate(task.getCreatetime(), DateTimeUtils.DEFAULT_FORMAT_DATE));
        }
//        // 监控点转换
//        if(StringUtils.isEmptyString(task.getCameraName())){
//            getCameraName(task);
//        }
//        // 转换人上、下半身颜色
//        setPersonColorName(task);
//        // 非机动
//        if (null != task.getObjtype() && 4 == task.getObjtype()) {
//            setBikeInfo(task);
//        }
        task.setRecogIdYMD(DateUtil.getFormat(task.getResulttime(), DateFormatConst.YMD) + task.getRecogId());
    }

    /**
     * 删除轨迹库信息
     */
    @Override
    public int deleteTract(String resultid, String objtype, String serialnumber) {
        return objextTrackMapper.delete(new QueryWrapper<ObjextTrack>().eq("resultid", resultid).eq("objtype", objtype).eq("serialnumber", serialnumber));
    }
}
