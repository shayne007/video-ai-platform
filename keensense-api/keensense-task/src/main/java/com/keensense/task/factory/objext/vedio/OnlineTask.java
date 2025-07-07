package com.keensense.task.factory.objext.vedio;

import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.VasLice;
import com.keensense.task.entity.VasUrlEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/8/7 14:13
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class OnlineTask extends VideoTask {

    /***
     * @description: 获取录像流下载分片子任务
     * @param serialnumber  任务ID
     * @param vasUrlEntity  分片参数对象
     * @param isWsVideo 是否是Master2.0视频
     * @return: java.util.List<com.keensense.task.entity.TbAnalysisDetail>
     */
    protected List<TbAnalysisDetail> getSliceDetail(String serialnumber, VasUrlEntity vasUrlEntity, Boolean isWsVideo) {
        List<VasLice> sliceList = super.getSliceList(vasUrlEntity);
        List<TbAnalysisDetail> insertDetailList = new ArrayList<>(sliceList.size());
        for (VasLice vas : sliceList) {
            String detailUrl = super.getUrl(vasUrlEntity.getVasUrl(), vas, isWsVideo);
            TbAnalysisDetail detail = super.initTbAnalysisDetail(serialnumber);
            detail.setEntryTime(super.getEntryTime(vas.getStartSecond()));
            detail.setAnalysisUrl(detailUrl);
            detail.setAnalysisId(detail.getId());
            detail.setAnalysisStatus(1);
            detail.setAnalysisProgress(0);
            insertDetailList.add(detail);
        }
        return insertDetailList;
    }
}
