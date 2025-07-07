package com.keensense.task.schedule;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.constants.TcInterfaceConstants;
import com.keensense.task.constants.TransConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.mapper.TbAnalysisDetailMapper;
import com.keensense.task.service.ITbAnalysisTaskService;
import com.keensense.task.service.IVsdTaskService;
import com.keensense.task.thread.PlatformDownloadThread;
import com.keensense.task.util.TbAnalysisDetailUtil;
import com.keensense.task.util.TranscodeHttpUtils;
import com.loocme.sys.entities.FtpConnection;
import com.keensense.task.util.DateUtil;
import com.loocme.sys.util.FtpUtil;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.ThreadUtil;
import com.loocme.sys.util.ThreadUtil.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * @Description: 联网录像下载线程  (已弃用)
 * @Author: wujw
 * @CreateDate: 2019/5/16 16:19
 * @Version: 1.1
 * @Company: 长沙千视通智能科技有限公司
 */
@Component
@Slf4j
//@Deprecated
public class PlatformTaskSchedule {

    private static final AtomicInteger DOWNLOAD_CURR_THREAD = new AtomicInteger(0);
    private static final Map<String, Long> CURR_DOWNLOAD_IDMAP = new HashMap<>();
    private static byte[] downloadLock = new byte[1];

    private static final ExecutorService SERVICE = ThreadUtil.newFixedThreadPool(20);

    @Autowired
    private TbAnalysisDetailMapper tbAnalysisDetailMapper;
    @Autowired
    private IVsdTaskService vsdTaskService;

    @Autowired
    private ITbAnalysisTaskService tbAnalysisTaskService;
    @Autowired
    private NacosConfig nacosConfig;

    public static int finished(String detailId) {
        if (CURR_DOWNLOAD_IDMAP.containsKey(detailId)) {
            synchronized (downloadLock) {
                Long times = CURR_DOWNLOAD_IDMAP.remove(detailId);
                if (null != times && times != 0) {
                    return DOWNLOAD_CURR_THREAD.decrementAndGet();
                }
            }
        }
        return DOWNLOAD_CURR_THREAD.get();
    }

    /**
     * 弃用录像下载分析
     */
    @Deprecated
    //@Scheduled(cron = "31 * * * * ?")
    public int removeOutThreadId() {
        log.info("------downloadID is " + CURR_DOWNLOAD_IDMAP);
        Iterator<String> detailIdIt = CURR_DOWNLOAD_IDMAP.keySet().iterator();
        List<String> delIdList = new ArrayList<>();
        while (detailIdIt.hasNext()) {
            String detailId = detailIdIt.next();
            Long times = CURR_DOWNLOAD_IDMAP.get(detailId);
            if (null == times || times <= 0) {
                continue;
            }

            if (System.currentTimeMillis() - times > 120000) {
                delIdList.add(detailId);
            }
        }
        return delIdList.size();
    }

    /**
     * 弃用录像下载分析
     */
    @Deprecated
    //@Scheduled(cron = "0/3 * * * * ?")
    public void downloadFile() {
        // 获取需要进行平台下载的任务
        Set<String> keyId = CURR_DOWNLOAD_IDMAP.keySet();
        QueryWrapper<TbAnalysisDetail> wrapper = TbAnalysisDetailUtil.queryAnalysisDetailByStatus("download_status");
        if (!keyId.isEmpty()) {
            wrapper.notIn("id", keyId);
        }
        List<TbAnalysisDetail> detailList = tbAnalysisDetailMapper.selectList(wrapper);
        if (ListUtil.isNotNull(detailList)) {
            for (TbAnalysisDetail detail : detailList) {
                while (DOWNLOAD_CURR_THREAD.get() >= nacosConfig.getAnalysisThreadCount()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                }
                // 启动下载进程
                DOWNLOAD_CURR_THREAD.incrementAndGet();
                CURR_DOWNLOAD_IDMAP.put(detail.getId(), System.currentTimeMillis());
                SERVICE.execute(new PlatformDownloadThread(detail));
            }
        }
    }

