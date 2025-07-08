package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.keensense.search.tool_interface.ParameterCheck;
import com.keensense.search.utils.ParametercheckUtil;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 1400标准机动车类
 */
@Data
@Table(name = "vlpr_result")
@ToString(callSuper=true, includeFieldNames=true)
public class VlprResult extends Result implements Serializable, ParameterCheck {

    private static final long serialVersionUID = 346567457457474L;

    /**
     * 车辆全局唯一标识
     */
    @JSONField(name= "MotorVehicleID")
    @Column(name = "motorvehicleid")
    private String motorVehicleID;

    /**
     * 关联卡口编号
     */
    @JSONField(name= "TollgateID")
    @Column(name = "tollgateid")
    private String tollgateID;

    /**
     * 近景照片：卡口相机所拍照片，自动采集必选，图像访问路径，采用 URI 命名规则
     */
    @JSONField(name= "StorageUrl1")
    @Column(name = "storageurl1")
    private String storageUrl1;

    /**
     * 车牌照片
     */
    @JSONField(name= "StorageUrl2")
    @Column(name = "storageurl2")
    private String storageUrl2;

    /**
     * 远景照片：全景相机所拍照片
     */
    @JSONField(name= "StorageUrl3")
    @Column(name = "storageurl3")
    private String storageUrl3;

    /**
     * 合成图
     */
    @JSONField(name= "StorageUrl4")
    @Column(name = "storageurl4")
    private String storageUrl4;

    /**
     * 缩略图
     */
    @JSONField(name= "StorageUrl5")
    @Column(name = "storageurl5")
    private String storageUrl5;

    /**
     * 车道号：车辆行驶方向最左车道为 1，由左向右顺序编号
     */
    @JSONField(name= "LaneNo")
    @Column(name = "laneno")
    private Integer laneNo;

    /**
     * 有无车牌
     */
    @JSONField(name= "HasPlate")
    @Column(name = "hasplate")
    private Integer hasPlate;

    /**
     * 号牌种类
     */
    @JSONField(name= "PlateClass")
    @Column(name = "plateclass")
    private String plateClass;

    /**
     * 车牌颜色：指号牌底色，取 ColorType 中部分值： 黑色，白色，黄色，蓝色，绿色
     */
    @JSONField(name= "PlateColor")
    @Column(name = "platecolor")
    private String plateColor;

    /**
     * 车牌号：各类机动车号牌编号车牌全部无法识别的以“无车牌”标识，部分未识别的每个字符以半角‘-’
     */
    @JSONField(name= "PlateNo")
    @Column(name = "plateno")
    private String plateNo;

    /**
     * 挂车牌号：各类机动车挂车号牌编号
     */
    @JSONField(name= "PlateNoAttach")
    @Column(name = "platenoattach")
    private String plateNoAttach;

    /**
     * 车牌描述：车牌框广告信息，包括车行名称，联系电话等
     */
    @JSONField(name= "PlateDescribe")
    @Column(name = "platedescribe")
    private String plateDescribe;

    /**
     * 是否套牌
     */
    @JSONField(name= "IsDecked")
    @Column(name = "isdecked")
    private Integer isDecked;

    /**
     * 是否涂改
     */
    @JSONField(name= "IsAltered")
    @Column(name = "isaltered")
    private Integer isAltered;

    /**
     * 是否遮挡
     */
    @JSONField(name= "IsCovered")
    @Column(name = "iscovered")
    private Integer isCovered;

    /**
     * 行驶速度：单位千米每小时（km/h）
     */
    @JSONField(name= "Speed")
    @Column(name = "speed")
    private Double speed;

    /**
     * 行驶方向
     */
    @JSONField(name= "Direction")
    @Column(name = "direction")
    private String direction;

    /**
     * 行驶状态代码
     */
    @JSONField(name= "DrivingStatusCode")
    @Column(name = "drivingstatuscode")
    private String drivingStatusCode;

    /**
     * 车辆使用性质代码
     */
    @JSONField(name= "UsingPropertiesCode")
    @Column(name = "usingpropertiescode")
    private Integer usingPropertiesCode;

