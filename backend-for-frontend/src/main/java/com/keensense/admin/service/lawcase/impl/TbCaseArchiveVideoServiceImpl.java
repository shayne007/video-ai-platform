package com.keensense.admin.service.lawcase.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.lawcase.TbCaseArchiveVideo;
import com.keensense.admin.entity.lawcase.TbCaseVideoDownload;
import com.keensense.admin.mapper.lawcase.TbCaseArchiveVideoMapper;
import com.keensense.admin.mapper.lawcase.TbCaseVideoDownloadMapper;
import com.keensense.admin.mapper.task.CameraMapper;
import com.keensense.admin.service.lawcase.ITbCaseArchiveVideoService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.CaseArchiveVideoVo;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service("tbCaseArchiveVideoService")
public class TbCaseArchiveVideoServiceImpl extends ServiceImpl<TbCaseArchiveVideoMapper, TbCaseArchiveVideo> implements ITbCaseArchiveVideoService {

    @Autowired
    private CameraMapper cameraMapper;
    @Autowired
    private TbCaseVideoDownloadMapper caseVideoDownloadMapper;

    @Override
    public List<CaseArchiveVideoVo> getCaseArchiveInfoVideoList(String caseCode) {
        return baseMapper.getCaseArchiveInfoVideoList(caseCode);
    }

    @Override
    public TbCaseArchiveVideo selectExistsArchiveVideo(TbCaseArchiveVideo caseArchiveVideo) {
        return baseMapper.selectExistsArchiveVideo(caseArchiveVideo);
    }

    /**
     * 同一个事务中操作
     * @param caseArchiveVideo
     * @return
     */
    @Override
    public int insertCaseArchiveVideo(TbCaseArchiveVideo caseArchiveVideo) {
        caseArchiveVideo.setDeleted(0);
        caseArchiveVideo.setVersion(0);

        String videoDownId = UUID.randomUUID().toString().replaceAll("-", "");
        caseArchiveVideo.setVideoDownloadId(videoDownId);

        int insert = baseMapper.insert(caseArchiveVideo);

        // 插入视频下载记录
        TbCaseVideoDownload caseVideoDownload = new TbCaseVideoDownload();
        Long cameraId = caseArchiveVideo.getCameraId();
        CameraVo camera = cameraMapper.selectByPrimaryKey(cameraId);
        String vasUrl = camera.getUrl();
        if(StringUtils.isNotEmptyString(vasUrl) && vasUrl.contains("vas://")){
            String startTime = DateTimeUtils.formatDate(caseArchiveVideo.getVideoStartTime(), DateFormatConst.YMDHMS);
            String endTime = DateTimeUtils.formatDate(caseArchiveVideo.getVideoEndTime(), DateFormatConst.YMDHMS);;

            String downLoadUrl = vasUrl + "starttime=" + startTime + "&endtime=" + endTime + "&";

            caseVideoDownload.setId(videoDownId);
            caseVideoDownload.setDownloadUrl(downLoadUrl);

            caseVideoDownload.setProgress(0);
            caseVideoDownload.setEntryTime(DateUtil.getFormat(caseArchiveVideo.getVideoStartTime(), DateFormatConst.YMDHMS_));

            caseVideoDownload.setDownloadUrl(downLoadUrl);
            caseVideoDownload.setDownloadStatus(1);
            caseVideoDownload.setDownloadProgress(0);
            caseVideoDownload.setDownloadRetry(0);
            caseVideoDownload.setCreateTime(new Date());

            caseVideoDownloadMapper.insert(caseVideoDownload);
        }

        return insert;
    }
}
