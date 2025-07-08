package com.keensense.admin.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskParamVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String objMinSize;
    
    private String objMinTimeInMs;
    
    private String sensitivity;
    
    private String outputDSFactor;
    
    private String startIdx;
    
    private String endIdx;
    
    // 分析模式
    private String mode;
    
    // 目标类型
    private String targetTypes;

    // 参数类型 默认为0 自定义为1
    private String paramType;
    
    // 跳帧
    private String frameSkip;
    
    // 人脸质量阀值
    private String qualitythreshold;
    
    private  String paramTypeFace;
    
    // 0表示是不感兴趣区域  1 为感性趣
    private String  interest;
    
    // 兴趣区 节点字符串
    private String  featurFollowarea;
    
    
    
    private  String paramTypeVlpr;
    
    // 车辆任务 最大宽度
    private String maxWidthCar;
    
    // 最下宽度
    private String minWidthCar;
    
    // 0 车头 1 车尾
    private String detectModeCar;
    
    //  0 = 一般監控視頻場景, 1 = 卡口 / 類卡口場景
    private String scene;
    
    /**
     * 跨线参数
     */
    private String tripwiresParam;
}
