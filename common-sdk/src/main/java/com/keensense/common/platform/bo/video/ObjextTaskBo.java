package com.keensense.common.platform.bo.video;

import com.keensense.common.platform.enums.TaskTypeEnums;
import com.keensense.common.platform.enums.TypeEnums;
import lombok.Data;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 16:55 2019/7/17
 * @Version v0.1
 */
@Data
public class ObjextTaskBo {

    /**
     * 任务序列号	字符	64位	是	只能是英文、数字(必填)
     */
    private String serialnumber;
    /**
     * objext：全目标（包括行人，车辆，人脸，骑行）, summary: 视频浓缩  perosonDesitty：人群密度任务(必填)
     */
    TypeEnums type;

    /**
     * 视频类型(必填)
     */
    TaskTypeEnums taskType;


    /**
     * 录像任务必填[yyyy-MM-dd HH:mm:ss]
     */
    String startTime;

    /**
     * 录像任务必填[yyyy-MM-dd HH:mm:ss]
     */
    String endTime;

    /**
     * 视频路径支持实时流、在线视频、ftp、本地。见参数示例说明。(必填)
     */
    String url;

    /**
     * 分析场景	整数		否	默认为0.1:一般监控视频场景2：卡口、类卡口场景4：交通流量统计场景8：动态人脸场景16：混合行人及动态人脸
     */
    Integer scene;

    /**
     * 设备ID	字符	128	否
     */
    String deviceId;

    /**
     * 矫正时间	字符		否	视频文件根据矫正时间计算快照的输出时间[yyyy-MM-dd HH:mm:ss]
     */
    String entryTime;

    /**
     * 视频监控点	整数		否
     */
    String cameraId;

    /**
     * 任务名称	字符	255	否
     */
    String name;
    /**
     * 创建用户	字符	32	否
     */
    String userId;
    /**
     * 敏感度	整数		否	 取值范围0-5,越高越敏感, 默认为0
     */
    Integer sensitivity;

    /**
     * 物體最小Size	整数		否	默认为0，大于等于0
     */
    String objMinSize;

    /**
     * 最短Duration	整数		否	默认为500
     */
    String objMinTimeInMs;

    /**
     * 浓缩密度 默认为0,取值范围0-20
     */
    String density;

    /**
     * true-感兴趣，false-不感兴趣，默认为true
     */
    Boolean interested;

    /**
     * 感兴趣区	字符		否	见参数说明。
     * 例：["0.30625,0.17777,0.296875,0.6,0.5890625,0.6055,0.6609375,0.1555","0.30625,0.17777,0.296875,0.6,0.5890625,0.6055,0.6609375,0.1555"]
     */
    String udrVertices;

    /**
     * 画线
     * 例：["0,0.02109375,0.7111111111111111,0.79921875,0.9013888888888889"]
     */
    String tripWires;

    /**
     * 分辨率 取值范围1-4。默认为2
     */
    String outputDSFactor;

    String slaveIp;//	分析IP	字符	64	否	分析服务IP，多个以英文逗号”,”隔开，此参数用于zookeeper调度模块专用，其他模块不填
//    String param;//	扩展属性字段	JSON字符串		否	1、任务扩展参数1、感性区域、非感兴趣区域。 2、其他特定参数传递（需要定制）。见参数示例说明。

    Integer status;//状态
    Integer pageSize;
    Integer pageNo;
    String containName;
    Integer fromType;
    String cameraFileId;
    Long createuser;
    /**
     * 任务是否开启标注标志
     */
    Integer enablePartial;
    /**
     *独立输出人脸
     */
    Boolean enableIndependentFaceSnap;

    /**
     * 室内室外
     */
    Boolean enableBikeToHuman;

    String uuid;
}
