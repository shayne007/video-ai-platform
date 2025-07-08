package com.keensense.admin.controller.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.admin.request.ResultQueryRequest;
import com.keensense.admin.service.ext.QueryAnalysisResultService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.vo.CameraVo;
import com.keensense.common.util.DateUtil;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 17:10 2019/7/11
 * @Version v0.1
 */
@Api(tags = "实时感知")
@RestController
public class RealtimePerceptionController {
    @Autowired
    private ICameraService cameraService;

    @Autowired
    private TargetSearchController targetSearchController;

    @Autowired
    private CameraController cameraController;

    @Autowired
    private QueryAnalysisResultService queryAnalysisResultService;

    @Autowired
    private ResultService resultService;
    /**
     * 查询特定地理区域范围内的监控点
     */
    @ApiOperation(value = "查询监控点分析记录")
    @PostMapping(value = "/getTargetSearchList")
    public R getTargetSearchList(String cameraId, String objtype, int page, int rows) {
        ResultQueryRequest paramBo = new ResultQueryRequest();
        paramBo.setTimeSelect("desc");
        paramBo.setCameraId(cameraId);
        paramBo.setPage(page);
        paramBo.setRows(rows);
        paramBo.setType(objtype);
        return targetSearchController.getRealtimeLargeDataList(paramBo);
    }

    /**
     * 查询监控点分析记录统计
     */
    @ApiOperation(value = "查询监控点分析记录统计")
    @PostMapping(value = "/getTargetSearchDayReport")
    public R getTargetSearchDayReport(String cameraId, String objtype) {
        R ok = R.ok();

        long current = System.currentTimeMillis();//当前时间毫秒数
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long twelve = zero + 24 * 60 * 60 * 1000 - 1;//今天23点59分59秒的毫秒数
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        map.put("objType", objtype);
        map.put("cameraId", cameraId);
        map.put("startTime", DateUtil.formatDate(new Timestamp(zero), "yyyyMMdd HH:mm:ss"));//今天零点零分零秒
        map.put("endTime", DateUtil.formatDate(new Timestamp(twelve), "yyyyMMdd HH:mm:ss"));//今天零点零分零秒
        String result = queryAnalysisResultService.groupResult(map);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String ret = jsonObject.getString("ret");
        if("0".equals(ret)) {
            JSONArray groupByDateList = jsonObject.getJSONArray("groupByDateList");
            Map<String, Long> datas = new HashMap<>();
            for (int i = 0; i < groupByDateList.size(); i++) {
                JSONObject j = groupByDateList.getJSONObject(i);
                Long time = j.getLong("groupByKey");
                String hour = DateUtil.formatDate(new Timestamp(time), "H");
                Long count = j.getLong("groupByCount");
                datas.put(hour, count);
            }
            List<Long> counts = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                if (datas.containsKey(i + "")) {
                    counts.add(datas.get(i + ""));
                } else {
                    counts.add(0L);
                }
            }
            data.put("list", counts);
            return ok.put("data", data);
        }else {
            return R.error("视图库监控点分析统计异常："+objtype);
        }

    }

    /**
     * 查询监控点分析记录统计
     */
    @ApiOperation(value = "查询监控点实时分析记录统计")
    @PostMapping(value = "/getTargetSearchCurrentReport")
    public R getTargetSearchCurrentReport(String cameraId, String objtype, Integer seconds) {
        Map<String, Object> data = new HashMap<>();
        ResultQueryRequest paramBo = new ResultQueryRequest();
        if(seconds==null||seconds==0) {
            seconds = 5;
        }
        Date date = new Date();
        Map<String,Object> map = new HashMap<>();
        map.put("sorting","desc");
        map.put("cameraId",cameraId);
        map.put("startTime",DateUtil.formatDate(new Date(date.getTime()-seconds*1000),DateUtil.FORMAT_6));
        map.put("endTime",DateUtil.formatDate(new Date(date.getTime()),DateUtil.FORMAT_6));
        map.put("objType",objtype);
        map.put("page",1);
        map.put("rows",1);
        Map<String, Object> resultMap = resultService.queryResultByJManager(map);
        Integer totalNum = (Integer) resultMap.get("totalNum");
        data.put("total", totalNum);
        return R.ok().put("data", data);
    }

    /**
     * 查询在分析中监控点树
     */
    @ApiOperation(value = "查询在分析中监控点树")
    @PostMapping(value = "getCameraAnalysisListTreeById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "enablePartial", value = "标注开启"),
            @ApiImplicitParam(name = "cameraEventType", value = "异常检测任务标志")
    })
    public R getCameraAnalysisListTreeById(String cameraEventType, Integer enablePartial) {
        return cameraController.getCameraAnalysisListTreeById(cameraEventType, enablePartial);
    }


    /**
     * 查询最新的实时监控点位
     * @param enablePartial
     * @return
     */
    @ApiOperation(value = "查询最新的实时监控点位")
    @PostMapping(value = "queryNewStartCamera")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "enablePartial", value = "标注开关"),
            @ApiImplicitParam(name = "cameraEventType", value = "异常检测任务标志")
    })
    public R queryNewStartCamera(Integer enablePartial, String cameraEventType) {
        CameraVo cameraVo = cameraService.selectNewStartCamera(enablePartial, cameraEventType);
        return R.ok().put("camera", cameraVo);
    }
}
