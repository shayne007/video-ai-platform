package com.keensense.task.thread;

import com.keensense.common.config.SpringContext;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.mapper.TbAnalysisDetailMapper;
import com.keensense.task.schedule.PlatformTaskSchedule;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.TbAnalysisDetailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Arrays;

/**
 * @Description: 联网录像下载线程
 * @Author: wujw
 * @CreateDate: 2019/5/16 16:19
 * @Version: 1.1
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class PlatformDownloadThread implements Runnable {

    private TbAnalysisDetail detail;

    private TbAnalysisDetailMapper tbAnalysisDetailMapper = SpringContext.getBean(TbAnalysisDetailMapper.class);

    private static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    public PlatformDownloadThread(TbAnalysisDetail detail) {
        this.detail = detail;
    }

    @Override
    public void run() {
        log.info(String.format("[%s] platform download thread begin to execute ", this.detail.getId()));
//        String[] vasDownInfos = PatternUtil.getMatch(this.detail.getDownloadUrl(),
//                "^.*/name=([^&]+)&psw=([^&]+)&srvip=([^&]+)&srvport=([^&]+)&devid=([^&]+)&starttime=([^&]+)&endtime=([^&]+)&$");
        String[] vasDownInfos = new String[8];
        if (null == vasDownInfos || CollectionUtils.isEmpty(Arrays.asList(vasDownInfos))) {
            this.finished();
            return;
        }

        int downloadId = 0;
        String serverIp = nacosConfig.getVasIp();
        String port = nacosConfig.getVasPort();