    /**
     * 车辆类型
     */
    @JSONField(name= "VehicleClass")
    @Column(name = "vehicleclass")
    private String vehicleClass;

    /**
     * 车辆品牌：被标注车辆的品牌
     */
    @JSONField(name= "VehicleBrand")
    @Column(name = "vehiclebrand")
    private String vehicleBrand;

    /**
     * 车辆型号
     */
    @JSONField(name= "VehicleModel")
    @Column(name = "vehiclemodel")
    private String vehicleModel;

    /**
     * 车辆年款
     */
    @JSONField(name= "VehicleStyles")
    @Column(name = "vehiclestyles")
    private String vehicleStyles;

    /**
     * 车辆长度
     */
    @JSONField(name= "VehicleLength")
    @Column(name = "vehiclelength")
    private Integer vehicleLength;

    /**
     * 车辆宽度
     */
    @JSONField(name= "VehicleWidth")
    @Column(name = "vehiclewidth")
    private Integer vehicleWidth;

    /**
     * 车辆高度
     */
    @JSONField(name= "VehicleHeight")
    @Column(name = "vehicleheight")
    private Integer vehicleHeight;

    /**
     * 车身颜色
     */
    @JSONField(name= "VehicleColor")
    @Column(name = "vehiclecolor")
    private String vehicleColor;

    /**
     * 颜色深浅
     */
    @JSONField(name= "VehicleColorDepth")
    @Column(name = "vehiclecolordepth")
    private String vehicleColorDepth;

    /**
     * 车前盖：对车前盖的描述
     */
    @JSONField(name= "VehicleHood")
    @Column(name = "vehiclehood")
    private String vehicleHood;

    /**
     * 车后盖：对车后盖的描述
     */
    @JSONField(name= "VehicleTrunk")
    @Column(name = "vehicletrunk")
    private String vehicleTrunk;

    /**
     * 车轮
     */
    @JSONField(name= "VehicleWheel")
    @Column(name = "vehiclewheel")
    private String vehicleWheel;

    /**
     * 车轮印花纹
     */
    @JSONField(name= "WheelPrintedPattern")
    @Column(name = "wheelprintedpattern")
    private String wheelPrintedPattern;

    /**
     * 车窗：对车窗的描述
     */
    @JSONField(name= "VehicleWindow")
    @Column(name = "vehiclewindow")
    private String vehicleWindow;

    /**
     * 车顶：对车顶的描述
     */
    @JSONField(name= "VehicleRoof")
    @Column(name = "vehicleroof")
    private String vehicleRoof;

    /**
     * 车门：对车门的描述
     */
    @JSONField(name= "VehicleDoor")
    @Column(name = "vehicledoor")
    private String vehicleDoor;

    /**
     * 车侧：对车侧面的描述，不包括门
     */
    @JSONField(name= "SideOfVehicle")
    @Column(name = "sideOfvehicle")
    private String sideOfVehicle;

    /**
     * 车厢：对车厢的描述
     */
    @JSONField(name= "CarOfVehicle")
    @Column(name = "carOfvehicle")
    private String carOfVehicle;

    /**
     * 后视镜：对后视镜的描述
     */
    @JSONField(name= "RearviewMirror")
    @Column(name = "rearviewmirror")
    private String rearviewMirror;

    /**
     * 底盘：对车底盘的描述
     */
    @JSONField(name= "VehicleChassis")
    @Column(name = "vehiclechassis")
    private String vehicleChassis;

    /**
     * 遮挡：对车遮挡物的描述
     */
    @JSONField(name= "VehicleShielding")
    @Column(name = "vehicleshielding")
    private String vehicleShielding;

    /**
     * 贴膜颜色
     */
    @JSONField(name= "FilmColor")
    @Column(name = "filmcolor")
    private String filmColor;

    /**
     * 改装标志
     */
    @JSONField(name= "IsModified")
    @Column(name = "ismodified")
    private Integer isModified;

    /**
     * 撞痕信息
     */
    @JSONField(name= "HitMarkInfo")
    @Column(name = "hitmarkinfo")
    private String hitMarkInfo;

    /**
     * 车身描述：描述车身上的文字信息，或者车上载物信息
     */
    @JSONField(name= "VehicleBodyDesc")
    @Column(name = "vehiclebodydesc")
    private String vehicleBodyDesc;

