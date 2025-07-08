package com.keensense.admin.controller.cluster;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.dto.RealVideoBo;
import com.keensense.admin.dto.cluster.FeatureVo;
import com.keensense.admin.dto.cluster.OperateVo;
import com.keensense.admin.entity.cluster.TbClusterTask;
import com.keensense.admin.entity.cluster.TbClusterTaskDetail;
import com.keensense.admin.entity.sys.SysOperateLog;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.request.ResultQueryRequest;
import com.keensense.admin.request.cluster.ClusterDetailResultPageRequest;
import com.keensense.admin.request.cluster.ClusterExportPageRequest;
import com.keensense.admin.request.cluster.ClusterRecordPageRequest;
import com.keensense.admin.request.cluster.ClusterResultPageRequest;
import com.keensense.admin.service.cluster.IClusterDetailService;
import com.keensense.admin.service.cluster.IClusterService;
import com.keensense.admin.service.ext.FeatureSearchService;
import com.keensense.admin.service.ext.QueryAnalysisResultService;
import com.keensense.admin.service.task.DownloadService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.util.ExcelUtil;
import com.keensense.admin.util.FTPUtils;
import com.keensense.admin.util.FileUtils;
import com.keensense.admin.util.FileZipCompressorUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.util.ThreadTaskUtil;
import com.keensense.admin.vo.ClusterTaskDetailVo;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.common.platform.bo.feature.DumpQuery;
import com.keensense.common.platform.enums.ObjTypeEnum;
import com.keensense.common.platform.enums.TaskStatusEnum;
import com.keensense.common.util.DateUtil;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 14:43 2019/11/21
 * @Version v0.1
 */
@Slf4j
@Api(tags = "聚类检索")
@RestController
@RequestMapping("/cluster")
public class ClusterController {
    @Autowired
    FeatureSearchService featureSearchService;

    @Autowired
    IClusterService clusterService;

    @Autowired
    IClusterDetailService clusterDetailService;

    @Autowired
    IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    QueryAnalysisResultService queryAnalysisResultService;

    @Autowired
    ICameraService cameraService;

    @Resource
    private DownloadService downLoadService;

    /**
     * 聚类历史记录
     *
     * @param relineListRequest
     * @return
     */
    @ApiOperation("聚类历史记录")
    @PostMapping("/record/page")
    public R recordPage(@RequestBody @Valid ClusterRecordPageRequest relineListRequest) {
        IPage<TbClusterTask> pages = clusterService.recordPage(relineListRequest);
        return R.ok().put("page", new PageUtils(pages));
    }

    /**
     * 聚类操作
     *
     * @return
     */
    @ApiOperation("添加聚类任务")
    @PostMapping("/operate/execute")
    public R operateExecute(@RequestBody @Valid OperateVo operateVo) {
        String serianum = System.currentTimeMillis() + "";
        Date date = new Date();
        TbClusterTask tbClusterTask = new TbClusterTask();
        tbClusterTask.setId(serianum);
        tbClusterTask.setCameraIds(operateVo.getCameraIds());
        if (StringUtils.isNotEmpty(operateVo.getCameraIds())) {
            List<Camera> cameras = cameraService.list(new QueryWrapper<Camera>().in("id", operateVo.getCameraIds().split(",")));
            Set<String> camseraNames = new HashSet<>();
            cameras.forEach(camera -> camseraNames.add(camera.getName()));
            tbClusterTask.setCameraNames(String.join(",", camseraNames));
        } else {
            tbClusterTask.setCameraNames("全部");
        }
        tbClusterTask.setCreateTime(date);
        tbClusterTask.setObjType(operateVo.getObjType());
        tbClusterTask.setName(ObjTypeEnum.get(operateVo.getObjType()).getDesc() + "聚类:(" + operateVo.getStartTime() + "-" + operateVo.getEndTime() + ")");
        tbClusterTask.setStartTime(operateVo.getStartTime());
        tbClusterTask.setEndTime(operateVo.getEndTime());
        tbClusterTask.setThreshold(operateVo.getThreshold());
        tbClusterTask.setStatus(TaskStatusEnum.WAIT.getValue());

        List<VsdTaskRelation> vsdTaskRelations = vsdTaskRelationService.queryVsdTaskRelationByCameraId(operateVo.getCameraIds());
        List<FeatureVo> featureVos = new ArrayList<>();

        for (VsdTaskRelation vsdTaskRelation : vsdTaskRelations) {
            String taskId = vsdTaskRelation.getSerialnumber();
            List<FeatureVo> featureVo = getFeature(taskId, operateVo.getObjType(), operateVo.getStartTime(), operateVo.getEndTime(), vsdTaskRelation.getFromType(), DateUtil.formatDate(vsdTaskRelation.getEntryTime(), DateUtil.FORMAT_6));
            if (featureVo != null) {
                featureVos.addAll(featureVo);
            }
        }
        if (featureVos.isEmpty()) {
            return R.error("选择范围内无特征值");
        } else {
            clusterService.save(tbClusterTask);
        }
        ThreadTaskUtil.submit(() -> {
            clusterService.execute(serianum, featureVos, ObjTypeEnum.get(operateVo.getObjType()), new BigDecimal(operateVo.getThreshold()).divide(new BigDecimal(100)).floatValue());
        });
        return R.ok();
    }

