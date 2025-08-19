package com.keensense.task.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.util.ResultUtils;
import com.keensense.task.async.util.MsgUtils;
import com.keensense.task.async.AsyncQueueConstants;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.service.IAnalysisTrackService;
import com.keensense.task.service.ITbAnalysisTaskService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AnalysisTrackController
 * @Description: 分析轨迹接口
 * @Author: cuiss
 * @CreateDate: 2020/4/8 11:52
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@ApiOperation("分析轨迹")
@RestController
@RequestMapping("/rest/analysisTrack")
@Slf4j
public class AnalysisTrackController extends BaseController{

    @Autowired
    private IAnalysisTrackService analysisTrackService;

    @Autowired
    private ITbAnalysisTaskService tbAnalysisTaskService;

    @Autowired
    private NacosConfig nacosConfig;

    /**
     * controller基础路径
     */
    private static final String BASE_URL = "/rest/analysisTrack";

    /**
     * 同步接口路径
     */
    private static final String SYNC_TRACK_URL = "/syncAnalysisTrack";

    /**
     * 查询接口路径
     */
    private static final String QUERY_TRACK_URL = "/getAnalysisTrack";

    /**
     * 查询监控点的serialnumner列表
     */
    private static final String SERIALNUMBER_TRACK_URL = "/getSerialnumberByCamera";

    private static final String TRANS_TRACK_TO_HIS = "/transAnalysisTracksToHis";


    @ApiOperation(value = "同步分析时间轨迹", notes = "同步分析时间轨迹")
    @PostMapping("/syncAnalysisTrack")
    public String syncAnalysisTrack(@RequestBody String body){
        try{
            JSONObject paramJson = super.getJsonByBody(body,BASE_URL+SYNC_TRACK_URL);
            JSONArray jsonArray = paramJson.getJSONArray("tracks");
            log.info(">>>>>>>>>> tracks size:{}",jsonArray.size());
            for(Object object:jsonArray){
                if(object instanceof JSONObject){
                    JSONObject jsonObject = (JSONObject)object;
                    String serialnumber = jsonObject.getJSONObject("custom").getString("serialnumber");
                    String cameraId = jsonObject.getJSONObject("custom").getString("cameraId");
                    String analysisTypes = jsonObject.getJSONObject("custom").getString("analysisTypes");
                    String trackStartTime = jsonObject.getString("trackStartTime").replaceAll(" ","").replaceAll(":","").replaceAll("-","");
                    String trackEndTime = jsonObject.getString("trackEndTime").replaceAll(" ","").replaceAll(":","").replaceAll("-","");
                    log.info(">>>>>>>serialnumber:{},cameraId:{},analysisTypes:{},trackStartTime:{},trackEndTime:{}",
                            serialnumber,cameraId,analysisTypes,trackStartTime,trackEndTime);

                    //开启打点分析之后才推送消息
                    if(nacosConfig.getAnalysisTrackSwitch()){
                        if(cameraId != null && analysisTypes != null){
                            String [] analysisTypeArray = analysisTypes.split(",");
                            for(String analysisType:analysisTypeArray){
                                Map<String,Object> msgMap = new HashMap<>();
                                msgMap.put("serialnumber",serialnumber);
                                msgMap.put("cameraId",cameraId);
                                msgMap.put("analysisTypes",analysisType);
                                msgMap.put("trackStartTime",trackStartTime);
                                msgMap.put("trackEndTime",trackEndTime);

                                byte[] payload = MsgUtils.buildMessage(msgMap);
                                MsgUtils.publish(AsyncQueueConstants.AYALYSIS_TRACK_QUEUE_NAME,payload);
                            }
                        }
                    }
                }else{
                    log.error(">>>>>invalid json Excetion...");
                }
            }
        }catch (Exception e){
            log.error(">>>>>param invalid Exception:"+e.getMessage());
        }

        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "获取分析打点数据", notes = "获取分析打点数据")
    @PostMapping(value = "/getAnalysisTrack")
    public String getAnalysisTrack(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+QUERY_TRACK_URL);
        JSONObject result = null;
        try {
            result = analysisTrackService.getTrackList(paramJson);
        } catch (Exception e) {
            log.error(">>>>>>>>> getAnalysisTrack Exception:{}",e.getMessage());
            e.printStackTrace();
            return ResultUtils.renderFailure(null);
        }
        return ResultUtils.returnSuccess(result);
    }

    @ApiOperation(value = "根据点位id查询serialnumber", notes = "根据点位id查询serialnumber")
    @PostMapping(value = "/getSerialnumberByCamera")
    public String getSerialnumberByCamera(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+SERIALNUMBER_TRACK_URL);
        JSONObject result = null;
        try {
            result = analysisTrackService.getSerialnumberByCameras(paramJson);
        } catch (Exception e) {
            log.error(">>>>>>>>> getAnalysisTrack Exception:{}",e.getMessage());
            e.printStackTrace();
            return ResultUtils.renderFailure(null);
        }
        return ResultUtils.returnSuccess(result);
    }

    @ApiOperation(value = "删除打点轨迹", notes = "删除打点轨迹")
    @PostMapping(value = "/transAnalysisTracksToHis")
    public String transAnalysisTracksToHis(@RequestBody String body){
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+TRANS_TRACK_TO_HIS);
        String serialnumber = paramJson.getString("serialnumber");
        String time = paramJson.getString("time");
        TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.getById(serialnumber);
        if(tbAnalysisTask != null){
            analysisTrackService.transAnalysisTracksToHis(tbAnalysisTask.getCameraId(),time);
        }else{
            analysisTrackService.transAnalysisTracksToHis(null,time);
        }
        return ResultUtils.returnSuccess(null);
    }

}
