package com.keensense.admin.controller.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.FeaturesConstant;
import com.keensense.admin.constants.RgbConstants;
import com.keensense.admin.constants.VehicleClassConstants;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.entity.task.BikeModel;
import com.keensense.admin.entity.task.BrandModel;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.mqtt.config.EnumerationConfig;
import com.keensense.admin.request.ResultQueryRequest;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.task.DownloadService;
import com.keensense.admin.service.task.IBrandModelService;
import com.keensense.admin.service.task.IHumanColorModelService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.util.EhcacheUtils;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.ExcelUtil;
import com.keensense.admin.util.Page;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.util.ValidateHelper;
import com.keensense.admin.vo.CarBrandVo;
import com.keensense.admin.vo.ColorVo;
import com.keensense.admin.vo.CondVo;
import com.keensense.admin.vo.KeyValueVo;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.datastruct.WeekArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大数据查询
 */
@Slf4j
@RestController
@RequestMapping("/resultQuery")
@Api(tags = "查询-大数据查询")
public class ResultQueryContoller {

    @Resource
    private ResultService resultService;

    @Resource
    private DownloadService downLoadService;

    @Resource
    private VideoObjextTaskService videoObjextTaskService;

    @Resource
    private IBrandModelService brandModelService;

    @Resource
    private IHumanColorModelService humanColorModelService;

    @Autowired
    private IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    private EnumerationConfig enumerationConfig;

