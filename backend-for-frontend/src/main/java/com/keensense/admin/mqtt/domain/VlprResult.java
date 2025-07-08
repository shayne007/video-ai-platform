package com.keensense.admin.mqtt.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 1400标准机动车类
 */
@Data
public class VlprResult extends Result implements Serializable {

    private static final long serialVersionUID = 582429875446445484L;

    /**
     * 车辆全局唯一标识
     */
    @JSONField(name= "MotorVehicleID")
    private String motorVehicleID;

    /**
     * 关联卡口编号
     */
    @JSONField(name= "TollgateID")
    private String tollgateID;

    /**
     * 近景照片：卡口相机所拍照片，自动采集必选，图像访问路径，采用 URI 命名规则
     */
    @JSONField(name= "StorageUrl1")
    private String storageUrl1;

    /**
     * 车牌照片
     */
    @JSONField(name= "StorageUrl2")
    private String storageUrl2;

    /**
     * 远景照片：全景相机所拍照片
     */
    @JSONField(name= "StorageUrl3")
    private String storageUrl3;

    /**
     * 合成图
     */
    @JSONField(name= "StorageUrl4")
    private String storageUrl4;

    /**
     * 缩略图
     */
    @JSONField(name= "StorageUrl5")
    private String storageUrl5;

    /**
     * 车道号：车辆行驶方向最左车道为 1，由左向右顺序编号
     */
    @JSONField(name= "LaneNo")
    private Integer laneNo;

    /**
     * 有无车牌
     */
    @JSONField(name= "HasPlate")
    private String hasPlate;

    /**
     * 号牌种类
     */
    @JSONField(name= "PlateClass")
    private String plateClass;

    /**
     * 车牌颜色：指号牌底色，取 ColorType 中部分值： 黑色，白色，黄色，蓝色，绿色
     */
    @JSONField(name= "PlateColor")
    private String plateColor;

    /**
     * 车牌号：各类机动车号牌编号车牌全部无法识别的以“无车牌”标识，部分未识别的每个字符以半角‘-’
     */
    @JSONField(name= "PlateNo")
    private String plateNo;

    /**
     * 挂车牌号：各类机动车挂车号牌编号
     */
    @JSONField(name= "PlateNoAttach")
    private String plateNoAttach;

    /**
     * 车牌描述：车牌框广告信息，包括车行名称，联系电话等
     */
    @JSONField(name= "PlateDescribe")
    private String plateDescribe;

    /**
     * 是否套牌
     */
    @JSONField(name= "IsDecked")
    private String isDecked;

    /**
     * 是否涂改
     */
    @JSONField(name= "IsAltered")
    private String isAltered;

    /**
     * 是否遮挡
     */
    @JSONField(name= "IsCovered")
    private String isCovered;

    /**
     * 行驶速度：单位千米每小时（km/h）
     */
    @JSONField(name= "Speed")
    private Double speed;

    /**
     * 行驶方向
     */
    @JSONField(name= "Direction")
    private String direction;

    /**
     * 行驶状态代码
     */
    @JSONField(name= "DrivingStatusCode")
    private String drivingStatusCode;

    /**
     * 车辆使用性质代码
     */
    @JSONField(name= "UsingPropertiesCode")
    private String usingPropertiesCode;

    /**
     * 车辆类型
     */
    @JSONField(name= "VehicleClass")
    private String vehicleClass;

    /**
     * 车辆品牌：被标注车辆的品牌
     */
    @JSONField(name= "VehicleBrand")
    private String vehicleBrand;

    /**
     * 车辆型号
     */
    @JSONField(name= "VehicleModel")
    private String vehicleModel;

    /**
     * 车辆年款
     */
    @JSONField(name= "VehicleStyles")
    private String vehicleStyles;

    /**
     * 车辆长度
     */
    @JSONField(name= "VehicleLength")
    private Integer vehicleLength;

    /**
     * 车辆宽度
     */
    @JSONField(name= "VehicleWidth")
    private Integer vehicleWidth;

    /**
     * 车辆高度
     */
    @JSONField(name= "VehicleHeight")
    private Integer vehicleHeight;

