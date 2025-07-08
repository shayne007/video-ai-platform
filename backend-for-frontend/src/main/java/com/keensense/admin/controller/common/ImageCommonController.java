package com.keensense.admin.controller.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.dto.FileBo;
import com.keensense.admin.dto.SearchImageBo;
import com.keensense.admin.entity.task.TbImagesearchRecord;
import com.keensense.admin.mapper.task.TbImagesearchRecordMapper;
import com.keensense.admin.service.ext.FeatureSearchService;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.sys.ICfgMemPropsService;
import com.keensense.admin.service.task.CaseService;
import com.keensense.admin.service.task.FtpService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.R;
import com.loocme.plugin.spring.comp.Select;
import com.loocme.sys.datastruct.IVarForeachHandler;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 10:09 2019/6/17
 * @Version v0.1
 */
@Slf4j
@Api(tags = "常规-图片控制")
@RestController
@RequestMapping("/image")
public class ImageCommonController {
    @Autowired
    private FeatureSearchService featureSearchService;

    @Resource
    private FtpService ftpService;

    @Autowired
    private ICfgMemPropsService cfgMemPropsService;

    @Autowired
    private ResultService resultService;

    @Resource
    private CaseService caseService;

    @Autowired
    private VideoObjextTaskService videoObjextTaskService;

    @Autowired
    private TbImagesearchRecordMapper imagesearchRecordMapper;

    /**
     * 图片上传,返回ftp路径
     *
     * @return 图片保存的ftp路径
     */
    @ApiOperation("图片上传,返回全路径")
    @PostMapping("sumbitImage")
    public R sumbitImage(MultipartFile file) throws Exception {
        R result = R.ok();
        Map<String, Object> datas = new HashMap<>();
        if (file != null && !file.isEmpty()) {
            byte[] bytes = file.getBytes();
            BASE64Encoder encode = new BASE64Encoder();
            String imageBase64 = encode.encode(bytes).replaceAll("\\r|\\n", "");
            String fileUrl = videoObjextTaskService.saveImageToFdfs(imageBase64);
            FileBo fileBo = ftpService.editImage(null, null, null, null, fileUrl);
            datas.put("fileUrl", fileBo.getFileFtpPath());
        } else {
            return R.error("文件为空");
        }
        result.put("data", datas);
        return result;
    }