    /**
     * 车前部物品：当有多个时可用英文半角逗号分隔
     */
    @JSONField(name= "VehicleFrontItem")
    @Column(name = "vehiclefrontitem")
    private String vehicleFrontItem;

    /**
     * 车前部物品描述：对车前部物品数量、颜色、种类等信息的描述
     */
    @JSONField(name= "DescOfFrontItem")
    @Column(name = "descoffrontitem")
    private String descOfFrontItem;

    /**
     * 车后部物品：当有多个时可用英文半角逗号分隔
     */
    @JSONField(name= "VehicleRearItem")
    @Column(name = "vehiclerearitem")
    private String vehicleRearItem;

    /**
     * 车后部物品描述：对车后部物品数量、颜色、种类等信息的描述
     */
    @JSONField(name= "DescOfRearItem")
    @Column(name = "descofrearitem")
    private String descOfRearItem;

    /**
     * 车内人数：车辆内人员数量
     */
    @JSONField(name= "NumOfPassenger")
    @Column(name = "numofpassenger")
    private Integer numOfPassenger;

    /**
     * 经过时刻：卡口事件有效，过车时间
     */
    @JSONField(name= "PassTime", format = "yyyyMMddHHmmss")
    @Column(name = "passtime")
    private Date passTime;

    /**
     * 经过道路名称：车辆被标注时经过的道路名称
     */
    @JSONField(name= "NameOfPassedRoad")
    @Column(name = "nameofpassedroad")
    private String nameOfPassedRoad;

    /**
     * 是否可疑车
     */
    @JSONField(name= "IsSuspicious")
    @Column(name = "issuspicious")
    private Integer isSuspicious;

    /**
     * 遮阳板状态：0：收起；1：放下
     */
    @JSONField(name= "Sunvisor")
    @Column(name = "sunvisor")
    private Integer sunvisor;

    /**
     * 安全带状态：0：未系；1：有系
     */
    @JSONField(name= "SafetyBelt")
    @Column(name = "safetybelt")
    private Integer safetyBelt;

    /**
     * 打电话状态：0：未打电话；1：打电话中
     */
    @JSONField(name= "Calling")
    @Column(name = "calling")
    private Integer calling;

    /**
     * 号牌识别可信度：整个号牌号码的识别可信度，以 0～100 数值表示
     */
    @JSONField(name= "PlateReliability")
    @Column(name = "platereliability")
    private Integer plateReliability;

    /**
     * 每位号牌号码可信度：号牌号码的识别可信度，以 0～100 数值表示
     */
    @JSONField(name= "PlateCharReliability")
    @Column(name = "platecharreliability")
    private String plateCharReliability;

    /**
     * 品牌标志识别可信度：车辆品牌标志可信度；以 0～100 之间数值表示百分比，数值越大可信度越高
     */
    @JSONField(name= "BrandReliability")
    @Column(name = "brandreliability")
    private String brandReliability;

    // *****************data附加字段*****************

    /**
     * 车辆归属地
     */
    @JSONField(name= "LicenseAttribution")
    @Column(name = "licenseattribution")
    private String licenseAttribution;

    /**
     * 位置信息
     */
    @JSONField(name= "LocationInfo")
    @Column(name = "locationinfo")
    private String locationInfo;

    /**
     * 车标志,比如大众,本田等
     */
    @JSONField(name= "CarLogo")
    @Column(name = "carlogo")
    private String carLogo;

    /**
     * 副驾驶是否有系安全带;1有0没有
     */
    @JSONField(name= "SecondBelt")
    @Column(name = "secondbelt")
    private Integer secondBelt;

    /**
     * 车身可信度
     */
    @JSONField(name= "VehicleConfidence")
    @Column(name = "vehicleconfidence")
    private Integer vehicleConfidence;

    /**
     * 天窗
     */
    @JSONField(name= "SunRoof")
    @Column(name = "sunroof")
    private Integer sunRoof = 0;

    /**
     * 备用轮胎
     */
    @JSONField(name= "SpareTire")
    @Column(name = "sparetire")
    private Integer spareTire;

