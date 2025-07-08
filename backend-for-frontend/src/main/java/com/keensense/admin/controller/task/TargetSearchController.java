package com.keensense.admin.controller.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.request.ResultQueryRequest;
import com.keensense.admin.service.task.DownloadService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.ExcelUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/targetSearch")
@Api(tags = "目标检索")
public class TargetSearchController {

    @Resource
    private ResultService resultService;

    @Resource
    private ICameraService cameraService;

    @Resource
    private DownloadService downLoadService;

    @Resource
    private IVsdTaskRelationService vsdTaskRelationService;

    /**
     * 实时任务 查询结果
     */
    @ApiOperation(value = "查看目标检索列表")
    @PostMapping(value = "/getTargetSearchList")
    public R getRealtimeLargeDataList(@RequestBody ResultQueryRequest paramBo) {
        R result = R.ok();
        Map<String, Object> map = new HashMap<>();
        int page = paramBo.getPage();
        int rows = paramBo.getRows();
        String type = paramBo.getType();
        String timeSelect = paramBo.getTimeSelect();
        if (StringUtils.isNotEmptyString(timeSelect)) {
            map.put("sorting", timeSelect);
        }
        map.put("objType", type);
        map.put("paramBo", EntityObjectConverter.getObject(paramBo, ResultQueryVo.class));
        IPage<Object> ipage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, rows);
        //前台传过来的cameraId前面拼了"c_"这个串，处理之
        String cameraId = paramBo.getCameraId();
        if (StringUtils.isNotEmptyString(cameraId)) {
            if (cameraId.endsWith(",")) {
                cameraId = cameraId.substring(0, cameraId.length() - 1);
            }
            List<String> cameraAndAreaIdList = new ArrayList<>();
            String[] cameraArr = cameraId.split(",");
            for (int j = 0; j < cameraArr.length; j++) {
                cameraAndAreaIdList.add(cameraArr[j]);
            }
            String cameraIdStr = cameraService.handleQueryCameraIds(cameraAndAreaIdList);
            if (StringUtils.isEmptyString(cameraIdStr)) {
                return result.put("page", new PageUtils(ipage));
            }
            map.put("cameraId", cameraIdStr);
        }
        String objType = paramBo.getType();
        if (StringUtils.isNotEmptyString(objType)) {
            if (null != paramBo.getOverlineType() && !paramBo.getOverlineType().equals(0)
                    && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON)) {
                String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineType());
                if (StringUtils.isNotEmptyString(overStr)) {
                    map.put("serialnumber", overStr);
                } else {
                    return result.put("page", new PageUtils(ipage));
                }
            }
            if (null != paramBo.getOverlineTypeBike() && !paramBo.getOverlineTypeBike().equals(0)
                    && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR_PERSON)) {
                String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineTypeBike());
                if (StringUtils.isNotEmptyString(overStr)) {
                    map.put("serialnumber", overStr);
                } else {
                    return result.put("page", new PageUtils(ipage));
                }
            }
            if (null != paramBo.getOverlineTypeCar() && !paramBo.getOverlineTypeCar().equals(0)
                    && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR)) {
                String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineTypeCar());
                if (StringUtils.isNotEmptyString(overStr)) {
                    map.put("serialnumber", overStr);
                } else {
                    return result.put("page", new PageUtils(ipage));
                }
            }
        }
        map.put("page", page);
        map.put("rows", rows);
        Map<String, Object> resultMap = resultService.queryResultByJManager(map);
        Integer totalNum = (Integer) resultMap.get("totalNum");
        ipage.setRecords((List<Object>) resultMap.get("resultBoList"));
        ipage.setTotal(totalNum);
        return result.put("page", new PageUtils(ipage));
    }

    @ApiOperation(value = "目标检索数据导出")
    @GetMapping(value = "/exportLargeData")
    public void exportLargeData(ResultQueryRequest paramBo, HttpServletResponse response) {
        try {
            String cameraId = paramBo.getCameraId();
            String type = paramBo.getType();
            Map<String, Object> map = new HashMap<>();
            map.put("paramBo", EntityObjectConverter.getObject(paramBo, ResultQueryVo.class));
            //前台传进来的cameraId前面拼了'c_'这个串，处理之
            if (StringUtils.isNotEmptyString(cameraId)) {
                List<String> cameraAndAreaIdList = new ArrayList<>();
                String[] cameraArr = cameraId.split(",");
                for (int j = 0; j < cameraArr.length; j++) {
                    cameraAndAreaIdList.add(cameraArr[j]);
                }
                String cameraIdStr = cameraService.handleQueryCameraIds(cameraAndAreaIdList);
                if (StringUtils.isEmptyString(cameraIdStr)) {
                    return;
                }
                map.put("cameraId", cameraIdStr);
            }
            String timeSelect = paramBo.getTimeSelect();
            if (StringUtils.isNotEmptyString(timeSelect)) {
                map.put("sorting", timeSelect);
            }
            List<ResultQueryVo> resultList = new ArrayList<>();
            int pageStart = paramBo.getPageStart();
            int pageEnd = paramBo.getPageEnd();
            int rows = 2700;
            int j = (pageEnd * 27) / 2700;
            long startTime = System.currentTimeMillis();
            if (pageEnd / 100 != pageStart / 100) {
                for (int i = j; i <= j + 1; i++) {
                    map.put("page", i);
                    map.put("rows", rows);
                    map.put("objType", type);
                    Map<String, Object> resultMap = resultService.queryResultByJManager(map);
                    List<ResultQueryVo> resultBoList = (List<ResultQueryVo>) resultMap.get("resultBoList");
                    List<ResultQueryVo> resultQueryVos = new ArrayList<>();
                    if (i == j) {
                        int start = ((pageStart - 1) * 27) % rows;
                        for (int k = 0; k < resultBoList.size(); k++) {
                            if (k >= start) {
                                resultQueryVos.add(resultBoList.get(k));
                            }
                        }
                        resultList.addAll(resultQueryVos);
                    }
                    if (i == j + 1) {
                        int end = (pageEnd * 27) % rows;
                        for (int k = 0; k < resultBoList.size(); k++) {
                            if (k < end) {
                                resultQueryVos.add(resultBoList.get(k));
                            }
                        }
                        resultList.addAll(resultQueryVos);
                    }
                }
            } else {
                map.put("page", pageStart / 100 + 1);
                map.put("rows", rows);
                map.put("objType", type);
                int start = ((pageStart - 1) * 27) % rows;
                int end = ((pageEnd) * 27) % rows;
                List<ResultQueryVo> resultQueryVos = new ArrayList<>();
                Map<String, Object> resultMap = null;
                List<ResultQueryVo> resultBoList = new ArrayList<>();
                if (start > end){
                    //查1000到10001这种特例,跨2700,先查上页尾部,再查下页头部
                    if (pageStart % 100 == 0){
                        map.put("page", pageStart / 100);
                    }else{
                        map.put("page", pageStart / 100 + 1);
                    }
                    resultMap = resultService.queryResultByJManager(map);
                    resultBoList = (List<ResultQueryVo>) resultMap.get("resultBoList");
                    for (int k = start; k < resultBoList.size(); k++) {
                        resultQueryVos.add(resultBoList.get(k));
                    }
                    map.put("page", pageStart / 100 + 1);
                    resultMap = resultService.queryResultByJManager(map);
                    resultBoList = (List<ResultQueryVo>) resultMap.get("resultBoList");
                    for (int k = 0; k < end; k++) {
                        resultQueryVos.add(resultBoList.get(k));
                    }
                }else{
                    resultMap = resultService.queryResultByJManager(map);
                    resultBoList = (List<ResultQueryVo>) resultMap.get("resultBoList");
                    if (pageStart == pageEnd) {
                        end = start + 27;
                    }
                    if (end > resultBoList.size()) {
                        end = resultBoList.size();
                    }
                    for (int k = start; k < end; k++) {
                        resultQueryVos.add(resultBoList.get(k));
                    }
                }
                resultList.addAll(resultQueryVos);
            }
            String filePath = downLoadService.downloadTotalComprehensive(resultList, RandomUtils.get8RandomValiteCode(8), paramBo.getExportImgType());
            File zipFile = new File(filePath);
            if (StringUtils.isNotEmptyString(filePath)) {
                ExcelUtil.downloadZip(zipFile, response);
            }
            log.info("结束导出" + (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("exportLargeData error---->" + e.getMessage());
        }
    }
}
