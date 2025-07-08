package com.keensense.densecrowd.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.densecrowd.entity.sys.CfgMemProps;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface ICfgMemPropsService extends IService<CfgMemProps> {

    String getWs2ServerIp();

    String getWs2ServerPort();

    String getW2TagServerPort();

    String getH5ServerIp();

    String getH5ServerPort();

    String getMonitorGroupLimit();

    String getFtpServerHttpurl();

    /**
     * 视频帧分析间距
     * @return
     */
    int getDetectionFrameSkipInterval();

    /**
     * 最大等候时间
     * @return
     */
    int getPushFrameMaxWaitTime();

    /**
     * 图像缩放倍数
     * @return
     */
    int getDetectionScaleFactor();

    /**
     * 是否输出热力图
     * @return
     */
    boolean getEnableDensityMapOutput();

    /**
     * 热力图比率
     * @return
     */
    float getHeatmapWeight();
}

