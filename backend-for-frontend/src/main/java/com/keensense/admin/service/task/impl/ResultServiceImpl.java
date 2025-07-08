package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.entity.task.BikeColorModel;
import com.keensense.admin.entity.task.BrandModel;
import com.keensense.admin.entity.task.CtrlUnitFile;
import com.keensense.admin.entity.task.HumanColorModel;
import com.keensense.admin.mapper.task.BikeColorModelMapper;
import com.keensense.admin.mapper.task.BrandModelMapper;
import com.keensense.admin.mapper.task.CameraMapper;
import com.keensense.admin.mapper.task.CtrlUnitFileMapper;
import com.keensense.admin.mapper.task.HumanColorModelMapper;
import com.keensense.admin.mapper.task.MonitorGroupDetailMapper;
import com.keensense.admin.mapper.task.VsdTaskRelationMapper;
import com.keensense.admin.request.ResultQueryRequest;
import com.keensense.admin.service.ext.QueryAnalysisResultService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.Page;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.util.ValidateHelper;
import com.keensense.admin.vo.ColorVo;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class ResultServiceImpl implements ResultService {

    @Resource
    private CameraMapper cameraMapper;

    @Resource
    private VsdTaskRelationMapper vsdTaskRelationMapper;

    @Resource
    private CtrlUnitFileMapper ctrlUnitFileMapper;

    @Resource
    private MonitorGroupDetailMapper monitorGroupDetailMapper;

    @Autowired
    private QueryAnalysisResultService queryAnalysisResultService;

    @Resource
    private HumanColorModelMapper humanColorModelMapper;

    @Resource
    private BikeColorModelMapper bikeColorModelMapper;

    @Resource
    private BrandModelMapper brandModelMapper;

    @Autowired
    private IVsdTaskRelationService vsdTaskRelationService;

    @Override
    public Map<String, Object> getRealtimeDataByExt(Page<ResultQueryVo> page, Map<String, Object> map) {
        Map<String, Object> pamaMap = new HashMap<>();

        Map<String, Object> resultMap = new HashMap<>();

        ResultQueryVo paramBo = (ResultQueryVo) map.get("paramBo");

        final List<ResultQueryVo> resultList = new ArrayList<>();

        initFaceCond(pamaMap, paramBo);

        initCommonCond(pamaMap, paramBo);

        // 排序初始化
        initSortInfo(pamaMap, paramBo);

        pamaMap.put("rows", String.valueOf(page.getPageSize()));
        pamaMap.put("page", String.valueOf(page.getPageNo()));
        pamaMap.put("sorting", paramBo.getTimeSelect());
        String type = (String) map.get("type");
        pamaMap.put("objType", type);
        pamaMap.put("serialnumber", paramBo.getSerialnumber());
        dealDataByJManager(pamaMap, resultMap, paramBo, resultList);
        return resultMap;
    }

    @Override
    public Map<String, Object> getOfflinetDataByExt(Page<ResultQueryVo> page, Map<String, Object> map) {
        Map<String, Object> pamaMap = new HashMap<>();

        Map<String, Object> resultMap = new HashMap<>();

        String type = (String) map.get("type");

        final List<ResultQueryVo> resultList = new ArrayList<>();

        ResultQueryVo paramBo = (ResultQueryVo) map.get("paramBo");
        if (null != paramBo) {
            initFaceCond(pamaMap, paramBo);
            initCommonCond(pamaMap, paramBo);
        }
        pamaMap.put("rows", String.valueOf(page.getPageSize()));
        pamaMap.put("page", String.valueOf(page.getPageNo()));
        pamaMap.put("queryType", "offline");
        if (null != map.get("sorting")) {
            pamaMap.put("sorting", map.get("sorting"));
        }
//        VsdTaskRelation vsdTaskRelation = vsdTaskRelationMapper.selectOne(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", pamaMap.get("serialnumber").toString()));
//        if(vsdTaskRelation == null) {
//            throw new VideoException("流水号不存在");
//        }
//        Long cameraFileId = vsdTaskRelation.getCameraFileId();
//        CtrlUnitFile ctrlUnitFile = ctrlUnitFileMapper.selectById(cameraFileId);
        //TODO 需改正
        CtrlUnitFile ctrlUnitFile = ctrlUnitFileMapper.selectById(pamaMap.get("serialnumber").toString());
        if (ctrlUnitFile != null) {
            Long entryTime = ctrlUnitFile.getEntryTime().getTime();
            String startTime = DateTimeUtils.timeToDefaultDate(entryTime - 60 * 60 * 24 * 1000);
            String endTime = DateTimeUtils.timeToDefaultDate(entryTime + 24 * 60 * 60 * 1000);

            if (!pamaMap.containsKey("startTime")) {
                pamaMap.put("startTime", startTime);
            }
            if (!pamaMap.containsKey("endTime")) {
                pamaMap.put("endTime", endTime);
            }
        }

        // 离线视频结构化结果升序
        if (StringUtils.isNotEmptyString(paramBo.getTimeSelect())) {
            pamaMap.put("sorting", paramBo.getTimeSelect());
        } else {
            pamaMap.put("sorting", "asc");
        }
        // 目标结构化
        pamaMap.put("objType", type);
        dealDataByJManager(pamaMap, resultMap, paramBo, resultList);
        return resultMap;
    }

    private void initFaceCond(Map<String, Object> pamaMap, ResultQueryVo paramBo) {
        if (null != paramBo.getFaceAge()) {
            pamaMap.put("faceAge", paramBo.getFaceAge());
        }
        if (null != paramBo.getFaceSex()) {
            pamaMap.put("faceSex", paramBo.getFaceSex());
        }

        if (null != paramBo.getNation()) {
            pamaMap.put("nation", paramBo.getNation());
        }

        if (null != paramBo.getFaceGlassess()) {
            pamaMap.put("faceGlassess", paramBo.getFaceGlassess());
        }
    }

    private void initCommonCond(Map<String, Object> pamaMap, ResultQueryVo paramBo) {
        if (StringUtils.isNotEmptyString(paramBo.getTaskSerialnumbers())) {
            pamaMap.put("serialnumber", paramBo.getTaskSerialnumbers());
        } else {
            pamaMap.put("serialnumber", paramBo.getSerialnumber());
        }


        if (null != paramBo.getTaskid()) {
            pamaMap.put("taskid", paramBo.getTaskid());
        }

        if (StringUtils.isNotEmptyString(paramBo.getStartTime())) {
            pamaMap.put("startTime", paramBo.getStartTime());
        }
        if (StringUtils.isNotEmptyString(paramBo.getEndTime())) {
            pamaMap.put("endTime", paramBo.getEndTime());
        }
        if (StringUtils.isNotEmptyString(paramBo.getType())) {
            pamaMap.put("objtype", paramBo.getType());
        }

    }

    private void initSortInfo(Map<String, Object> pamaMap, ResultQueryVo paramBo) {
        if (StringUtils.isNotEmptyString(paramBo.getTimeSelect())) {
            pamaMap.put("sorting", paramBo.getTimeSelect());

            // 时间相差超过一天
            if (DateTimeUtils.daysBetween(paramBo.getStartTime(), paramBo.getEndTime()) > 1
                    || StringUtils.isEmptyString(paramBo.getStartTime()) || StringUtils.isEmptyString(paramBo.getEndTime())) {
                if (ASC.equalsIgnoreCase(paramBo.getTimeSelect())) {
                    pamaMap.put("sorting", ID_ASC);
                } else {
                    pamaMap.put("sorting", ID_DESC);
                }
            }
        } else {
            // 默认降序
            pamaMap.put("sorting", ASC);
            if (DateTimeUtils.daysBetween(paramBo.getStartTime(), paramBo.getEndTime()) > 1) {
                pamaMap.put("sorting", ID_ASC);
            }
        }
    }


    @Override
    public void dealDataByJManager(Map<String, Object> pamaMap, Map<String, Object> resultMap, ResultQueryVo paramBo, List<ResultQueryVo> resultList) {
        Map<String, Object> resultJson = queryAnalysisResultService.doHttpService(pamaMap, paramBo);
        if (null == resultJson) {
            resultMap.put("code", 500);
            resultMap.put("msg", "jmanager调用异常：无返回");
            return;
        }

        if (!resultJson.get("code").equals(0)) {
            resultMap.put("code", 500);
            resultMap.put("msg", resultJson.get("desc"));
            return;
        }

        resultMap.put("resultBoList", resultJson.get("resultBoList"));
        resultMap.put("totalNum", resultJson.get("totalNum"));
    }

    @Override
    public ResultQueryVo getResultBoById(String objtype, String resultId) {
        ResultQueryVo resultBo = null;

        if (StringUtils.isEmptyString(resultId)) {
            return resultBo;
        }
        Map<String, Object> pamaMap = new HashedMap();
        pamaMap.put("objType", objtype);
        pamaMap.put("uuid", resultId);
        String returnJson = queryAnalysisResultService.doHttpService(pamaMap);

        List<ResultQueryVo> resultBoList = queryAnalysisResultService.transAllResultToBo(returnJson);

        if (resultBoList != null && !resultBoList.isEmpty()) {
            resultBo = resultBoList.get(0);
        }
        if (resultBo == null) {
            log.info("图片信息不存在 resultId : " + resultId + "\t objtype : " + objtype);
        } else {
            resultBo.setObjtype(Short.valueOf(objtype));
        }
        return resultBo;
    }

    @Override
    public Map<String, Object> getMonRealtimeDataByExt(Page<ResultQueryVo> page, Map<String, Object> map) {
        Map<String, Object> pamaMap = new HashMap<>();

        Map<String, Object> resultMap = new HashMap<>();

        ResultQueryVo paramBo = (ResultQueryVo) map.get("paramBo");

        final List<ResultQueryVo> resultList = new ArrayList<>();

        initFaceCond(pamaMap, paramBo);

        initCommonCond(pamaMap, paramBo);

        // 排序初始化
        initSortInfo(pamaMap, paramBo);

        pamaMap.put("rows", String.valueOf(page.getPageSize()));
        pamaMap.put("page", String.valueOf(page.getPageNo()));
        pamaMap.put("sorting", paramBo.getTimeSelect());
        String type = (String) map.get("type");
        pamaMap.put("objType", type);
        String monitorGroupId = (String) map.get("monitorGroupId");
        List<String> monitorGroupIds = monitorGroupDetailMapper.selectMonitorGroupSerialnumber(Long.valueOf(monitorGroupId));
        if (monitorGroupIds != null && !monitorGroupIds.isEmpty()) {
            String serialnumberStr = String.join(",", monitorGroupIds);

            String objType = paramBo.getType();
            if (StringUtils.isNotEmptyString(objType)) {
                if (null != paramBo.getOverlineType() && !paramBo.getOverlineType().equals(0)
                        && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON)) {
                    String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineType());
                    serialnumberStr = getOverLineSerialnumber(overStr, serialnumberStr);
                    if (StringUtils.isEmptyString(serialnumberStr)) {
                        resultMap.put("totalNum", 0);
                        resultMap.put("resultBoList", new ArrayList<>());
                        return resultMap;
                    }
                }
                if (null != paramBo.getOverlineTypeBike() && !paramBo.getOverlineTypeBike().equals(0)
                        && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR_PERSON)) {
                    String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineTypeBike());
                    serialnumberStr = getOverLineSerialnumber(overStr, serialnumberStr);
                    if (StringUtils.isEmptyString(serialnumberStr)) {
                        resultMap.put("totalNum", 0);
                        resultMap.put("resultBoList", new ArrayList<>());
                        return resultMap;
                    }
                }
                if (null != paramBo.getOverlineTypeCar() && !paramBo.getOverlineTypeCar().equals(0)
                        && objType.equals(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR)) {
                    String overStr = vsdTaskRelationService.selectOverlineTypeSerialNumber(paramBo.getOverlineTypeCar());
                    serialnumberStr = getOverLineSerialnumber(overStr, serialnumberStr);
                    if (StringUtils.isEmptyString(serialnumberStr)) {
                        resultMap.put("totalNum", 0);
                        resultMap.put("resultBoList", new ArrayList<>());
                        return resultMap;
                    }
                }
            }

            pamaMap.put("serialnumber", serialnumberStr);
            dealDataByJManager(pamaMap, resultMap, paramBo, resultList);
        } else {
            resultMap.put("totalNum", 0);
            resultMap.put("resultBoList", new ArrayList<>());
        }
        return resultMap;
    }

    /**
     * 根据入侵跨界类型查询任务号
     *
     * @param overStr
     * @param serialnumberStr
     * @return
     */
    public String getOverLineSerialnumber(String overStr, String serialnumberStr) {
        String[] overStrArray = overStr.split(",");
        List<String> overList = Arrays.asList(overStrArray);
        String[] serialnumberStrArray = serialnumberStr.split(",");
        String serialnumber = "";
        for (int i = 0; i < serialnumberStrArray.length; i++) {
            if (overList.contains(serialnumberStrArray[i]) && i == 0) {
                serialnumber = serialnumberStrArray[i];
            } else if (overList.contains(serialnumberStrArray[i]) && i != 0){
                serialnumber = "," + serialnumberStrArray[i];
            }
        }

        return serialnumber;
    }

    @Override
    public Map<String, Object> queryResultByJManager(Map<String, Object> map) {
        Map<String, Object> pamaMap = new HashMap<>();

        Map<String, Object> resultMap = new HashMap<>();

        final List<ResultQueryVo> resultList = new ArrayList<>();

        // 监控点条件
        String cameraId = MapUtil.getString(map, "cameraId");
        if (StringUtils.isNotEmptyString(cameraId)) {
            pamaMap.put("cameraId", cameraId);
        }

        ResultQueryVo paramBo = (ResultQueryVo) map.get("paramBo");
        String objType = MapUtil.getString(map, "objType");
        if (null != paramBo) {
            /*if(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON.equals(objType)){
                // 人
                initPersonCond(pamaMap, paramBo);
            }else if(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR.equals(objType)){
                // 车
                initCarCond(pamaMap, paramBo);
            }else if(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR_PERSON.equals(objType)){
                // 人骑车
                initBikeCond(pamaMap, paramBo);
            }else if(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_FACE.equals(objType)){
                // 人脸
                initFaceCond(pamaMap, paramBo);
            }else if(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_OTHER.equals(objType)){
            }*/
            // 公共条件： 时间/监控点
            initCommonCond(pamaMap, paramBo);
        }
        if (null != map.get("sorting")) {
            pamaMap.put("sorting", map.get("sorting"));
        }
        pamaMap.put("rows", map.get("rows"));
        pamaMap.put("page", map.get("page"));
        if (map.get("serialnumber") != null) {
            pamaMap.put("serialnumber", map.get("serialnumber"));
        }
        if (map.get("startTime") != null) {
            pamaMap.put("startTime", map.get("startTime"));
        }
        if (map.get("endTime") != null) {
            pamaMap.put("endTime", map.get("endTime"));
        }
        pamaMap.put("objType", objType);

        dealDataByJManager(pamaMap, resultMap, paramBo, resultList);

        return resultMap;
    }

    private void initPersonCond(Map<String, Object> pamaMap, ResultQueryVo paramBo) {
        if (StringUtils.isNotEmptyString(paramBo.getUpcolorStr())) {
            String[] upcolorStrArr = paramBo.getUpcolorStr().split(",");
            if (null != upcolorStrArr && upcolorStrArr.length > 0) {
                List<Integer> colorList = new ArrayList<>();
                for (int i = 0; i < upcolorStrArr.length; i++) {
                    if (StringUtils.isNumeric(upcolorStrArr[i])) {
                        List<Integer> tempcolorList = getColorTagList(Integer.valueOf(upcolorStrArr[i]), ColorVo.HUMAN_COLOR_TYPE);
                        colorList.addAll(tempcolorList);
                    }
                }
                pamaMap.put("upcolor", colorList);
            }
        }

        if (StringUtils.isNotEmptyString(paramBo.getLowcolorStr())) {
            String[] lowcolorArr = paramBo.getLowcolorStr().split(",");
            if (null != lowcolorArr && lowcolorArr.length > 0) {
                List<Integer> colorList = new ArrayList<>();
                for (int i = 0; i < lowcolorArr.length; i++) {
                    if (StringUtils.isNumeric(lowcolorArr[i])) {
                        List<Integer> tempcolorList = getColorTagList(Integer.valueOf(lowcolorArr[i]), ColorVo.HUMAN_COLOR_TYPE);
                        colorList.addAll(tempcolorList);
                    }

                }
                pamaMap.put("lowcolor", colorList);
            }
        }

        if (null != paramBo.getSex()) {
            pamaMap.put("sex", paramBo.getSex());
        }
        if (null != paramBo.getAge()) {
            pamaMap.put("age", paramBo.getAge());
        }

        if (null != paramBo.getAngle()) {
            pamaMap.put("angle", paramBo.getAngle());
        }
        if (null != paramBo.getAngle2()) {
            pamaMap.put("angle2", paramBo.getAngle2());
        }
        if (null != paramBo.getGlasses()) {
            pamaMap.put("glasses", paramBo.getGlasses());
        }
        if (null != paramBo.getHandbag()) {
            pamaMap.put("handbag", paramBo.getHandbag());
        }
        if (null != paramBo.getBag()) {
            pamaMap.put("bag", paramBo.getBag());
        }

        if (null != paramBo.getUmbrella()) {
            pamaMap.put("umbrella", paramBo.getUmbrella());
        }
        if (StringUtils.isNotEmptyString(paramBo.getCoatStyle()) && !",".equals(paramBo.getCoatStyle())) {
            String coatStyle = paramBo.getCoatStyle();
            if (-1 != paramBo.getCoatStyle().lastIndexOf(",")) {
                coatStyle = paramBo.getCoatStyle().replaceAll(",", "");
            }
            pamaMap.put("coatStyle", coatStyle);
        }
        if (StringUtils.isNotEmptyString(paramBo.getTrousersStyle())) {
            pamaMap.put("trousersStyle", paramBo.getTrousersStyle());
        }
    }

    private List<Integer> getColorTagList(Integer colorTag, String type) {
        List<Integer> colorList = new ArrayList<>();
        List<ColorVo> colorModelList = null;
        if (ColorVo.HUMAN_COLOR_TYPE.equalsIgnoreCase(type)) {
            colorModelList = EntityObjectConverter.getList(humanColorModelMapper.selectList(new QueryWrapper<HumanColorModel>().eq("color_bgr_tag", colorTag)), ColorVo.class);
        } else if (ColorVo.BIKE_COLOR_TYPE.equalsIgnoreCase(type)) {
            colorModelList = EntityObjectConverter.getList(bikeColorModelMapper.selectList(new QueryWrapper<BikeColorModel>().eq("color_bgr_tag", colorTag)), ColorVo.class);
        }
        if (ValidateHelper.isNotEmptyList(colorModelList)) {
            for (ColorVo colorVo : colorModelList) {
                colorList.add(colorVo.getColorBgrTag());
            }
        }
        return colorList;
    }

    private void initCarCond(Map<String, Object> pamaMap, ResultQueryVo paramBo) {
        if (StringUtils.isNotEmptyString(paramBo.getCarcolor())) {
            String[] carcolorStrArr = paramBo.getCarcolor().split(",");
            if (null != carcolorStrArr && carcolorStrArr.length > 0) {
                List<String> colorList = new ArrayList<>();
                for (int i = 0; i < carcolorStrArr.length; i++) {
                    colorList.add(carcolorStrArr[i]);
                }

                pamaMap.put("carcolor", colorList);
            }
        }

        if (null != paramBo.getSize()) {
            pamaMap.put("size", Integer.valueOf(paramBo.getSize()));
            List<BrandModel> carKindList = brandModelMapper.selectAllCarKind(paramBo.getSize().intValue());
            if (ValidateHelper.isNotEmptyList(carKindList)) {
                pamaMap.put("carSize", carKindList.get(0).getCarKind());
            } else {
                pamaMap.put("noCarSize", "1");
            }
        }

        if (StringUtils.isNotEmptyString(paramBo.getCarlogo())) {
            pamaMap.put("carlogo", paramBo.getCarlogo());
        }


        if (StringUtils.isNotEmptyString(paramBo.getLicense())) {
            pamaMap.put("license", "%" + paramBo.getLicense() + "%");
        }

        if (StringUtils.isNotEmptyString(paramBo.getVehiclekind())) {
            pamaMap.put("vehiclekind", paramBo.getVehiclekind());
        }

        if (StringUtils.isNotEmptyString(paramBo.getVehicleseries())) {
            pamaMap.put("vehicleseries", paramBo.getVehicleseries());
        }


        if (null != paramBo.getCrash()) {
            pamaMap.put("crash", paramBo.getCrash());
        }

        if (null != paramBo.getDanger()) {
            pamaMap.put("danger", paramBo.getDanger());
        }

        if (null != paramBo.getDrop()) {
            pamaMap.put("drop", paramBo.getDrop());
        }

        if (null != paramBo.getPaper()) {
            pamaMap.put("paper", paramBo.getPaper());
        }

        if (null != paramBo.getSun()) {
            pamaMap.put("sun", paramBo.getSun());
        }

        if (null != paramBo.getTag()) {
            pamaMap.put("tag", paramBo.getTag());
        }

        if (null != paramBo.getTaskid()) {
            pamaMap.put("taskid", paramBo.getTaskid());
        }

    }

    private void initBikeCond(Map<String, Object> pamaMap, ResultQueryVo paramBo) {
        if (null != paramBo.getWheels()) {
            pamaMap.put("wheels", paramBo.getWheels());
        }
        if (null != paramBo.getHandbag()) {
            pamaMap.put("handbag", paramBo.getHandbag());
        }
        // 车身颜色
        if (StringUtils.isNotEmptyString(paramBo.getBikecolorStr())) {
            String[] bikeColorStrArr = paramBo.getBikecolorStr().split(",");
            if (null != bikeColorStrArr && bikeColorStrArr.length > 0) {
                List<Integer> colorList = new ArrayList<>();
                for (int i = 0; i < bikeColorStrArr.length; i++) {
                    if (StringUtils.isNumeric(bikeColorStrArr[i])) {
                        List<Integer> tempcolorList = getColorTagList(Integer.valueOf(bikeColorStrArr[i]), ColorVo.BIKE_COLOR_TYPE);
                        colorList.addAll(tempcolorList);
                    }

                }
                pamaMap.put("bikecolor", colorList);
            }
        }

        // 头盔颜色
        if (StringUtils.isNotEmptyString(paramBo.getHelmetcolorStr())) {
            String[] helmetcolorStrArr = paramBo.getHelmetcolorStr().split(",");
            if (null != helmetcolorStrArr && helmetcolorStrArr.length > 0) {
                List<Integer> colorList = new ArrayList<>();
                for (int i = 0; i < helmetcolorStrArr.length; i++) {
                    if (StringUtils.isNumeric(helmetcolorStrArr[i])) {
                        List<Integer> tempcolorList = getColorTagList(Integer.valueOf(helmetcolorStrArr[i]), ColorVo.BIKE_COLOR_TYPE);
                        colorList.addAll(tempcolorList);
                    }

                }
                pamaMap.put("helmetcolor", colorList);
            }
        }

        // 上身颜色
        if (StringUtils.isNotEmptyString(paramBo.getPassengersUpColorStr())) {
            String[] passengersUpcolorStrArr = paramBo.getPassengersUpColorStr().split(",");
            if (null != passengersUpcolorStrArr && passengersUpcolorStrArr.length > 0) {
                List<Integer> colorList = new ArrayList<>();
                for (int i = 0; i < passengersUpcolorStrArr.length; i++) {
                    if (StringUtils.isNumeric(passengersUpcolorStrArr[i])) {
                        List<Integer> tempcolorList = getColorTagList(Integer.valueOf(passengersUpcolorStrArr[i]), ColorVo.BIKE_COLOR_TYPE);
                        colorList.addAll(tempcolorList);
                    }

                }
                pamaMap.put("upcolor", colorList);
            }
        }
        if (null != paramBo.getSeatingCount()) {
            pamaMap.put("seatingCount", paramBo.getSeatingCount());
        }

        if (null != paramBo.getBikeGenre()) {
            pamaMap.put("bikeGenre", paramBo.getBikeGenre());
        }
        if (null != paramBo.getHelmet()) {
            pamaMap.put("helmet", paramBo.getHelmet());
        }

        if (null != paramBo.getBikeSex()) {
            pamaMap.put("sex", paramBo.getBikeSex());
        }
        if (null != paramBo.getBikeAge()) {
            pamaMap.put("age", paramBo.getBikeAge());
        }

        if (null != paramBo.getUmbrella()) {
            pamaMap.put("umbrella", paramBo.getUmbrella());
        }

        if (null != paramBo.getBikeHasPlate()) {
            pamaMap.put("bikeHasPlate", paramBo.getBikeHasPlate());
        }

        if (StringUtils.isNotEmptyString(paramBo.getCoatStyle()) && !",".equals(paramBo.getCoatStyle())) {
            String coatStyle = paramBo.getCoatStyle();
            if (-1 != paramBo.getCoatStyle().lastIndexOf(",")) {
                coatStyle = paramBo.getCoatStyle().replaceAll(",", "");
            }
            pamaMap.put("coatStyle", coatStyle);
        }
    }
}
