package com.keensense.densecrowd.service.task;

import com.keensense.common.platform.bo.video.CrowdDensity;

import java.util.*;


public interface DownloadService
{

    /**
     * 下载查询结果
     * @return String 文件路径
     */
    public String downloadTotalComprehensive(List<CrowdDensity> resultList, String serialNum);
    

}