    /**
     * 弃用录像下载分析
     */
    @Deprecated
    //@Scheduled(cron = "1/3 * * * * ?")
    public void transcodeFile() {
        // 获取需要进行转码的任务
        List<TbAnalysisDetail> detailList = tbAnalysisDetailMapper.selectList(
                TbAnalysisDetailUtil.queryAnalysisDetailByStatus("transcode_status"));
        for (TbAnalysisDetail detail : detailList) {
            // 调用转码模块
            String[] fileInfos = detail.getTranscodeUrl().split(":");
            if (TaskConstants.OPERAT_TYPE_WATI == detail.getTranscodeStatus()) {
                this.startTransCode(fileInfos, detail);
            } else {
                this.updateTransCodeProgress(fileInfos, detail);
            }
        }
    }

    /**
     * 弃用录像下载分析
     */
    @Deprecated
    //@Scheduled(cron = "0 6/10 * * * ?")
    public void deleteAnalysisFile() {
        // 获取已经分析完成或失败的任务
        Wrapper<TbAnalysisDetail> wrapper = new QueryWrapper<TbAnalysisDetail>().in("progress", -1, 100)
                .isNotNull("download_file").lt("download_status", 10)
                .lt("finish_time", new Timestamp(System.currentTimeMillis() - 10 * 60 * 1000));
        List<TbAnalysisDetail> detailList = tbAnalysisDetailMapper.selectList(wrapper);
        for (TbAnalysisDetail detail : detailList) {
            if (detail.getDownloadStatus() == TaskConstants.PROGRESS_FAILED) {
                // 下载失败，不处理
                continue;
            } else if (detail.getTranscodeProgress() == TaskConstants.PROGRESS_FAILED) {
                // 转码失败，删除下载文件
                String ftpUser = "chiwailam";
                String ftpPass = "abcd1234!";
                String[] downloadFileInfos = PatternUtil.getMatch(detail.getDownloadFile(), "^.*(\\\\|/)([^/\\\\]+)#([0-9\\.]+)$");
                deleteFtpFile(downloadFileInfos[3], ftpUser, ftpPass, "", downloadFileInfos[2]);
            } else {
                // 分析失败，删除文件
                String analyUrl = detail.getAnalysisUrl();
                if (analyUrl.startsWith("ftp")) {
                    // 通过ftp删除文件
                    String[] ftpInfos = getFtpParam(analyUrl);
                    deleteFtpFile(ftpInfos[3], ftpInfos[1], ftpInfos[2], ftpInfos[4], ftpInfos[5]);
                }
            }
            detail.setDownloadStatus(11);
            tbAnalysisDetailMapper.updateById(detail);
        }
    }

    @Scheduled(cron = "2/3 * * * * ?")
    public void analysisFile() {
        //處理vsd_task及tb_analysis_detail表数据
        vsdTaskService.insertVsdTask();
        //假如开启打点，根据打点更新录像分析主任务
        if(nacosConfig.getAnalysisTrackSwitch() == true){
            tbAnalysisTaskService.updateProgressByTracks();
        }
    }

    /***
     * @description: 删除ftp文件
     * @param server 服务IP
     * @param user 用户名
     * @param password 密码
     * @param path  文件夹路径
     * @param filePath 删除文件路径
     * @return: void
     */
    private void deleteFtpFile(String server, String user, String password, String path, String filePath) {
        FtpConnection ftpConn = null;
        try {
            ftpConn = FtpUtil.getInstance(server, 21, user, password, path);
            ftpConn.delete(filePath);
        } catch (Exception e) {
            log.error("deleteFtpFile failed", e);
        } finally {
            if (ftpConn != null) {
                ftpConn.close();
            }
        }
    }

    /***
     * @description: 获取ftp参数
     * @param url 路径
     * @return: java.lang.String[]
     */
    private String[] getFtpParam(String url) {
        return PatternUtil.getMatch(url, "^ftp://([^:]+):([^@]+)@(\\d+\\.\\d+\\.\\d+\\.\\d+)(.*)/([^/]+)$");
    }