    /**
     * 聚类任务删除
     *
     * @return
     */
    @ApiOperation("删除聚类任务")
    @PostMapping("/operate/delete")
    public R operateDelete(String id) {
        TbClusterTask tbClusterTask = clusterService.getById(id);
        if (tbClusterTask == null) {
            return R.error("任务不存在");
        } else {
            clusterDetailService.remove(new QueryWrapper<TbClusterTaskDetail>().eq("pid", id));
            clusterService.removeById(id);
        }
        return R.ok();
    }

    List<FeatureVo> getFeature(String taskId, Integer objType, String from, String to, Integer fromType, String entryTime) {
        DumpQuery dumpQuery = new DumpQuery();
        if (fromType == 2) {
            dumpQuery.setStartAt(entryTime);
        }
        dumpQuery.setFrom(from);
        dumpQuery.setTo(to);
        dumpQuery.setObjType(objType);
        dumpQuery.setTask(taskId);
        String featrue = featureSearchService.doDumpService(dumpQuery);
        JSONObject featrueJson = JSONObject.parseObject(featrue);
        List<FeatureVo> featureVos = new ArrayList<>();
        if (featrueJson.getInteger("error_code") != 0) {
            log.error("获取特征值失败:" + featrue);
        } else {
            featureVos = JSONObject.parseArray(featrueJson.getString("features"), FeatureVo.class);
        }
        return featureVos;
    }

    /**
     * 聚类结果
     *
     * @return
     */
    @ApiOperation("查询聚类结果")
    @PostMapping("/cluster/page")
    public R clusterPage(@RequestBody @Valid ClusterResultPageRequest clusterResultPageRequest) {
        TbClusterTask clusterTask = clusterService.getById(clusterResultPageRequest.getClusterId());
        if (clusterTask == null) {
            return R.error("任务不存在");
        }
        Page page = new Page(clusterResultPageRequest.getPage(), clusterResultPageRequest.getRows());
        IPage<TbClusterTaskDetail> clusterTaskDetails = clusterDetailService.
                page(page, new QueryWrapper<TbClusterTaskDetail>().eq("pid", clusterResultPageRequest.getClusterId()));
        List<TbClusterTaskDetail> details = clusterTaskDetails.getRecords();
        Page<ClusterTaskDetailVo> pages = new Page<>(clusterResultPageRequest.getPage(), clusterResultPageRequest.getRows());
        pages.setTotal(clusterTaskDetails.getTotal());
        List<ClusterTaskDetailVo> list = new ArrayList<>();
        for (TbClusterTaskDetail detail : details) {
            ClusterTaskDetailVo clusterTaskDetailVo = new ClusterTaskDetailVo();
            List<String> result = JSONArray.parseArray(detail.getResult(), String.class);
            String ids = String.join(",", result);
            Map<String, Object> pamaMap = new HashedMap();
            pamaMap.put("objType", clusterTask.getObjType());
            pamaMap.put("uuid", ids);
            pamaMap.put("rows", clusterResultPageRequest.getLateral());
            pamaMap.put("page", 1);
//            pamaMap.put("startTime", clusterTask.getStartTime());
//            pamaMap.put("endTime", clusterTask.getEndTime());
            String returnJson = queryAnalysisResultService.doHttpService(pamaMap);
            System.out.println(returnJson);
            List<ResultQueryVo> resultBoList = queryAnalysisResultService.transAllResultToBo(returnJson);

            clusterTaskDetailVo.setResults(resultBoList);
            clusterTaskDetailVo.setDetailId(detail.getId());
            list.add(clusterTaskDetailVo);
        }
        pages.setRecords(list);
        return R.ok().put("page", new PageUtils(pages));
    }

