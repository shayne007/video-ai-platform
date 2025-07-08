package com.keensense.admin.controller.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.dto.TreeNode;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.CtrlUnit;
import com.keensense.admin.request.ResultQueryRequest;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.ICtrlUnitService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.admin.vo.WarningVo;
import com.keensense.common.util.DateUtil;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.*;
import java.sql.*;
import java.util.*;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:24 2019/10/14
 * @Version v0.1
 */
@Slf4j
@RestController
@RequestMapping("/warning")
@Api(tags = "异常告警")
public class WarningOfAbnormalBehaviorController {

    @Resource
    private ICtrlUnitService ctrlUnitService;

    @Resource
    private ICameraService cameraService;

    @Resource
    private ResultService resultService;

    @Resource
    private IVsdTaskRelationService vsdTaskRelationService;

    /**
     * 取得告警数据
     *
     * @return
     */
    @ApiOperation("获取告警数据")
    @PostMapping(value = "/getWarningDate")
    public R getWarningDate(Integer warningType) {
        R result = R.ok();

        long current = System.currentTimeMillis();//当前时间毫秒数
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long twelve = zero + 24 * 60 * 60 * 1000 - 1;//今天23点59分59秒的毫秒数
        Map<String, Object> map = new HashMap<>();
        int page = 1;
        int rows = 4;
        ResultQueryRequest paramBo = new ResultQueryRequest();
        if (warningType == null) {
            warningType = 1;
        }
        if (warningType == 1) {
            paramBo.setHasKnife(1);
        } else {
            paramBo.setRespirator("0");
        }
        map.put("sorting", "desc");
        map.put("objType", CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON);
        map.put("paramBo", EntityObjectConverter.getObject(paramBo, ResultQueryVo.class));
//        map.put("startTime", DateUtil.formatDate(new Timestamp(zero), "yyyyMMdd HH:mm:ss"));//今天零点零分零秒
//        map.put("endTime", DateUtil.formatDate(new Timestamp(twelve), "yyyyMMdd HH:mm:ss"));//今天零点零分零秒
        map.put("page", page);
        map.put("rows", rows);
        Map<String, Object> resultMapKnife = resultService.queryResultByJManager(map);
        List<ResultQueryVo> resultBoList = (List<ResultQueryVo>) resultMapKnife.get("resultBoList");
        List<WarningVo> warningVoList = new ArrayList<WarningVo>();
        for (ResultQueryVo resultQueryVo : resultBoList) {
            WarningVo warningVo = new WarningVo();
            warningVo.setId(resultQueryVo.getId());
            warningVo.setCameraName(resultQueryVo.getCameraName());
            warningVo.setPicUrl(resultQueryVo.getImgurl());
            warningVo.setTime(resultQueryVo.getCreatetimeStr());
            warningVo.setWarningType(warningType);
            warningVoList.add(warningVo);
        }

        int totalNum = 0;
        int knifeNum = Integer.valueOf(resultMapKnife.get("totalNum").toString());
        totalNum = knifeNum;
        result.put("resultList", warningVoList);
        result.put("knifeNum", knifeNum);
        result.put("totalNum", totalNum);
        return result;
    }

