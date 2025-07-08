package com.keensense.admin.service.task;

import com.keensense.admin.vo.ResultQueryVo;

import java.util.List;


public interface DownloadService
{

    /**
     * 下载查询结果
     * @return String 文件路径
     */
    public String downloadTotalComprehensive(List<ResultQueryVo> resultList, String serialNum,Integer exportImgType);
    

}