    /***
     * @description: 开始转码
     * @param fileInfos 文件路径参数
     * @param detail 任务对象
     * @return: void
     */
    private void startTransCode(String[] fileInfos, TbAnalysisDetail detail) {
        // 添加转码
        LinkedHashMap<String, String> paramMap = new LinkedHashMap<>(2);
        paramMap.put("filename", fileInfos[1]);
        paramMap.put("video_type", "0");
        String addTransCodeTaskResponse = TranscodeHttpUtils.getHttp(fileInfos[0],
                TcInterfaceConstants.ADD_TRANSCODE_TASK, paramMap);
        log.info("添加转码返回报文：" + addTransCodeTaskResponse);
        String[] retArr = PatternUtil.getMatch(addTransCodeTaskResponse.replaceAll("\r\n", " "),
                "^.*\"ret\"[^:]*:(\\d+)[^,]*,.*\"id\"[^:]*:\\s*([a-zA-Z0-9]+)[^a-zA-Z0-9]*$",
                Pattern.CASE_INSENSITIVE);
        if (retArr != null && retArr.length == 3 && "0".equals(retArr[1])) {
            // 调用转码成功
            detail = TbAnalysisDetailUtil.updateProgress(detail, TbAnalysisDetailUtil.TRANSCODE, 0, null);
            detail.setLastupdateTime(DateUtil.now());
            detail.setTranscodeStatus(TaskConstants.OPERAT_TYPE_RUNNING);
            detail.setTranscodeId(retArr[2]);
            tbAnalysisDetailMapper.updateById(detail);
        } else {
            // 调用转码失败
            detail = TbAnalysisDetailUtil.updateProgress(detail, TbAnalysisDetailUtil.TRANSCODE, -1, null);
            Timestamp timestamp = DateUtil.now();
            detail.setLastupdateTime(timestamp);
            detail.setTranscodeStatus(TaskConstants.OPERAT_TYPE_FAILED);
            String errMsg = StringUtils.isEmpty(nacosConfig.getAnalysisTransCodeError()) ?
                    "添加转码 ret is " + (retArr == null ? "" : retArr[1]) : nacosConfig.getAnalysisDownloadInitError();
            detail.setRemark(errMsg);
            detail.setFinishTime(timestamp);
            tbAnalysisDetailMapper.updateFailed(detail);
        }
    }

    /***
     * @description: 更新进度
     * @param fileInfos 文件路径参数
     * @param detail 任务对象
     * @return: void
     */
    private void updateTransCodeProgress(String[] fileInfos, TbAnalysisDetail detail) {
        // 查询转码进度
        LinkedHashMap<String, String> paramMap = new LinkedHashMap<>(1);
        paramMap.put("id", detail.getTranscodeId());
        String addTransCodeTaskResponse = TranscodeHttpUtils.getHttp(fileInfos[0],
                TcInterfaceConstants.QUERY_TRANSCODE_STATUS, paramMap);
        addTransCodeTaskResponse = PatternUtil.getMatch(
                addTransCodeTaskResponse.replaceAll("\r\n", " "), "^[^{]*(\\{.*})[^}]*$",
                Pattern.CASE_INSENSITIVE, 1);
        log.info("获取转码进度返回报文：" + addTransCodeTaskResponse);
        JSONObject resultObj = TransConstants.getTransCodeResult(addTransCodeTaskResponse);
        if (resultObj != null) {
            Integer status = TransConstants.getTransStatus(resultObj);
            String orgVideosUrl = TransConstants.getTransFtpUrl(resultObj);
            detail.setLastupdateTime(DateUtil.now());
            if (TransConstants.STATUS_FAILED == status) {
                // 转码失败
                detail = TbAnalysisDetailUtil.updateProgress(detail, TbAnalysisDetailUtil.TRANSCODE, -1, null);
                detail.setTranscodeStatus(TaskConstants.OPERAT_TYPE_FAILED);
                String errMsg = StringUtils.isEmpty(nacosConfig.getAnalysisTransCodeError()) ?
                        "转码status is 3." : nacosConfig.getAnalysisDownloadInitError();
                detail.setRemark(errMsg);
                tbAnalysisDetailMapper.updateFailed(detail);
                return;
            } else if (TransConstants.STATUS_SUCCESS == status) {
                // 转码完成
                detail = TbAnalysisDetailUtil.updateProgress(detail, TbAnalysisDetailUtil.TRANSCODE
                        , TaskConstants.PROGRESS_MAX_VALUE, null);
                detail.setTranscodeStatus(TaskConstants.OPERAT_TYPE_SUCCESS);
                detail.setTranscodeUrl(orgVideosUrl);
                detail.setAnalysisUrl(orgVideosUrl);
                detail.setAnalysisProgress(0);
                detail.setAnalysisStatus(TaskConstants.OPERAT_TYPE_WATI);
            } else {
                // 转码中
                detail = TbAnalysisDetailUtil.updateProgress(detail, TbAnalysisDetailUtil.TRANSCODE,
                        TransConstants.getTransProgress(resultObj), null);
            }
            tbAnalysisDetailMapper.updateById(detail);
        }
    }
}
