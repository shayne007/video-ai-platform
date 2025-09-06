package com.keensense.task.factory.other;

import com.alibaba.fastjson.JSONObject;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.factory.AbstractTaskManager;
import com.keensense.task.util.VideoExceptionUtil;

import java.sql.Timestamp;

/**
 * @Description: 提供一个任务接口，但是添加的任务不分析，用于图片关联任务
 * @Author: wujw
 * @CreateDate: 2019/10/21 11:34
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class OtherTask extends AbstractTaskManager {

    @Override
    public void insertTask(JSONObject paramJson, String serialnumber, String type, String url) {
        this.insertTbAnalysisTask(paramJson, serialnumber,type, paramJson.getIntValue("taskType"), 1);
        TbAnalysisDetail detail = super.initDefaultDetail(serialnumber, url);
        tbAnalysisDetailMapper.insert(detail);
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        VsdTask vsdTask = initVsdTask("{}", serialnumber, createTime, createTime,TaskConstants.ANALY_TYPE_OTHER,TaskConstants.TASK_STATUS_WAIT);
        int count = vsdTaskMapper.insert(vsdTask);
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException(serialnumber + " insert vsdTask failed");
        }
    }
}