    /**
     * 图片上传，获取截取图片
     *
     * @return 图片保存的ftp路径
     */
    @ApiOperation("图片上传，获取截取图片 ")
    @PostMapping("editImage")
    public R editImage(Integer x, Integer y, Integer width, Integer height, String imageUrl, String uuid, String imageType) throws Exception {
        log.info("进来editImage");
        Map<String, Object> datas = new HashMap<>();
        R result = R.ok();
        //imageType 1:人, 2:车, 3:人脸, 4:骑行
        if (StringUtils.isNotEmptyString(uuid) && StringUtils.isNotEmptyString(imageType)) {
            if ("1".equals(imageType)) {//人行大图里面的以脸搜脸
                ResultQueryVo resultQueryVo = resultService.getResultBoById("1", uuid);
                if (resultQueryVo == null){
                    log.info("人ID查ES无数据" + uuid);
                    return R.error("暂无人行数据");
                }
                String faceUUID = resultQueryVo.getFaceUUID();
                ResultQueryVo faceResultQueryVo = resultService.getResultBoById("3", faceUUID);
                if (faceResultQueryVo == null){
                    log.info("人脸ID查ES无数据" + faceUUID);
                    return R.error("暂无人脸数据");
                }
                width = faceResultQueryVo.getW().intValue();
                height = faceResultQueryVo.getH().intValue();
                imageUrl = faceResultQueryVo.getImgurl();
                datas.put("fileUrl", imageUrl);//搜脸直接返回原url
                result.put("data", datas);
                return result;
            } else if ("3".equals(imageType)) {//脸大图里面的以人搜人,以骑搜骑
                ResultQueryVo resultQueryVo = resultService.getResultBoById("3", uuid);
                if (resultQueryVo == null){
                    log.info("人脸ID查ES无数据" + uuid);
                    return R.error("暂无人脸数据");
                }
                String connectUUID = resultQueryVo.getConnectObjectId();
                Integer connectType = resultQueryVo.getConnectObjectType();
                ResultQueryVo connectResultQueryVo = new ResultQueryVo();
                if(connectType==null) {
                    connectResultQueryVo = resultService.getResultBoById("1", connectUUID);
                    if (connectResultQueryVo == null){
                        connectResultQueryVo = resultService.getResultBoById("4", connectUUID);
                    }
                    if (connectResultQueryVo == null){
                        log.info("人行/骑行ID查ES无数据" + connectUUID);
                        return R.error("暂无骑行或人行对应数据");
                    }
                }  else if (1 == connectType) {
                    connectResultQueryVo = resultService.getResultBoById("1", connectUUID);
                    if (connectResultQueryVo == null){
                        log.info("人ID查ES无数据" + connectUUID);
                        return R.error("暂无人行数据");
                    }
                } else if (4 == connectType) {
                    connectResultQueryVo = resultService.getResultBoById("4", connectUUID);
                    if (connectResultQueryVo == null){
                        log.info("骑行ID查ES无数据" + connectUUID);
                        return R.error("暂无骑行数据");
                    }
                }
                width = connectResultQueryVo.getW().intValue();
                height = connectResultQueryVo.getH().intValue();
                imageUrl = connectResultQueryVo.getImgurl();
            } else if ("4".equals(imageType)){//骑行大图里面的以脸搜脸
                ResultQueryVo resultQueryVo = resultService.getResultBoById("4", uuid);
                if (resultQueryVo == null){
                    log.info("骑行ID查ES无数据" + uuid);
                    return R.error("暂无骑行数据");
                }
                String faceUUID = resultQueryVo.getFaceUUID();
                ResultQueryVo faceResultQueryVo = resultService.getResultBoById("3", faceUUID);
                if (faceResultQueryVo == null){
                    log.info("人脸ID查ES无数据" + faceUUID);
                    return R.error("暂无人脸数据");
                }
                width = faceResultQueryVo.getW().intValue();
                height = faceResultQueryVo.getH().intValue();
                imageUrl = faceResultQueryVo.getImgurl();
                datas.put("fileUrl", imageUrl);
                result.put("data", datas);
                return result;//搜脸直接返回原url
            }
        }
        if (StringUtils.isEmptyString(uuid) && StringUtils.isNotEmptyString(imageType) && imageType.equals("3")) {//查看结果人脸搜图
            if (StringUtils.isNotEmptyString(imageUrl) && imageUrl.startsWith("http:")) {
                datas.put("fileUrl", imageUrl);//搜脸直接返回原url
                result.put("data", datas);
                return result;
            }
            //来自案件管理里的人脸图片
            String imageBase64 = "";
            if (StringUtils.isNotEmptyString(imageUrl) && imageUrl.startsWith("persist_picture")) {
                String picture = caseService.getPictureStr(imageUrl);
                if (StringUtils.isNotEmptyString(picture)) {
                    imageBase64 = picture;
                }
            } else {
                //手动框选图片
                imageBase64 = imageUrl;
            }
            imageUrl = videoObjextTaskService.saveImageToFdfs(imageBase64);
            datas.put("fileUrl", imageUrl);//格林算法的搜脸直接返回原url
            result.put("data", datas);
            return result;
        }
        FileBo fileBo = ftpService.editImage(x, y, width, height, imageUrl);
        datas.put("fileUrl", fileBo.getFileFtpPath());
        result.put("data", datas);
        return result;
    }

