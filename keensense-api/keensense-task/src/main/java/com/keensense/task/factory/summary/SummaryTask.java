package com.keensense.task.factory.summary;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.factory.AbstractTaskManager;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.VideoExceptionUtil;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @Description: 浓缩视频任务
 * @Author: wujw
 * @CreateDate: 2019/5/9 16:54
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class SummaryTask extends AbstractTaskManager {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTask(JSONObject paramJson, String serialnumber, String analyType, String url) {
        String entryTime = super.getEntryTime(paramJson);
        JSONObject param = getParamMap(paramJson, serialnumber, url, entryTime);
        param.put("url", url);
        paramJson.put("param", param);
        super.insertTbAnalysisTask(paramJson, serialnumber, analyType, TaskConstants.TASK_TYPE_SUMMARY, 1);
        TbAnalysisDetail detail = super.initDefaultDetail(serialnumber, null);
        tbAnalysisDetailMapper.insert(detail);
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        VsdTask vsdTask = initVsdTask(param.toString(), serialnumber, createTime, Timestamp.valueOf(entryTime), TaskConstants.ANALY_TYPE_SUMMARY, TaskConstants.TASK_STATUS_WAIT);
        int count = vsdTaskMapper.insert(vsdTask);
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException(serialnumber + " insert vsdTask failed");
        }
    }

    /***
     * @description: 获取param参数
     * @param paramJson 请求参数
     * @param url 视频路径
     * @param entryTime 矫正时间
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject getParamMap(JSONObject paramJson, String serialnumber, String url, String entryTime) {
        JSONObject paramRoot = initParamRoot(paramJson, serialnumber, url, entryTime);
        paramRoot.put("analysisCfg", getAnalysisCfgMap(paramJson));
        paramRoot.put("summaryCfg", getSummaryCfg(paramJson));
        return paramRoot;
    }

    /****
     * @description: 获取浓缩配置
     * @param paramJson 请求参数
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject getSummaryCfg(JSONObject paramJson) {
        JSONObject summaryCfg = new JSONObject();
        summaryCfg.put("tripArea", new ArrayList<>(0));

        long density = TaskParamValidUtil.getDensity(paramJson, "density", 0);
        summaryCfg.put("density", density);

        String tripWires = "tripWires";
        JSONArray tripWiresArr = paramJson.getJSONArray(tripWires);
        tripWiresArr = TaskParamValidUtil.getTripWires(tripWiresArr);
        summaryCfg.put(tripWires, tripWiresArr);
        paramJson.remove(tripWires);
        return summaryCfg;
    }

    /***
     * @description: 获取analysisCfgMap参数
     * @param paramJson 请求参数
     * @return: com.alibaba.fastjson.JSONObject
     */
    @Override
    protected JSONObject getAnalysisCfgMap(JSONObject paramJson) {
        JSONObject analysisCfgMap = super.getAnalysisCfgMap(paramJson);

        int dominantColor = this.getDominantColor(paramJson);
        analysisCfgMap.put(TaskConstants.ANALYSIS_CFG_DOMINANTCOLOR, dominantColor);
        return analysisCfgMap;
    }

    private int getDominantColor(JSONObject paramJson) {
        int dominantColor = TaskParamValidUtil.getDominantColor(paramJson, TaskConstants.ANALYSIS_CFG_DOMINANTCOLOR, 0);
        paramJson.remove(TaskConstants.ANALYSIS_CFG_DOMINANTCOLOR);
        return dominantColor;
    }
}