    /**
     * 行李架
     */
    @JSONField(name= "Rack")
    @Column(name = "rack")
    private Integer rack = 0;

    /**
     * 与主驾驶关联的人脸ID
     */
    @JSONField(name= "FaceUUID1")
    @Column(name = "faceuuid1")
    private String faceUUID1;

    /**
     * 与副驾驶关联的人脸ID
     */
    @JSONField(name= "FaceUUID2")
    @Column(name = "faceuuid2")
    private String faceUUID2;


    // *****************kafka附加字段*****************

    /**
     * 主驾
     */
    @JSONField(name= "MainDriver")
    @Column(name = "maindriver")
    private String mainDriver;

    /**
     * 副驾
     */
    @JSONField(name= "CoDriver")
    @Column(name = "codriver")
    private String coDriver;

    /**
     *
     */
    @JSONField(name= "HasCrash")
    @Column(name = "hascrash")
    private Integer hasCrash;

    /**
     * 是否是危化品车辆;1是0不是
     */
    @JSONField(name= "HasDanger")
    @Column(name = "hasdanger")
    private Integer hasDanger;

    /**
     * 车窗标识物数目
     */
    @JSONField(name= "TagNum")
    @Column(name = "tagnum")
    private Integer tagNum;

    /**
     * 车窗贴位置
     */
    @JSONField(name= "Tags")
    @Column(name = "tags")
    private String tags;

    /**
     * 车牌位置
     */
    @JSONField(name= "PlateLocationInfo")
    @Column(name = "platelocationinfo")
    private String  plateLocationInfo;

    /**
     * 乘客信息
     */
    @JSONField(name= "Passengers")
    @Column(name = "passengers")
    private String passengers;

    /**
     * 是否抽烟
     */
    @JSONField(name= "HasSmoke")
    @Column(name = "hassmoke")
    private Integer hasSmoke;

    /**
     * 是否遮脸
     */
    @JSONField(name= "HasFaceCover")
    @Column(name = "hasfacecover")
    private Integer hasFaceCover;

    /**
     * 性别
     */
    @JSONField(name= "Sex")
    @Column(name = "sex")
    private String sex;


    //****************************************7月30日新增字段
    @JSONField(name= "Tag")
    @Column(name = "tag")
    private Integer tag = 0;

    /**
     * 是否有纸巾盒
     */
    @JSONField(name= "Paper")
    @Column(name = "paper")
    private Integer paper = 0;

    /**
     * 是否有遮阳板
     */
    @JSONField(name= "Sun")
    @Column(name = "sun")
    private Integer sun = 0;

    /**
     * 是否有吊坠
     */
    @JSONField(name= "Drop")
    @Column(name = "drop")
    private Integer drop = 0;

    @JSONField(name= "Aerial")
    @Column(name = "aerial")
    private Integer aerial = 0;

    @JSONField(name= "Decoration")
    @Column(name = "decoration")
    private Integer decoration = 0;

    @JSONField(name= "CoSunvisor")
    @Column(name = "cosunvisor")
    private Integer coSunvisor = 0;

    //****************************************交通新增字段
    /**
     * 是否为渣土车 0-不是 1-是
     */
    @JSONField(name = "SlagFlag")
    private  String slagFlag;

    /**
     * 主安全带可信度
     */
    @JSONField(name = "MainDriverConfidence")
    private  String mainDriverConfidence;

    /**
     * 副安全带可信度
     */
    @JSONField(name = "CoDriverConfidence")
    private  String coDriverConfidence;

    /**
     * 打电话可信度
     */
    @JSONField(name = "CallConfidence")
    private String callConfidence;

    @JSONField(name= "Angle")
    @Column(name = "angle")
    private Integer angle;

    @Override
    public void checkParameter() {
        ParametercheckUtil.checkEmpty("MotorVehicleID", motorVehicleID);
        ParametercheckUtil.checkEmpty("MarkTime", markTime);
        ParametercheckUtil.checkEmpty("Serialnumber", serialnumber);
        ParametercheckUtil.checkEmpty("SubImageList", subImageList);
        ParametercheckUtil.checkEmpty("FeatureOject", featureObject);
    }
}