    @ApiOperation("目标检测接口")
    @PostMapping("handleTargetPosition")
    public R handleTargetPosition(String imageUrl, Integer objtype) {
        R result = R.ok();
        Map<String, Object> data = new HashMap<>();
        Var searchPostion = getTargetPosition(imageUrl, objtype);

        Var objexts = searchPostion.get("objexts");

        List<SearchImageBo> searchImageBos = new ArrayList<>();
        objexts.foreach(new IVarForeachHandler() {

            private static final long serialVersionUID = 1L;

            @Override
            public void execute(String index, Var objextVar) {
                StringBuilder innerXywhs = new StringBuilder();
                SearchImageBo searchImageBo = new SearchImageBo();

                innerXywhs.append(objextVar.getString("snapshot.boundingBox.x")).append("-").append(objextVar.getString("snapshot.boundingBox.y"))
                        .append("-").append(objextVar.getString("snapshot.boundingBox.w")).append("-").append(objextVar.getString("snapshot.boundingBox.h"));

                searchImageBo.setXywh(innerXywhs.toString());
                searchImageBo.setFeature(objextVar.getString("features.featureData"));
                searchImageBo.setObjType(objextVar.getInt("objType"));
                double width = Double.parseDouble(objextVar.getString("snapshot.boundingBox.w"));
                double height = Double.parseDouble(objextVar.getString("snapshot.boundingBox.h"));
                searchImageBo.setFeatureArea(width * height);

                searchImageBos.add(searchImageBo);

            }
        });

        compareFeatureArea(searchImageBos);

        StringBuilder xywhs = new StringBuilder();
        StringBuilder features = new StringBuilder();
        StringBuilder objTypes = new StringBuilder();
        StringBuilder featureAreas = new StringBuilder();

        if (ListUtil.isNotNull(searchImageBos)) {
            for (int i = 0; i < searchImageBos.size(); i++) {
                SearchImageBo searchImageBo = searchImageBos.get(i);
                if (xywhs.length() != 0) {
                    xywhs.append(",");
                    features.append(",");
                    objTypes.append(",");
                    featureAreas.append(",");
                }
                xywhs.append(searchImageBo.getXywh());
                features.append(searchImageBo.getFeature());
                objTypes.append(searchImageBo.getObjType());
                featureAreas.append(searchImageBo.getFeatureArea());
            }
            data.put("xywhs", xywhs);
            data.put("features", features);
            data.put("objTypes", objTypes);
            data.put("featureAreas", featureAreas);
            return result.put("data", data);
        } else {
            return R.error("未自动检测到目标");
        }

    }

    public Var getTargetPosition(String pictureUrl, Integer objtype) {

        Map<String, Object> var = new HashMap<>();
        var.put("picture", pictureUrl);
        var.put("objtype", objtype);
        String reString = featureSearchService.doStructPictureService(var);
        Var searchPostion = Var.fromJson(reString);
        if (StringUtil.isNull(reString)) {
            throw new VideoException(700, "特征值获取失败");
        }
        String ret = searchPostion.getString("ret");
        if (!"0".equals(ret)) {
            throw new VideoException("目标检测异常,视图库错误:" + searchPostion.getString("desc"));
        }
        return searchPostion;
    }