    /**
     * 取得告警数据
     *
     * @return
     */
    @ApiOperation("获取入侵、跨界告警数据")
    @PostMapping(value = "/getOverlineTypeDate")
    public R getOverlineTypeDate() {
        R result = R.ok();
        long current = System.currentTimeMillis();//当前时间毫秒数
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long twelve = zero + 24 * 60 * 60 * 1000 - 1;//今天23点59分59秒的毫秒数
        int page = 1;
        int rows = 4;
        int intrusionNum = 0;
        int transboundaryNum = 0;
        List<WarningVo> warningVoList = new ArrayList<WarningVo>();
        String intrusionSerialNumbers = vsdTaskRelationService.selectOverlineTypeSerialNumber(1);
        String transboundaryserialNumbers = vsdTaskRelationService.selectOverlineTypeSerialNumber(2);
        ResultQueryRequest paramBo = new ResultQueryRequest();

        String[] intrusionArray = intrusionSerialNumbers.split(",");
        String[] transboundaryArray = transboundaryserialNumbers.split(",");

        List<String> intrusionList = new ArrayList<>(Arrays.asList(intrusionArray));
        List<String> transboundaryList = new ArrayList<>(Arrays.asList(transboundaryArray));

        if (StringUtils.isNotEmptyString(intrusionSerialNumbers)) {
            Map<String, Object> intrusionMap = new HashMap<>();
            intrusionMap.put("sorting", "desc");
            intrusionMap.put("paramBo", EntityObjectConverter.getObject(paramBo, ResultQueryVo.class));
            //intrusionMap.put("objType", CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON);
//            intrusionMap.put("startTime", DateUtil.formatDate(new Timestamp(zero), "yyyyMMdd HH:mm:ss"));//今天零点零分零秒
//            intrusionMap.put("endTime", DateUtil.formatDate(new Timestamp(twelve), "yyyyMMdd HH:mm:ss"));//今天零点零分零秒
            intrusionMap.put("page", page);
            intrusionMap.put("rows", rows);
            intrusionMap.put("serialnumber", intrusionSerialNumbers);
            Map<String, Object> resultMapIntrusion = resultService.queryResultByJManager(intrusionMap);
            intrusionNum = Integer.valueOf(resultMapIntrusion.get("totalNum") + "");
        }

        if (StringUtils.isNotEmptyString(transboundaryserialNumbers)) {
            Map<String, Object> transboundaryMap = new HashMap<>();
            transboundaryMap.put("sorting", "desc");
            transboundaryMap.put("paramBo", EntityObjectConverter.getObject(paramBo, ResultQueryVo.class));
            //transboundaryMap.put("objType", CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON);
//            transboundaryMap.put("startTime", DateUtil.formatDate(new Timestamp(zero), "yyyyMMdd HH:mm:ss"));//今天零点零分零秒
//            transboundaryMap.put("endTime", DateUtil.formatDate(new Timestamp(twelve), "yyyyMMdd HH:mm:ss"));//今天零点零分零秒
            transboundaryMap.put("page", page);
            transboundaryMap.put("rows", rows);
            transboundaryMap.put("serialnumber", transboundaryserialNumbers);
            Map<String, Object> resultMapTransboundary = resultService.queryResultByJManager(transboundaryMap);
            transboundaryNum = Integer.valueOf(resultMapTransboundary.get("totalNum") + "");
        }

        if (StringUtils.isNotEmptyString(intrusionSerialNumbers) ||
                StringUtils.isNotEmptyString(transboundaryserialNumbers)) {
            Map<String, Object> totalMap = new HashMap<>();
            totalMap.put("sorting", "desc");
            totalMap.put("paramBo", EntityObjectConverter.getObject(paramBo, ResultQueryVo.class));
            //totalMap.put("objType", CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON);
//            totalMap.put("startTime", DateUtil.formatDate(new Timestamp(zero), "yyyyMMdd HH:mm:ss"));//今天零点零分零秒
//            totalMap.put("endTime", DateUtil.formatDate(new Timestamp(twelve), "yyyyMMdd HH:mm:ss"));//今天零点零分零秒
            totalMap.put("page", page);
            totalMap.put("rows", rows);
            if (StringUtils.isNotEmptyString(intrusionSerialNumbers) &&
                    StringUtils.isNotEmptyString(transboundaryserialNumbers)) {
                totalMap.put("serialnumber", transboundaryserialNumbers + "," + intrusionSerialNumbers);
            } else if (StringUtils.isNotEmptyString(intrusionSerialNumbers) &&
                    StringUtils.isEmptyString(transboundaryserialNumbers)) {
                totalMap.put("serialnumber", intrusionSerialNumbers);
            } else if (StringUtils.isEmptyString(intrusionSerialNumbers) &&
                    StringUtils.isNotEmptyString(transboundaryserialNumbers)) {
                totalMap.put("serialnumber", transboundaryserialNumbers);
            }

            Map<String, Object> resultMapTotal = resultService.queryResultByJManager(totalMap);
            List<ResultQueryVo> resultBoList = (List<ResultQueryVo>) resultMapTotal.get("resultBoList");
            for (ResultQueryVo resultQueryVo : resultBoList) {
                WarningVo warningVo = new WarningVo();
                warningVo.setCameraName(resultQueryVo.getCameraName());
                warningVo.setPicUrl(resultQueryVo.getImgurl());
                warningVo.setTime(resultQueryVo.getCreatetimeStr());
                if (intrusionList.contains(resultQueryVo.getSerialnumber())) {
                    warningVo.setWarningType(2);
                }
                if (transboundaryList.contains(resultQueryVo.getSerialnumber())) {
                    warningVo.setWarningType(3);
                }
                warningVoList.add(warningVo);
            }
        }
        int totalNum = intrusionNum + transboundaryNum;
        result.put("intrusionNum", intrusionNum);
        result.put("transboundaryNum", transboundaryNum);
        result.put("resultList", warningVoList);
        result.put("totalNum", totalNum);
        return result;
    }

}