    /**
     * 聚类结果
     *
     * @return
     */
    @ApiOperation("查询聚类详情结果")
    @PostMapping("/cluster/detail/page")
    public R clusterDetailPage(@RequestBody @Valid ClusterDetailResultPageRequest clusterDetailResultPageRequest) {
        TbClusterTask clusterTask = clusterService.getById(clusterDetailResultPageRequest.getClusterId());
        if (clusterTask == null) {
            return R.error("任务不存在");
        }
        TbClusterTaskDetail clusterTaskDetail = clusterDetailService.
                getOne(new QueryWrapper<TbClusterTaskDetail>().eq("pid", clusterDetailResultPageRequest.getClusterId()).eq("id", clusterDetailResultPageRequest.getClusterDetailId()));
        Page<ClusterTaskDetailVo> pages = new Page<>(clusterDetailResultPageRequest.getPage(), clusterDetailResultPageRequest.getRows());
        List<ClusterTaskDetailVo> list = new ArrayList<>();
        ClusterTaskDetailVo clusterTaskDetailVo = new ClusterTaskDetailVo();
        List<String> result = JSONArray.parseArray(clusterTaskDetail.getResult(), String.class);
        String ids = String.join(",", result);
        Map<String, Object> pamaMap = new HashedMap();
        pamaMap.put("objType", clusterTask.getObjType());
        pamaMap.put("uuid", ids);
        pamaMap.put("rows", clusterDetailResultPageRequest.getRows());
        pamaMap.put("page", clusterDetailResultPageRequest.getPage());
        pamaMap.put("startTime", clusterTask.getStartTime());
        pamaMap.put("endTime", clusterTask.getEndTime());
        String returnJson = queryAnalysisResultService.doHttpService(pamaMap);
        System.out.println(returnJson);
        Map<String, Object> resultData = queryAnalysisResultService.dealResultData(returnJson, clusterTask.getObjType() + "", null);
        clusterTaskDetailVo.setResults((List<ResultQueryVo>) resultData.get("resultBoList"));
        clusterTaskDetailVo.setDetailId(clusterTaskDetail.getId());
        list.add(clusterTaskDetailVo);
        pages.setRecords(list);
        pages.setTotal(Integer.parseInt(resultData.get("totalNum").toString()));
        return R.ok().put("page", new PageUtils(pages));
    }