    /**
     * 车身颜色
     */
    @JSONField(name= "VehicleColor")
    private String vehicleColor;

    /**
     * 颜色深浅
     */
    @JSONField(name= "VehicleColorDepth")
    private String vehicleColorDepth;

    /**
     * 车前盖：对车前盖的描述
     */
    @JSONField(name= "VehicleHood")
    private String vehicleHood;

    /**
     * 车后盖：对车后盖的描述
     */
    @JSONField(name= "VehicleTrunk")
    private String vehicleTrunk;

    /**
     * 车轮
     */
    @JSONField(name= "VehicleWheel")
    private String vehicleWheel;

    /**
     * 车轮印花纹
     */
    @JSONField(name= "WheelPrintedPattern")
    private String wheelPrintedPattern;

    /**
     * 车窗：对车窗的描述
     */
    @JSONField(name= "VehicleWindow")
    private String vehicleWindow;

    /**
     * 车顶：对车顶的描述
     */
    @JSONField(name= "VehicleRoof")
    private String vehicleRoof;

    /**
     * 车门：对车门的描述
     */
    @JSONField(name= "VehicleDoor")
    private String vehicleDoor;

    /**
     * 车侧：对车侧面的描述，不包括门
     */
    @JSONField(name= "SideOfVehicle")
    private String sideOfVehicle;

    /**
     * 车厢：对车厢的描述
     */
    @JSONField(name= "CarOfVehicle")
    private String carOfVehicle;

    /**
     * 后视镜：对后视镜的描述
     */
    @JSONField(name= "RearviewMirror")
    private String rearviewMirror;

    /**
     * 底盘：对车底盘的描述
     */
    @JSONField(name= "VehicleChassis")
    private String vehicleChassis;

    /**
     * 遮挡：对车遮挡物的描述
     */
    @JSONField(name= "VehicleShielding")
    private String vehicleShielding;

    /**
     * 贴膜颜色
     */
    @JSONField(name= "FilmColor")
    private String filmColor;

    /**
     * 改装标志
     */
    @JSONField(name= "IsModified")
    private String isModified;

    /**
     * 撞痕信息
     */
    @JSONField(name= "HitMarkInfo")
    private String hitMarkInfo;

    /**
     * 车身描述：描述车身上的文字信息，或者车上载物信息
     */
    @JSONField(name= "VehicleBodyDesc")
    private String vehicleBodyDesc;

    /**
     * 车前部物品：当有多个时可用英文半角逗号分隔
     */
    @JSONField(name= "VehicleFrontItem")
    private String vehicleFrontItem;

    /**
     * 车前部物品描述：对车前部物品数量、颜色、种类等信息的描述
     */
    @JSONField(name= "DescOfFrontItem")
    private String descOfFrontItem;

    /**
     * 车后部物品：当有多个时可用英文半角逗号分隔
     */
    @JSONField(name= "VehicleRearItem")
    private String vehicleRearItem;

    /**
     * 车后部物品描述：对车后部物品数量、颜色、种类等信息的描述
     */
    @JSONField(name= "DescOfRearItem")
    private String descOfRearItem;

    /**
     * 车内人数：车辆内人员数量
     */
    @JSONField(name= "NumOfPassenger")
    private Integer numOfPassenger;

    /**
     * 经过时刻：卡口事件有效，过车时间
     */
    @JSONField(name= "PassTime", format = "yyyyMMddHHmmss")
    private String passTime;

    /**
     * 经过道路名称：车辆被标注时经过的道路名称
     */
    @JSONField(name= "NameOfPassedRoad")
    private String nameOfPassedRoad;

    /**
     * 是否可疑车
     */
    @JSONField(name= "IsSuspicious")
    private String isSuspicious;

    /**
     * 遮阳板状态：0：收起；1：放下
     */
    @JSONField(name= "Sunvisor")
    private Integer sunvisor;

    /**
     * 安全带状态：0：未系；1：有系
     */
    @JSONField(name= "SafetyBelt")
    private Integer safetyBelt;

    /**
     * 打电话状态：0：未打电话；1：打电话中
     */
    @JSONField(name= "Calling")
    private Integer calling;

