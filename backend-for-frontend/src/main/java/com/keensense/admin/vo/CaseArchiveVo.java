package com.keensense.admin.vo;

import lombok.Data;

/**
 * 案件归档数据传输类
 */
@Data
public class CaseArchiveVo {

    /**
     * 图片类型
     */
    private String objtype;
    /**
     * 图片Id
     */
    private String resultId;
    /**
     * 案件编号
     */
    private String caseCode;

    /**
     * 图片名称
     */
    private String picName;
    /**
     * 图片发生状态 0事前 1事中 2事后
     */
    private String picState;

    /**
     * 视频名称
     */
    private String videoName;
    /**
     * 视频的发生状态 0事前 1事中 2事后
     */
    private String videoState;
    /**
     * 是否是证据视频 (0 否 1 是)
     */
    private String videoProof;
    /**
     * 视频开始时间
     */
    private String videoStartTime;
    /**
     * 视频结束时间
     */
    private String videoEndTime;

}
