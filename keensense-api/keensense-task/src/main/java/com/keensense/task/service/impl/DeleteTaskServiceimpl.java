package com.keensense.task.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.DeleteTaskConstants;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TaskCleanInterim;
import com.keensense.task.search.SearchHttp;
import com.keensense.task.service.IDeleteTaskService;
import com.keensense.task.service.ITaskCleanInterimService;
import com.keensense.task.service.IVsdSlaveService;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.DeleteTaskUtil;
import com.keensense.task.util.oldclean.MysqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Description: 删除服务接口
 * @Author: wujw
 * @CreateDate: 2019/11/18 11:06
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service
@Slf4j
public class DeleteTaskServiceimpl implements IDeleteTaskService {

    @Autowired
    private NacosConfig nacosConfig;
    @Autowired
    private IVsdSlaveService vsdSlaveService;
    @Autowired
    private ITaskCleanInterimService taskCleanInterimService;

    @Override
    public boolean getDataSpace() {
        String resultStr = SearchHttp.getDataSpace();
        log.info("get data space request = " + resultStr);
        JSONObject resultJson = JSON.parseObject(resultStr);
        JSONObject fastdfs = resultJson.getJSONObject("fastdfs");
        JSONObject es = resultJson.getJSONObject("es");
        JSONObject feature = resultJson.getJSONObject("featuresearch");
        Long fastdfsTotal = Optional.ofNullable(fastdfs).map(p -> p.getLong("total")).orElse(0L);
        Long fastdfsUsage = Optional.ofNullable(fastdfs).map(p -> p.getLong("usage")).orElse(0L);
        Long esTotal = Optional.ofNullable(es).map(p -> p.getLong("total")).orElse(0L);
        Long esUsage = Optional.ofNullable(es).map(p -> p.getLong("usage")).orElse(0L);
        Long featureTotal = Optional.ofNullable(feature).map(p -> p.getLong("total")).orElse(0L);
        Long featureUsage = Optional.ofNullable(feature).map(p -> p.getLong("usage")).orElse(0L);
        if (fastdfsTotal == 0 || esTotal == 0) {
            log.error("get data space error !");
        } else {
            int allowSurplus = nacosConfig.getCleanDiskValue();
            long fastdfsRate = (fastdfsTotal - fastdfsUsage) * 100 / fastdfsTotal;
            long esRate = (esTotal - esUsage) * 100 / esTotal;
            long featureRate = 0;
            if (featureTotal != 0) {
                featureRate = (featureTotal - featureUsage) * 100 / featureTotal;
            } else {
                featureRate = 100;
            }
            log.info("allowSurplus:" + allowSurplus + ", fastdfs rate = " + fastdfsRate + ", es rate = " + esRate + ", feature rate=" + featureRate);
            if (fastdfsRate < allowSurplus || esRate < allowSurplus || featureRate < allowSurplus) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getResultOrderByCreateTime(Map<String, Object> paramMap) {
        return SearchHttp.getResultOrderByCreateTime(paramMap);
    }

    @Override
    public boolean deleteEs(String serialnumber, String time, String analyType) {
        if (TaskConstants.ANALY_TYPE_DESITTY.equals(analyType)) {
            String resultStr = SearchHttp.deleteDensityData(DeleteTaskUtil.deleteTask(serialnumber, time));
            return deleteDataResult(resultStr);
        } else if (TaskConstants.ANALY_TYPE_TRAFFIC.equals(analyType)) {
            Map<String, Object> param = DeleteTaskUtil.deleteTask(serialnumber, time);
            String resultStr = SearchHttp.deleteVehicleflowrateData(param);
            if (deleteDataResult(resultStr)) {
                resultStr = SearchHttp.deleteViolationData(param);
                if (deleteDataResult(resultStr)) {
                    resultStr = SearchHttp.deleteEventData(param);
                    if (deleteDataResult(resultStr)) {
                        resultStr = SearchHttp.deleteData(DeleteTaskUtil.deleteTask(serialnumber, time));
                        return deleteDataResult(resultStr);
                    }
                }
            }
            return false;
        } else {
            String resultStr = SearchHttp.deleteData(DeleteTaskUtil.deleteTask(serialnumber, time));
            return deleteDataResult(resultStr);
        }
    }

    @Override
    public boolean addDeleteImageRecord(String serialnumber, String userSerialnumber, String analyType, String ymd, int taskType, int optSource) {
        if (MysqlUtil.isMysql()) {
            List<String> slaves = getSlaveIdList();
            List<TaskCleanInterim> interims = DeleteTaskUtil.getTaskCleanList(serialnumber, userSerialnumber
                    , analyType, ymd, taskType, DateUtil.now(), optSource, slaves);
            return taskCleanInterimService.insertBatch(interims);
        } else {
            TaskCleanInterim taskCleanInterim = new TaskCleanInterim(serialnumber, userSerialnumber, analyType, ymd,
                    taskType, DateUtil.now(), optSource, null);
            return taskCleanInterimService.insert(taskCleanInterim);
        }
    }

    @Override
    public boolean deleteImages(String serialnumber, String time, String analyType) {
        if (TaskConstants.ANALY_TYPE_DESITTY.equals(analyType)) {
            String resultStr = SearchHttp.deleteDensityDataImage(DeleteTaskUtil.deleteTask(serialnumber, time));
            return deleteDataResult(resultStr);
        } else if (TaskConstants.ANALY_TYPE_TRAFFIC.equals(analyType)) {
            String resultStr = SearchHttp.deleteFile(DeleteTaskUtil.deleteTask(serialnumber, time));
            return deleteDataResult(resultStr);
        } else {
            String resultStr = SearchHttp.deleteDataImage(DeleteTaskUtil.deleteTask(serialnumber, time));
            return deleteDataResult(resultStr);
        }
    }

    /***
     * @description: 判断调用视图库删除接口是否成功
     * @param reqesetResult 视图库返回结果
     * @return: boolean
     */
    private boolean deleteDataResult(String reqesetResult) {
        log.info("delete task request response = " + reqesetResult);
        Integer code = DeleteTaskUtil.getDeleteStatus(reqesetResult);
        if (DeleteTaskConstants.SUCCESS == code) {
            return true;
        } else {
            log.error("delete task failed! response : " + reqesetResult);
            return false;
        }
    }

    /***
     * @description: mysql模式下获取slaveId
     * @return: java.util.List<java.lang.String>
     */
    private List<String> getSlaveIdList() {
        if (MysqlUtil.isMysql()) {
            List<String> slaves = vsdSlaveService.getSlaveIdList();
            if (slaves.isEmpty()) {
                log.error("can not get slaveId info!");
            }
            return slaves;
        }
        return Collections.emptyList();
    }
}