    /**
     * 根据特征区域的面积进行降序排序
     *
     * @param searchImageBos
     */
    private void compareFeatureArea(List<SearchImageBo> searchImageBos) {
        Collections.sort(searchImageBos, new Comparator<SearchImageBo>() {
            @Override
            public int compare(SearchImageBo o1, SearchImageBo o2) {
                if (o2.getFeatureArea() - o1.getFeatureArea() > 0) {
                    return 1;
                } else if (o2.getFeatureArea() - o1.getFeatureArea() == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }


    /**
     * 置顶(匹配度后台随机取值99.7~99.9)
     *
     * @param request
     * @param uuid
     * @param score
     * @param serialnumber
     * @return
     */
    @ApiOperation("置顶(匹配度后台随机取值99.7~99.9)")
    @PostMapping("topTargetMem")
    @ApiIgnore
    public R topTargetMem(HttpServletRequest request, String uuid, String serialnumber, String score) {
        R result = R.ok();
        if (StringUtils.isEmptyString(uuid) || StringUtils.isEmptyString(serialnumber) || StringUtils.isEmptyString(score)) {
            return R.error(500, "传值不全");
        }
        TbImagesearchRecord record = imagesearchRecordMapper.selectOne(new QueryWrapper<TbImagesearchRecord>()
                .eq("task_id", serialnumber).eq("record_id", uuid));
        if (record == null) {
            float max = 0.95f;
            float min = Float.valueOf(score) > 0.85 ? Float.valueOf(score) : 0.85f;
            String id = RandomUtils.get18TimeRandom();
            Float f = new Float(Math.random() * (max - min) + min);
            log.info(f + "" + max);
            TbImagesearchRecord imagesearchRecord = new TbImagesearchRecord();
            imagesearchRecord.setId(id);
            if (Float.valueOf(score) >= 0.9) {
                imagesearchRecord.setScore(score);
            } else {
                imagesearchRecord.setScore(String.valueOf(f));
            }
            imagesearchRecord.setTaskId(serialnumber);
            imagesearchRecord.setRecordId(uuid);
            imagesearchRecord.setCreateTime(new Date());
            imagesearchRecord.setType(1);
            imagesearchRecordMapper.insert(imagesearchRecord);
            result.put("code","0");
            result.put("msg", "置顶成功");
        } else {
            result.put("code","0");
            result.put("msg", "已经置顶");
        }
        return result;
    }

    /**
     * 根据图片获取特征值
     *
     * @param objextType
     * @param imageUrl
     * @return
     * @throws Exception
     */
    public String getFeatureByImageUrl(String objextType, String imageUrl) throws Exception {
        Map<String, Object> extractFromPictureParam = new HashMap<>();
        extractFromPictureParam.put("objtype", objextType + "");
        extractFromPictureParam.put("picture", com.keensense.common.util.ImageUtils.getURLImage(imageUrl));
        String resultJson = featureSearchService.doExtractFromPictureService(extractFromPictureParam);
        Var resultVar = Var.fromJson(resultJson);
        String ret = resultVar.getString("ret");
        if (!"0".equals(ret)) {
            throw new VideoException("视图库:" + resultVar.getString("desc"));
        }
        String feartureData = resultVar.getString("feature");
        if (StringUtils.isEmpty(feartureData)) {
            throw new VideoException(700, "搜图模块异常");
        }
        return feartureData;
    }

    /**
     * 根据图片获取特征值
     *
     * @param objextType
     * @param imageUrls
     * @return
     * @throws Exception
     */
    public String getFeatureByImageUrls(String objextType, String imageUrls) throws Exception {
        StringBuilder features = new StringBuilder();
        String[] pics = imageUrls.split(",");
        for (String pic : pics) {
            String feature = getFeatureByImageUrl(objextType, pic);
            if (features.length() != 0) {
                features.append(",");
            }
            features.append(feature);
        }
        return features.toString();
    }

    /**
     * 根据人脸关联uuid查询人形或骑行图
     * @param connectObjectType
     * @param connectObjectId
     * @return
     */
    @ApiOperation("根据人脸关联uuid查询人形或骑行图")
    @PostMapping("getConnectUrl")
    public R getConnectUrl(String connectObjectType, String connectObjectId) {
        R result = R.ok();
        ResultQueryVo connectResultQueryVo = resultService.getResultBoById(String.valueOf(connectObjectType), connectObjectId);
        if (connectResultQueryVo == null) {
            result = R.error();
            result.put("data", "请重新搜图");
        } else {
            result.put("data", connectResultQueryVo.getImgurl());
        }

        return result;
    }
}