    @ApiOperation(value = "目标检索数据导出")
    @GetMapping(value = "/exportClusterData")
    public void exportClusterData(@Valid ClusterExportPageRequest clusterResultPageRequest, HttpServletResponse response) {

        TbClusterTask clusterTask = clusterService.getById(clusterResultPageRequest.getClusterId());
        if (clusterTask == null) {
            return;
        }
        if (clusterResultPageRequest.getPage() > clusterResultPageRequest.getEndPage()) {
            return;
        }
        String rootDir = FTPUtils.getRootPath("/");
        String resultFileDir = rootDir + "cluster_" + clusterTask.getId();
        for (int j = 0; j <= (clusterResultPageRequest.getEndPage() - clusterResultPageRequest.getPage()); j++) {
            Page page = new Page(clusterResultPageRequest.getPage() + j, clusterResultPageRequest.getRows());
            IPage<TbClusterTaskDetail> clusterTaskDetails = clusterDetailService.
                    page(page, new QueryWrapper<TbClusterTaskDetail>().eq("pid", clusterResultPageRequest.getClusterId()));
            List<TbClusterTaskDetail> details = clusterTaskDetails.getRecords();
            Page<ClusterTaskDetailVo> pages = new Page<>(clusterResultPageRequest.getPage(), clusterResultPageRequest.getRows());
            pages.setTotal(clusterTaskDetails.getTotal());
            List<ClusterTaskDetailVo> list = new ArrayList<>();
            for (int i = 0; i < details.size(); i++) {
                // 人
                String personDir = resultFileDir + File.separator + "聚类" + ((page.getCurrent() - 1) * page.getSize() + 1 + i);
                // 创建人 文件夹
                File personFile = new File(personDir);
                if (!personFile.exists()) {
                    personFile.mkdirs();
                }


                TbClusterTaskDetail detail = details.get(i);
                ClusterTaskDetailVo clusterTaskDetailVo = new ClusterTaskDetailVo();
                List<String> result = JSONArray.parseArray(detail.getResult(), String.class);
                String ids = String.join(",", result);
                Map<String, Object> pamaMap = new HashedMap();
                pamaMap.put("objType", clusterTask.getObjType());
                pamaMap.put("uuid", ids);
                pamaMap.put("startTime", clusterTask.getStartTime());
                pamaMap.put("endTime", clusterTask.getEndTime());
                String returnJson = queryAnalysisResultService.doHttpService(pamaMap);
                System.out.println(returnJson);
                List<ResultQueryVo> resultBoList = queryAnalysisResultService.transAllResultToBo(returnJson);
                for (ResultQueryVo resultQueryVo : resultBoList) {
                    String imageUrl = resultQueryVo.getImgurl();
                    String timeStr = resultQueryVo.getCreatetimeStr().replace(" ", "_").replaceAll(":", "-");
                    String suffix = ".jpg";
                    String fileOriginalName = null;
                    String[] picturUrlArray = imageUrl.split("/");
                    if (null != picturUrlArray && picturUrlArray.length > 0) {
                        fileOriginalName = picturUrlArray[picturUrlArray.length - 1];
                    }
                    if (StringUtils.isNotEmptyString(fileOriginalName) && fileOriginalName.contains(".")) {
                        suffix = fileOriginalName.substring(fileOriginalName.lastIndexOf('.'), fileOriginalName.length());
                    }
                    String newPicName = timeStr + "_" + resultQueryVo.getSerialnumber() + "_" + resultQueryVo.getId() + suffix;
                    String bikePicturePath = personFile.getPath() + File.separator + newPicName;
                    FileUtils.dowloadFileFromUrl(imageUrl, bikePicturePath);
                }
                clusterTaskDetailVo.setResults(resultBoList);
                clusterTaskDetailVo.setDetailId(detail.getId());
                list.add(clusterTaskDetailVo);
            }
        }


        // 压缩文件路径
        String zipFileDirName = resultFileDir + ".zip";
        File rarFile = new File(zipFileDirName);
        if (rarFile.exists()) {
            rarFile.delete();
        }
        // 生成压缩文件
        FileZipCompressorUtil fileZipUtil = new FileZipCompressorUtil(zipFileDirName);

        File resultFile = new File(resultFileDir);
        if (!resultFile.exists()) {
            resultFile.mkdirs();
        }

        fileZipUtil.compressExe(resultFileDir);
        // 删除excel和源图片文件
        try {
            File f = new File(resultFileDir);
            FileUtils.deleteFileOrDirector(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File zipFile = new File(zipFileDirName);
        if (StringUtils.isNotEmptyString(zipFileDirName)) {
            ExcelUtil.downloadZip(zipFile, response);
        }
    }
}