    /**
     * 实时任务 查询结果
     */
    @ApiOperation(value = "实时任务 查询结果")
    @PostMapping(value = "/getRealtimeLargeDataList")
    public R getRealtimeLargeDataList(@RequestBody ResultQueryRequest paramBo) {
        IPage<Object> ipage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(paramBo.getPage(), paramBo.getRows());
        R result = R.ok();
        Map<String, Object> map = new HashMap<>();
        // 人/人骑车/车
        map.put("type", paramBo.getType() == null ? "" : paramBo.getType());
        map.put("paramBo", EntityObjectConverter.getObject(paramBo, ResultQueryVo.class));
        map.put("page", paramBo.getPage());
        map.put("rows", paramBo.getRows());
        String objType = paramBo.getType();
        if (StringUtils.isNotEmptyString(objType)) {
            String serialnumber = "";
            if (null != paramBo.getOverlineType() && !paramBo.getOverlineType().equals(0)
                    && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON)) {
                String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineType());
                serialnumber = getOverLineSerialnumber(overStr, paramBo);
                if (StringUtils.isNotEmptyString(serialnumber)) {
                    map.put("serialnumber", serialnumber);
                } else {
                    return result.put("page", new PageUtils(ipage));
                }
            }
            if (null != paramBo.getOverlineTypeBike() && !paramBo.getOverlineTypeBike().equals(0)
                    && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR_PERSON)) {
                String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineTypeBike());
                serialnumber = getOverLineSerialnumber(overStr, paramBo);
                if (StringUtils.isNotEmptyString(serialnumber)) {
                    map.put("serialnumber", serialnumber);
                } else {
                    return result.put("page", new PageUtils(ipage));
                }
            }
            if (null != paramBo.getOverlineTypeCar() && !paramBo.getOverlineTypeCar().equals(0)
                    && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR)) {
                String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineTypeCar());
                serialnumber = getOverLineSerialnumber(overStr, paramBo);
                if (StringUtils.isNotEmptyString(serialnumber)) {
                    map.put("serialnumber", serialnumber);
                } else {
                    return result.put("page", new PageUtils(ipage));
                }
            }
        }
        Page<ResultQueryVo> pages = new Page<>(paramBo.getPage(), paramBo.getRows());
        Map<String, Object> resultMap = resultService.getRealtimeDataByExt(pages, map);
        Integer totalNum = (Integer) resultMap.get("totalNum");
        ipage.setRecords((List<Object>) resultMap.get("resultBoList"));
        ipage.setTotal(totalNum);
        return result.put("page", new PageUtils(ipage));
    }

    /**
     * 根据入侵跨界类型查询任务号
     *
     * @param overStr
     * @param paramBo
     * @return
     */
    public String getOverLineSerialnumber(String overStr, ResultQueryRequest paramBo) {
        String[] overStrArray = overStr.split(",");
        List<String> overList = Arrays.asList(overStrArray);
        String serialnumber = "";
        if (overList.contains(paramBo.getSerialnumber())) {
            serialnumber = paramBo.getSerialnumber();
        }
        return serialnumber;
    }

    /**
     * 查询实时任务是否可用
     */
    @ApiOperation(value = "查询实时任务是否可用")
    @PostMapping(value = "/getRealTaskStatusById")
    @ApiImplicitParam(name = "taskId", value = "任务Id")
    public R getRealTaskStatusById(String taskId) {
        R result = R.ok();
        VsdTaskRelation vsdTaskRelation = vsdTaskRelationService.getOne(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", taskId));
        if (vsdTaskRelation != null && vsdTaskRelation.getFromType() == VideoTaskConstant.TASK_TYPE.GATE) {//卡口任务单独走vsd_task_relation
            result.put("taskStatus", vsdTaskRelation.getIsvalid());
            return result;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("serialnumber", taskId);
        String resultJson = videoObjextTaskService.queryVsdTaskAllService(paramMap);
        Var resultVar = Var.fromJson(resultJson);
        if (resultVar == null) {
            return R.error("未返回数据");
        }
        if ("0".equals(resultVar.getString("ret"))) {
            WeekArray array = resultVar.getArray("tasks");
            if (array != null && array.getSize() > 0) {
                Var task = array.get(0 + "");
                result.put("taskStatus", task.getString("status"));
            } else {
                return R.error("任务不存在");
            }
        } else {
            return R.error();
        }
        return result;
    }

    /**
     * 数据导出 下载 离线 实时
     */
    @ApiOperation(value = "数据导出 下载 离线 实时")
    @GetMapping(value = "/exportOfflineLargeData")
    public void exportofflineLargeData(ResultQueryRequest paramBo, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", paramBo.getType() == null ? "" : paramBo.getType());
        map.put("paramBo", EntityObjectConverter.getObject(paramBo, ResultQueryVo.class));
        String monitorGroupId = paramBo.getMonitorGroupId();
        if (StringUtils.isNotEmptyString(monitorGroupId)) {
            map.put("monitorGroupId", paramBo.getMonitorGroupId());
        } else {
            map.put("cameraId", paramBo.getCameraId());
        }
        String taskTypeCode = paramBo.getTaskTypeCode();
        List<ResultQueryVo> resultList = new ArrayList<>();
        int pageStart = paramBo.getPageStart();
        int pageEnd = paramBo.getPageEnd();
        int rows = 2700;
        int j = (pageEnd * 27) / 2700;
        if (pageEnd / 100 != pageStart / 100) {
            for (int i = j; i <= j + 1; i++) {
                Page<ResultQueryVo> pages = new Page<>(i, rows);
                Map<String, Object> resultBoMap = null;
                if (String.valueOf(VideoTaskConstant.FROM_TYPE.OFFLINE).equals(taskTypeCode)) {
                    resultBoMap = resultService.getOfflinetDataByExt(pages, map);
                } else if (String.valueOf(VideoTaskConstant.FROM_TYPE.REAL).equals(taskTypeCode)) {
                    resultBoMap = resultService.getRealtimeDataByExt(pages, map);
                } else {
                    resultBoMap = resultService.getMonRealtimeDataByExt(pages, map);
                }
                List<ResultQueryVo> resultBoList = (List<ResultQueryVo>) resultBoMap.get("resultBoList");
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
            int start = ((pageStart - 1) * 27) % rows;
            int end = ((pageEnd) * 27) % rows;
            List<ResultQueryVo> resultQueryVos = new ArrayList<>();
            List<ResultQueryVo> resultBoList = new ArrayList<>();
            if (start > end) {
                //查1000到10001这种特例,跨2700,先查上页尾部,再查下页头部
                Page<ResultQueryVo> pages = null;
                if (pageStart % 100 == 0) {
                    pages = new Page<>(pageStart / 100, rows);
                } else {
                    pages = new Page<>(pageStart / 100 + 1, rows);
                }
                resultBoList = getResultBoList(taskTypeCode, pages, map);
                for (int k = start; k < resultBoList.size(); k++) {
                    resultQueryVos.add(resultBoList.get(k));
                }
                pages = new Page<>(pageStart / 100 + 1, rows);
                resultBoList = getResultBoList(taskTypeCode, pages, map);
                for (int k = 0; k < end; k++) {
                    resultQueryVos.add(resultBoList.get(k));
                }

            } else {
                Page<ResultQueryVo> pages = new Page<>(pageStart / 100 + 1, rows);
                resultBoList = getResultBoList(taskTypeCode, pages, map);
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
        String filePath = downLoadService.downloadTotalComprehensive(resultList, paramBo.getSerialnumber(), paramBo.getExportImgType());
        File zipFile = new File(filePath);
        if (StringUtils.isNotEmptyString(filePath)) {
            ExcelUtil.downloadZip(zipFile, response);
        }
    }

    private List<ResultQueryVo> getResultBoList(String taskTypeCode, Page<ResultQueryVo> pages, Map<String, Object> map) {
        Map<String, Object> resultBoMap = null;
        if (String.valueOf(VideoTaskConstant.FROM_TYPE.OFFLINE).equals(taskTypeCode)) {
            resultBoMap = resultService.getOfflinetDataByExt(pages, map);
        } else if (String.valueOf(VideoTaskConstant.FROM_TYPE.REAL).equals(taskTypeCode)) {
            resultBoMap = resultService.getRealtimeDataByExt(pages, map);
        } else {
            resultBoMap = resultService.getMonRealtimeDataByExt(pages, map);
        }
        List<ResultQueryVo> resultBoList = (List<ResultQueryVo>) resultBoMap.get("resultBoList");
        return resultBoList;
    }


    /**
     * 离线结构化结果查询
     *
     * @return
     */
    @ApiOperation("离线结构化结果查询")
    @PostMapping(value = "/getOfflineLargeDataList")
    public R getOfflineLargeDataList(@RequestBody ResultQueryRequest paramBo) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Object> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(paramBo.getPage(), paramBo.getRows());
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("type", paramBo.getType() == null ? "" : paramBo.getType());
        map.put("paramBo", EntityObjectConverter.getObject(paramBo, ResultQueryVo.class));
        map.put("page", paramBo.getPage());
        map.put("rows", paramBo.getRows());
        Page<ResultQueryVo> pages = new Page<>(paramBo.getPage(), paramBo.getRows());
        String serialnumber = "";

        String objType = paramBo.getType();
        if (StringUtils.isNotEmptyString(objType)) {
            if (null != paramBo.getOverlineType() && !paramBo.getOverlineType().equals(0)
                    && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON)) {
                String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineType());
                serialnumber = getOverLineSerialnumber(overStr, paramBo);
                if (StringUtils.isNotEmptyString(serialnumber)) {
                    map.put("serialnumber", serialnumber);
                } else {
                    return R.ok().put("page", new PageUtils(page));
                }
            }
            if (null != paramBo.getOverlineTypeBike() && !paramBo.getOverlineTypeBike().equals(0)
                    && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR_PERSON)) {
                String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineTypeBike());
                serialnumber = getOverLineSerialnumber(overStr, paramBo);
                if (StringUtils.isNotEmptyString(serialnumber)) {
                    map.put("serialnumber", serialnumber);
                } else {
                    return R.ok().put("page", new PageUtils(page));
                }
            }
            if (null != paramBo.getOverlineTypeCar() && !paramBo.getOverlineTypeCar().equals(0)
                    && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR)) {
                String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineTypeCar());
                serialnumber = getOverLineSerialnumber(overStr, paramBo);
                if (StringUtils.isNotEmptyString(serialnumber)) {
                    map.put("serialnumber", serialnumber);
                } else {
                    return R.ok().put("page", new PageUtils(page));
                }
            }
        }

        VsdTaskRelation vsdTaskRelation = vsdTaskRelationService.queryVsdTaskRelationBySerialnumber(serialnumber);
        Map<String, Object> resultBoMap = resultService.getOfflinetDataByExt(pages, map);
        if (null == resultBoMap) {
            result.put("taskProgress", 100);// 总记录数
            page.setTotal(0);
            page.setRecords(new ArrayList<Object>());
        } else {
            int totalRecords = (int) resultBoMap.get("totalNum");
            List<Object> resultBoList = (List) resultBoMap.get("resultBoList");
            page.setTotal(totalRecords);
            page.setRecords(resultBoList);
            if (vsdTaskRelation != null) {
                // 进度
                result.put("taskProgress", vsdTaskRelation.getTaskProgress());
            } else {
                Map<String, Object> requestParams = new HashMap<>();
                requestParams.put("serialnumber", serialnumber);
                requestParams.put("type", VideoTaskConstant.Type.OBJEXT);
                String taskListReponse = videoObjextTaskService.queryVsdTaskService(requestParams);
                JSONObject json = JSON.parseObject(taskListReponse);
                JSONArray tasksArray = json.getJSONArray("tasks");
                String tastJson = String.valueOf(tasksArray.get(0));
                JSONObject jsonObject = JSON.parseObject(tastJson);
                Integer progress = Integer.parseInt(jsonObject.getString("progress"));
                // 进度
                result.put("taskProgress", progress);
            }
        }
        return R.ok().put("page", new PageUtils(page));
    }

    @ApiOperation("查询车系")
    @PostMapping(value = "queryVehicleModel")
    public R queryVehicleModel(String vehicleBrand) {
        R result = R.ok();
        if (null == EhcacheUtils.getItem("vehiclemodel")) {
            //初始化车系放入缓存
            String vehicleModel = enumerationConfig.getVehicleModel();
            if (StringUtils.isNotEmptyString(vehicleModel)) {
                vehicleModelEhcache(vehicleModel);
            }
        }
        Map<String, List<String>> vehicleModelMap = (Map<String, List<String>>) EhcacheUtils.getItem("vehiclemodel");
        result.put("vehicleModelList", vehicleModelMap.get(vehicleBrand));
        return result;
    }

    /**
     * 车系放入缓存
     */
    public void vehicleModelEhcache(String str) {
        String[] strArray = str.split("/");
        List<String> vehicleModeList = new ArrayList<>();
        Map<String, List<String>> vehicleModelMap = new HashMap<>();
        for (int i = 0; i + 1 < strArray.length; i += 2) {
            if (i == 0) {
                vehicleModeList.add(strArray[i + 1]);
            } else if (strArray[i].equals(strArray[i - 2])) {
                vehicleModeList.add(strArray[i + 1]);
            } else if (!strArray[i].equals(strArray[i - 2])) {
                vehicleModelMap.put(strArray[i - 2], vehicleModeList);
                vehicleModeList = new ArrayList<>();
                vehicleModeList.add(strArray[i + 1]);
            } else if (i == strArray.length - 2) {
                vehicleModeList.add(strArray[i + 1]);
                vehicleModelMap.put(strArray[i], vehicleModeList);
            }
        }
        EhcacheUtils.putItem("vehiclemodel", vehicleModelMap);
    }

    @ApiOperation("高级查询条件初始化")
    @PostMapping(value = "initialize")
    public R realtimeTaskDetailCommon() {
        R result = R.ok();
        // 初始化所有车系
        List<String> seriesList = brandModelService.selectAllCarBrand();
        //初始化所有品牌
        List<KeyValueVo> brandList = initCarBrandList();
        // 初始化人颜色
        List<ColorVo> humanColorList = initColorListByType(ColorVo.HUMAN_COLOR_TYPE);
        // 初始化非机动车颜色
        List<ColorVo> bikeColorList = initColorListByType(ColorVo.BIKE_COLOR_TYPE);
        // 初始化车辆颜色
        List<ColorVo> carColorList = initColorListByType(ColorVo.CAR_COLOR_TYPE);
        // 车辆类型初始化
        List<CarBrandVo> carKindList = getCarKindList();
        // 车系初始化
        List<CondVo> carSeriesList = initVlrpSerires();
        // 非机动车类型
        List<BikeModel> bikeKindList = initBikeKindList();
        //车辆行驶方向初始化
        List<KeyValueVo> carDirectionList = initCarDirectionList();
        //初始化人的发型
        List<KeyValueVo> hairStyleList = initHairStyleList();
        //初始化车牌类型
        List<KeyValueVo> palteClassList = initPlateClassList();
        result.put("bikeKindList", bikeKindList);//类型
        result.put("carlogoList", brandList);//品牌
        result.put("seriesList", seriesList);//车系
        result.put("humanColorList", humanColorList);//上下衣
        result.put("bikeColorList", bikeColorList);//上衣头盔颜色
        result.put("carColorList", carColorList);//车身颜色
        result.put("carKindList", carKindList);//车型
        result.put("carSeriesList", carSeriesList);//车系
        result.put("carDirectionList", carDirectionList);//行驶方向
        result.put("hairStyleList", hairStyleList);//发型
        result.put("vehiclePlateClassList", palteClassList);//车牌类型
        result.put("socialAttributeList", initSocialAttributeList());
        result.put("enterpriseList", initEnterpriseList());
        return result;
    }

    /**
     * 根据类型获取查询初始化颜色数据
     *
     * @param type 类型
     * @return
     */
    private List<ColorVo> initColorListByType(String type) {
        List<ColorVo> colorList = new ArrayList<>();
        if (StringUtils.isEmptyString(type)) {
            return colorList;
        }
        colorList = humanColorModelService.getColorModelList(type);
        List<ColorVo> newColorList = new ArrayList<>();
        List<String> colorNameList = new ArrayList<>();

        if (ValidateHelper.isNotEmptyList(colorList)) {

            for (ColorVo colorVo : colorList) {

                String colorName = colorVo.getColorName();
                Integer colorBgrTag = colorVo.getColorBgrTag();
                if (!colorNameList.contains(colorName)) {
                    colorVo.setColorBgrTagIds(String.valueOf(colorBgrTag));
                    newColorList.add(colorVo);
                    colorNameList.add(colorVo.getColorName());
                } else {

                    for (int i = 0; i < newColorList.size(); i++) {
                        ColorVo cm = newColorList.get(i);
                        if (cm.getColorName().equals(colorName)) {
                            String colorBgrTagIds = String.valueOf(colorBgrTag) + "," + cm.getColorBgrTagIds();
                            cm.setColorBgrTagIds(colorBgrTagIds);
                        }
                    }
                }
            }
        }
        return newColorList;
    }

    public List<CarBrandVo> getCarKindList() {
        List<CarBrandVo> carBrandList = new ArrayList<>();
        Map<String, String> carKindType = VehicleClassConstants.CARTYPE_CODEFORMAT;
        for (Map.Entry<String, String> entry : carKindType.entrySet()) {
            CarBrandVo carBrandVo = new CarBrandVo();
            carBrandVo.setCarKindId(entry.getKey());
            carBrandVo.setCarKindName(entry.getValue());
            carBrandList.add(carBrandVo);
        }
        return carBrandList;
    }

    // 初始化车辆系列
    private List<CondVo> initVlrpSerires() {
        List<CondVo> carSeriesList = new ArrayList<>();
        List<BrandModel> carKindTempList = brandModelService.selectAllCarSeries();
        if (ValidateHelper.isNotEmptyList(carKindTempList)) {
            for (BrandModel carBrandModel : carKindTempList) {
                CondVo condVo = new CondVo(String.valueOf(carBrandModel.getCarSeries()), carBrandModel.getCarSeries());
                carSeriesList.add(condVo);
            }
        }
        return carSeriesList;
    }

    private List<BikeModel> initBikeKindList() {
        List<BikeModel> bikeModelList = new ArrayList<>();
        Map<Integer, String> bikeModelMap = FeaturesConstant.getBikeGenre();
        for (Map.Entry<Integer, String> entry : bikeModelMap.entrySet()) {
            BikeModel bikeModel = new BikeModel();
            bikeModel.setKindId(entry.getKey());
            bikeModel.setKindName(entry.getValue());
            bikeModelList.add(bikeModel);
        }
        return bikeModelList;
    }

    private List<KeyValueVo> initCarDirectionList() {
        Map<String, String> carDirectionType = RgbConstants.CARDIRECTION_CODEFORMAT;
        return buildKeyValueVo(carDirectionType);
    }

    private List<KeyValueVo> initHairStyleList() {
        Map<String, String> hairStyleMap = RgbConstants.HAIRSTYLE_CODEFORMAT;
        return buildKeyValueVo(hairStyleMap);
    }

    private List<KeyValueVo> initCarBrandList() {
        Map<String, String> carBrandMap = new HashMap<>();
        String[] brandArray = (String[]) EhcacheUtils.getItem("brandList");
        if (null == brandArray || brandArray.length < 0) {
            String brandStr = enumerationConfig.getVehicleBrand();
            brandArray = brandStr.split("/");
            EhcacheUtils.putItem("brandList", brandArray);
        }
        for (int i = 0; i < brandArray.length; i++) {
            // 99为未知筛选项，不用99作为code
            if (i == 99) {
                i++;
            }
            carBrandMap.put(i + "", brandArray[i]);
        }
        return buildKeyValueVo(carBrandMap);
    }

    private List<KeyValueVo> initPlateClassList() {
        Map<String, String> plateClassMap = StringUtils.splitEnumerationCodeForName(enumerationConfig.getPlateClass());
        return buildKeyValueVo(plateClassMap);
    }

    private List<KeyValueVo> initSocialAttributeList() {
        Map<Integer, String> plateClassMap = FeaturesConstant.getSocialAttribute();
        return buildIntegerKeyValueVo(plateClassMap);
    }

    private List<KeyValueVo> initEnterpriseList() {
        Map<Integer, String> e = FeaturesConstant.getEnterprise();
        return buildIntegerKeyValueVo(e);
    }

    private List<KeyValueVo> buildKeyValueVo(Map<String, String> kevalueMap) {
        List<KeyValueVo> kevalueList = new ArrayList<>();
        for (Map.Entry<String, String> entry : kevalueMap.entrySet()) {
            if (entry.getKey().equals("-1") || entry.getKey().equals("99")) {
                continue;
            }
            KeyValueVo keyValueVo = new KeyValueVo();
            keyValueVo.setKey(entry.getKey());
            keyValueVo.setValue(entry.getValue());
            kevalueList.add(keyValueVo);
        }
        return kevalueList;
    }

    private List<KeyValueVo> buildIntegerKeyValueVo(Map<Integer, String> kevalueMap) {
        List<KeyValueVo> kevalueList = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : kevalueMap.entrySet()) {
            if (-1 == entry.getKey() || 99 == entry.getKey()) {
                continue;
            }
            KeyValueVo keyValueVo = new KeyValueVo();
            keyValueVo.setKey(String.valueOf(entry.getKey()));
            keyValueVo.setValue(entry.getValue());
            kevalueList.add(keyValueVo);
        }
        return kevalueList;
    }
}
