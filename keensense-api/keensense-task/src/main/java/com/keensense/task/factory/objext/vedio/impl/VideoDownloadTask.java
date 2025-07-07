package com.keensense.task.factory.objext.vedio.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.VasLice;
import com.keensense.task.entity.VasUrlEntity;
import com.keensense.task.factory.objext.vedio.DownloadTask;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.VideoExceptionUtil;

import java.util.List;

/**
 * @Description: 联网录像下载子类
 * @Author: wujw
 * @CreateDate: 2019/5/9 16:53
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class VideoDownloadTask extends DownloadTask {

    @Override
    public void insertTask(JSONObject paramJson, String serialnumber, String type, String url) {
        String entryTime = super.getEntryTime(paramJson);
        paramJson.put("param", getParamMap(paramJson, serialnumber, url, entryTime, TaskConstants.TASK_TYPE_VIDEO));
        VasUrlEntity vasUrlEntity = TaskParamValidUtil.getVasParamByUrl(url);
        List<VasLice> sliceList = getSliceList(vasUrlEntity);
        List<TbAnalysisDetail> insertDetailList = super.getDetailList(vasUrlEntity.getVasUrl(),serialnumber,sliceList, false);

        super.insertTbAnalysisTask(paramJson, serialnumber, TaskConstants.ANALY_TYPE_OBJEXT, TaskConstants.TASK_TYPE_VIDEO,insertDetailList.size());
        int count = tbAnalysisDetailMapper.insertBatch(insertDetailList);
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException(serialnumber + " insert TbAnalysisDetail failed!");
        }
    }

}