    /**
     * 号牌识别可信度：整个号牌号码的识别可信度，以 0～100 数值表示
     */
    @JSONField(name= "PlateReliability")
    private String plateReliability;

    /**
     * 每位号牌号码可信度：号牌号码的识别可信度，以 0～100 数值表示
     */
    @JSONField(name= "PlateCharReliability")
    private String plateCharReliability;

    /**
     * 品牌标志识别可信度：车辆品牌标志可信度；以 0～100 之间数值表示百分比，数值越大可信度越高
     */
    @JSONField(name= "BrandReliability")
    private String brandReliability;

    // *****************data附加字段*****************

    /**
     *
     */
    @JSONField(name= "Crash")
    private Long crash;

    /**
     * 车辆归属地
     */
    @JSONField(name= "LicenseAttribution")
    private String licenseAttribution;

    /**
     * 是否是危化品车辆;1是0不是
     */
    @JSONField(name= "Danger")
    private Integer danger;

    /**
     * 位置信息
     */
    @JSONField(name= "LocationInfo")
    private String locationInfo;

    /**
     * 车标志,比如大众,本田等
     */
    @JSONField(name= "CarLogo")
    private String carLogo;

    /**
     * 副驾驶是否有系安全带;1有0没有
     */
    @JSONField(name= "SecondBelt")
    private Integer secondBelt;

    /**
     * 车身可信度
     */
    @JSONField(name= "VehicleConfidence")
    private Integer vehicleConfidence;

    /**
     * 天窗
     */
    @JSONField(name= "SunRoof")
    private Integer sunRoof;

    /**
     * 备用轮胎
     */
    @JSONField(name= "SpareTire")
    private Integer spareTire;

    /**
     * 行李架
     */
    @JSONField(name= "Rack")
    private Integer rack;

    /**
     * 与主驾驶关联的人脸ID
     */
    @JSONField(name= "FaceUUID1")
    private String faceUUID1;

    /**
     * 与副驾驶关联的人脸ID
     */
    @JSONField(name= "FaceUUID2")
    private String faceUUID2;


    // *****************kafka附加字段*****************

    /**
     * 主驾
     */
    @JSONField(name= "MainDriver")
    private String mainDriver;

    /**
     * 副驾
     */
    @JSONField(name= "CoDriver")
    private String coDriver;

    /**
     *
     */
    @JSONField(name= "HasCrash")
    private String hasCrash;

    /**
     *
     */
    @JSONField(name= "HasDanger")
    private String hasDanger;

    /**
     * 车辆类型编码
     */
    @JSONField(name= "TypeCode")
    private String typeCode;

    /**
     * 车身颜色编码
     */
    @JSONField(name= "ColorCode")
    private String colorCode;

    /**
     * 车窗标识物数目
     */
    @JSONField(name= "TagNum")
    private String tagNum;

    /**
     * 车窗贴位置
     */
    @JSONField(name= "Tags")
    private String tags;

    /**
     * 是否有遮阳板
     */
    @JSONField(name= "Sun")
    private String sun;

    /**
     * 是否有吊坠
     */
    @JSONField(name= "Drop")
    private String drop;

    /**
     * 是否有纸巾盒
     */
    @JSONField(name= "Paper")
    private String paper;

    /**
     * 车牌颜色编码
     */
    @JSONField(name= "PlateColorCode")
    private String plateColorCode;

    /**
     * 车牌类型id 参考配置文件vlpr_class_map.json
     */
    @JSONField(name= "PlateClassCode")
    private String plateClassCode;

    /**
     * 车牌位置
     */
    @JSONField(name= "PlateLocationInfo")
    private String  plateLocationInfo;

    /**
     * 车牌自信度
     */
    @JSONField(name= "PlateConfidence")
    private String plateConfidence;

    /**
     * 乘客信息
     */
    @JSONField(name= "Passengers")
    private String passengers;

    /**
     * 是否抽烟
     */
    @JSONField(name= "HasSmoke")
    private String hasSmoke;

    /**
     * 是否遮脸
     */
    @JSONField(name= "HasFaceCover")
    private String hasFaceCover;

    /**
     * 性别
     */
    @JSONField(name= "Sex")
    private String sex;

}
