package com.keensense.task.factory.objext.vedio;

import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.VasLice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/8/7 14:13
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class DownloadTask extends VideoTask {

    /***
     * @description: 获取分片任务对象
     * @param vasUrl vasUrl地址
     * @param serialnumber 任务ID
     * @param sliceList 分片时间对象
     * @param isWsVideo 是否是Master2.0视频
     * @return: java.util.List<com.keensense.task.entity.TbAnalysisDetail>
     */
    protected List<TbAnalysisDetail> getDetailList(String vasUrl, String serialnumber, List<VasLice> sliceList, Boolean isWsVideo) {
        List<TbAnalysisDetail> list;
        //判断是否重用已有分析结果
        if (nacosConfig.getAnalysisUseHistoryData()) {
            List<String> urlList = new ArrayList<>(sliceList.size());
            Map<String, TbAnalysisDetail> tbAnalysisDetailMap = new HashMap<>(sliceList.size());
            for (VasLice vas : sliceList) {
                String detailUrl = getUrl(vasUrl, vas, isWsVideo);
                TbAnalysisDetail detail = super.initTbAnalysisDetail(serialnumber);
                detail.setEntryTime(getEntryTime(vas.getStartSecond()));
                detail.setDownloadUrl(detailUrl);
                urlList.add(detailUrl);
                tbAnalysisDetailMap.put(detailUrl, detail);
            }
            list = getReuseDetailList(urlList, tbAnalysisDetailMap);
        } else {
            list = new ArrayList<>(sliceList.size());
            for (VasLice vas : sliceList) {
                String detailUrl = getUrl(vasUrl, vas, isWsVideo);
                TbAnalysisDetail detail = super.initTbAnalysisDetail(serialnumber);
                detail.setEntryTime(getEntryTime(vas.getStartSecond()));
                initDownloadInfo(detail, detailUrl);
                list.add(detail);
            }
        }
        return list;
    }

    /***
     * @description: 获取可以重用的分片任务
     * @param urlList url集合
     * @param tbAnalysisDetailMap url与detail映射关系Map
     * @return: java.util.List<com.keensense.task.entity.TbAnalysisDetail>
     */
    private List<TbAnalysisDetail> getReuseDetailList(List<String> urlList
            , Map<String, TbAnalysisDetail> tbAnalysisDetailMap) {
        List<TbAnalysisDetail> reUserList = tbAnalysisDetailMapper.getReuseList(urlList);
        List<TbAnalysisDetail> resultList = new ArrayList<>(tbAnalysisDetailMap.size());
        TbAnalysisDetail tbAnalysisDetail;
        if (!reUserList.isEmpty()) {
            for (TbAnalysisDetail analysisDetail : reUserList) {
                tbAnalysisDetail = tbAnalysisDetailMap.get(analysisDetail.getDownloadUrl());
                if (tbAnalysisDetail != null) {
                    tbAnalysisDetail.setProgress(TaskConstants.PROGRESS_REUSE);
                    tbAnalysisDetail.setAnalysisId(analysisDetail.getAnalysisId());
                    tbAnalysisDetail.setEntryTime(analysisDetail.getEntryTime());
                    resultList.add(tbAnalysisDetail);
                    tbAnalysisDetailMap.remove(analysisDetail.getDownloadUrl());
                }
            }
        }
        for (Map.Entry<String, TbAnalysisDetail> entry : tbAnalysisDetailMap.entrySet()) {
            tbAnalysisDetail = entry.getValue();
            initDownloadInfo(tbAnalysisDetail, entry.getKey());
            tbAnalysisDetail.setAnalysisId(UUID.randomUUID().toString().replaceAll("-", ""));
            resultList.add(tbAnalysisDetail);
        }
        return resultList;
    }

    /***
     * @description: detail表设置Download信息，联网录像下载使用
     * @param detail TbAnalysisDetail对象
     * @param url 下载url
     * @return:
     */
    private void initDownloadInfo(TbAnalysisDetail detail, String url) {
        detail.setDownloadUrl(url);
        detail.setDownloadStatus(1);
        detail.setDownloadProgress(0);
        detail.setDownloadRetry(0);
    }
}