//        ClientSocket conn = ClientSocket.getInstance(StringUtil.isNotNull(serverIp) ? serverIp : vasDownInfos[3],
//                StringUtil.isNotNull(port) ? StringUtil.getInteger(port) : StringUtil.getInteger(vasDownInfos[4]),
//                vasDownInfos[1], vasDownInfos[2]);
//        if (null == conn) {
//            // 初始化失败
//            String errMsg = StringUtils.isEmpty(nacosConfig.getAnalysisDownloadInitError()) ? "init vas connect failed." : nacosConfig.getAnalysisDownloadInitError();
//            addRetryCount(errMsg);
//            log.error(String.format("[%s] init vas connect failed. ", this.detail.getId()));
//            this.finished();
//            return;
//        }
        Integer devIdlength = nacosConfig.getAnalysisLength();
        // 初始化并获取与vas交互的下载id
        if (TaskConstants.OPERAT_TYPE_WATI == this.detail.getDownloadStatus()) {
//            downloadId = conn.startDownloadRec(vasDownInfos[5], com.loocme.sys.util.DateUtil.getDate(vasDownInfos[6]),
//                    com.loocme.sys.util.DateUtil.getDate(vasDownInfos[7]), devIdlength != null ? devIdlength : 32);
//            if (downloadId <= 0) {
//                String errMsg = StringUtils.isEmpty(nacosConfig.getAnalysisDownloadProcess()) ?
//                        "downloadId is " + downloadId : nacosConfig.getAnalysisDownloadProcess();
//                addRetryCount(errMsg);
//                log.error(String.format("[%s] platform download file failed. downloadId = %d", this.detail.getId(), downloadId));
//                this.finished();
//                return;
//            }
            this.detail.setDownloadId(downloadId + "");
            this.detail.setDownloadStatus(TaskConstants.OPERAT_TYPE_RUNNING);
            this.detail.setLastupdateTime(com.keensense.task.util.DateUtil.now());
            tbAnalysisDetailMapper.updateById(this.detail);
        } else if (TaskConstants.OPERAT_TYPE_RUNNING == this.detail.getDownloadStatus()) {
            downloadId = StringUtil.getInteger(this.detail.getDownloadId());
        }

        int pos = getProgess(conn, downloadId);
        if (pos >= TaskConstants.PROGRESS_MAX_VALUE) {
            String fileUrl = conn.getDownloadRecFPath(downloadId);
            downloadProgress(pos, fileUrl);
            log.info(String.format("[%s] platform download file success. file is [%s]. ", this.detail.getId(), fileUrl));
        } else {
            log.error(String.format("[%s] platform download file error . pos=%d ", this.detail.getId(), pos));
            String errMsg = StringUtils.isEmpty(nacosConfig.getAnalysisDownloadProcess()) ?
                    "download file pos is " + pos : nacosConfig.getAnalysisDownloadProcess();
            addRetryCount(errMsg);
        }
        this.finished();
    }

    /***
     * @description: 获取下载进度
     * @param conn   scoket连接对象
     * @param downloadId 下载ID
     * @return: int
     */
    private int getProgess(ClientSocket conn, int downloadId) {
        // 根据下载id获取下载状态和进度
        int pos = 0;
        long startDownTime = System.currentTimeMillis();
        while (pos >= 0 && pos < TaskConstants.PROGRESS_MAX_VALUE) {
            int timeout = nacosConfig.getAnalysisTimeOut();
            if (0 == pos && (System.currentTimeMillis() - startDownTime > timeout * 1000)) {
                log.info(String.format("[%s] platform download file progress=0 and readtimeout for [%d] ", this.detail.getId(), timeout));
                break;
            }

            try {
                Thread.sleep(nacosConfig.getAnalysisRetrySleep() * 1000L);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
            int nPos = conn.getDownloadRecProgress(downloadId);
            if (-101 != nPos) {
                pos = nPos;
            }
            log.info(String.format("[%s] platform download file progress=%d ", this.detail.getId(), nPos));

            downloadProgress(pos, "");
        }
        return pos;
    }

    private void addRetryCount(String msg) {
        int maxRetryCount = nacosConfig.getAnalysisRetryCount();
        detail.setDownloadRetry(detail.getDownloadRetry() + 1);
        if (detail.getDownloadRetry() > maxRetryCount) {
            detail = TbAnalysisDetailUtil.updateProgress(this.detail, TbAnalysisDetailUtil.DOWNLOAD, -1, null);
            detail.setDownloadStatus(TaskConstants.OPERAT_TYPE_FAILED);
            detail.setFinishTime(new Timestamp(System.currentTimeMillis()));
            // 超过重试次数，直接置为失败
            this.detail.setLastupdateTime(DateUtil.now());
            this.detail.setRemark("下载录像失败:" + msg);
            tbAnalysisDetailMapper.updateFailed(this.detail);
            return;
        }
        this.detail.setDownloadProgress(TaskConstants.OPERAT_TYPE_WATI);
        detail = TbAnalysisDetailUtil.updateProgress(this.detail, TbAnalysisDetailUtil.DOWNLOAD, 0, null);
        this.detail.setLastupdateTime(DateUtil.now());
        tbAnalysisDetailMapper.updateById(this.detail);
    }

    private void downloadProgress(int progress, String fileUrl) {
        detail = TbAnalysisDetailUtil.updateProgress(this.detail, TbAnalysisDetailUtil.DOWNLOAD, progress, null);
        if (TaskConstants.PROGRESS_MAX_VALUE <= progress) {
            String[] downloadFileInfos = PatternUtil.getMatch(fileUrl, "^.*(\\\\|/)([^/\\\\]+)#([0-9\\.]+)$");
            if (null == downloadFileInfos) {
                String errMsg = StringUtils.isEmpty(nacosConfig.getAnalysisDownloadPath()) ?
                        "下载路径名不合法-" + fileUrl : nacosConfig.getAnalysisDownloadPath();
                this.addRetryCount(errMsg);
                return;
            }
            this.detail.setDownloadStatus(TaskConstants.OPERAT_TYPE_SUCCESS);
            this.detail.setDownloadFile(fileUrl);
            if (nacosConfig.getAnalysisTransCodeSwitch()) {
                // 需要转码
                this.detail.setTranscodeUrl(downloadFileInfos[3] + ":" + downloadFileInfos[2]);
                this.detail.setTranscodeStatus(TaskConstants.OPERAT_TYPE_WATI);
                this.detail.setTranscodeProgress(0);
            } else {
                // 不需要转码
                this.detail.setAnalysisUrl("ftp://chiwailam:abcd1234!@" + downloadFileInfos[3] + "/" + downloadFileInfos[2]);
                this.detail.setAnalysisStatus(TaskConstants.OPERAT_TYPE_WATI);
                this.detail.setAnalysisProgress(0);
            }
        }
        this.detail.setDownloadProgress(progress);
        this.detail.setLastupdateTime(DateUtil.now());
        tbAnalysisDetailMapper.updateById(this.detail);
    }

    private void finished() {
        int currThread = PlatformTaskSchedule.finished(this.detail.getId());
        if (0 == currThread) {
            ClientSocket.destroyInstance();
        }
    }
}
