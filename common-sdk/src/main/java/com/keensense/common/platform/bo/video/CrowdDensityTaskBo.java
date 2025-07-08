package com.keensense.common.platform.bo.video;

import lombok.Data;

/**
 * @Author: zengyc
 * @Description: 人群密度重要字段
 * @Date: Created in 9:39 2019/11/8
 * @Version v0.1
 */
@Data
public class CrowdDensityTaskBo extends ObjextTaskBo {
    /**
     * 视频帧分析间距，在分析单一视频帧后，会忽略此数目的视频帧不作分析
     * 默认25*6 帧
     * 取值范围[0.2000],默认为0
     */
    int detectionFrameSkipInterval = 150;

    /**
     * 最大等候时间(ms), 长于此时间视频帧仍未分析，则开始丢弃该帧
     * 取值范围[0.10000],默认为0
     */
    int pushFrameMaxWaitTime = 0;

    /**
     * 图像缩放倍数
     * 取值范围[1,4]，默认为1
     */
    int detectionScaleFactor = 1;

    /**
     * 是否输出热力图
     * 默认false
     */
    boolean enableDensityMapOutput = true;

    /**
     * 热力图比率
     * 取值范围[0,1]认0.4
     */
    float heatmapWeight = 0.4f;
}
