package com.keensense.common.platform.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 1400标准非机动车类
 */
@Data
public class NonMotorVehiclesResult extends Result implements java.io.Serializable {

    private static final long serialVersionUID = 13543253463463L;

    /**
     * 车辆标识
     */
    @JSONField(name= "NonMotorVehicleID")
    private String nonMotorVehicleID;

    /**
     * 有无车牌
     */
    @JSONField(name= "HasPlate")
    private Short hasPlate;

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
     * 车辆品牌：被标注车辆的品牌
     */
    @JSONField(name= "VehicleBrand")
    private String vehicleBrand;

    /**
     * 车辆款型：被标注车辆的款式型号描述
     */
    @JSONField(name= "VehicleType")
    private String vehicleType;

    /**
     * 车辆长度
     */
    @JSONField(name= "VehicleLength")
    private Long vehicleLength;

    /**
     * 车辆宽度
     */
    @JSONField(name= "VehicleWidth")
    private Long vehicleWidth;

    /**
     * 车辆高度
     */
    @JSONField(name= "VehicleHeight")
    private Long vehicleHeight;

    /**
     * 车身颜色
     */
    @JSONField(name= "VehicleColor")
    private String vehicleColor;

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
     * 贴膜颜色：0：深色，1：浅色，2：无
     */
    @JSONField(name= "FilmColor")
    private String filmColor;

    /**
     * 改装标志：0：未改装，1：改装
     */
    @JSONField(name= "IsModified")
    private Integer isModified;

    // *****************data附加字段*****************

    /**
     * 上半身颜色标签1
     */
    @JSONField(name= "UpcolorTag1")
    private String upcolorTag1;

    /**
     * 上半身颜色标签2
     */
    @JSONField(name= "UpcolorTag2")
    private Integer upcolorTag2;

    /**
     * 主颜色标签标签1
     */
    @JSONField(name= "MaincolorTag")
    private Integer maincolorTag;

    /**
     * 主颜色标签2
     */
    @JSONField(name= "MaincolorTag1")
    private Integer maincolorTag1;

    /**
     * 主颜色标签3
     */
    @JSONField(name= "MaincolorTag2")
    private Integer maincolorTag2;

    /**
     * 性别。0：未知，1：男性，2：女性
     */
    @JSONField(name= "Sex")
    private Byte sex;

    /**
     * 年龄。0：未知，4：小朋友，8:青年，16：中年，32：老年，64：备用
     */
    @JSONField(name= "Age")
    private Byte age;

    /**
     * 是否戴头盔。2：未知，1：戴，0：不戴
     */
    @JSONField(name= "Helmet")
    private Integer helmet;

    /**
     * 乘客1头盔颜色标签
     */
    @JSONField(name= "HelmetColorTag1")
    private String helmetColorTag1;

    /**
     * 乘客2头盔颜色标签
     */
    @JSONField(name= "HelmetColorTag2")
    private Integer helmetColorTag2;

    /**
     * 是否戴眼镜。-1：未知，1：戴眼镜，0：不戴眼镜
     */
    @JSONField(name= "Glasses")
    private Integer glasses;

    /**
     * 是否背包。-1：未知，1：背包，0：不背包
     */
    @JSONField(name= "Bag")
    private Integer bag;

    /**
     * 是否打伞。-1：未知，1：打伞，0：不打伞
     */
    @JSONField(name= "Umbrella")
    private Integer umbrella;

    /**
     * 角度。0：未知，128：正面，256：侧面，512：背面
     */
    @JSONField(name= "Angle")
    private Integer angle;

    /**
     * 是否有手提包。-1：未知，1：有手提包，0：没有手提包
     */
    @JSONField(name= "Handbag")
    private Integer handbag;

    /**
     * 上身衣着
     */
    @JSONField(name= "CoatStyle")
    private String coatStyle;

    /**
     * 下身衣着
     */
    @JSONField(name= "TrousersStyle")
    private String trousersStyle;

    /**
     *
     */
    @JSONField(name= "TubeId")
    private Integer tubeId;

    /**
     * 人脸图片URL
     */
    @JSONField(name= "FaceImgurl")
    private String faceImgurl;

    /**
     *
     */
    @JSONField(name= "LamShape")
    private Integer lamShape;

    /**
     * 是否带口罩
     */
    @JSONField(name= "Respirator")
    private Integer respirator;

    /**
     * 是否带帽子
     */
    @JSONField(name= "Cap")
    private Integer cap;

    /**
     * 发型
     */
    @JSONField(name= "HairStyle")
    private Integer hairStyle;

    /**
     * 上衣纹理
     */
    @JSONField(name= "CoatTexture")
    private Integer coatTexture;

    /**
     * 下衣纹理
     */
    @JSONField(name= "TrousersTexture")
    private Integer trousersTexture;

    /**
     * 关联人脸ID
     */
    @JSONField(name= "FaceUUID")
    private String faceUUID;

    /**
     *
     */
    @JSONField(name= "Wheels")
    private Byte wheels;

    /**
     *
     */
    @JSONField(name= "BikeGenre")
    private Integer bikeGenre;


    // *****************kafka附加字段*****************

    /**
     * 行人/骑车人上身衣着 objType为HUMAN或BIKE时，有此值. 款式数值包括: -1-未知, 1-长袖, 2-短袖
     */
    @JSONField(name= "UpperClothing")
    private String upperClothing;

    /**
     * 行人下身衣着 objType为HUMAN时，有此值. 款式数值包括: -1-未知, 1-长裤, 2-短裤, 3-裙子
     */
    @JSONField(name= "LowerClothing")
    private String lowerClothing;

    /**
     * 人脸图片地址
     */
    @JSONField(name= "FaceUrl")
    private String faceUrl;

    /**
     * 目标标志后主色
     */
    @JSONField(name= "MainColor1")
    private String mainColor1;

    /**
     *
     */
    @JSONField(name= "MainColor2")
    private String mainColor2;

    /**
     *
     */
    @JSONField(name= "CoatColor1")
    private String coatColor1;

    /**
     *
     */
    @JSONField(name= "CoatColor2")
    private String coatColor2;

    /**
     *
     */
    @JSONField(name= "TrousersColor1")
    private String trousersColor1;

    /**
     *
     */
    @JSONField(name= "TrousersColor2")
    private String trousersColor2;

    /**
     *
     */
    @JSONField(name= "CarryBag")
    private String carryBag;

    /**
     * 车灯形状
     */
    @JSONField(name = "LampShape")
    private String lampShape;

    /**
     * 载客
     */
    @JSONField(name = "CarryPassenger")
    private String carryPassenger;

    /**
     * 车辆社会属性
     "1": 普通
     "2": 外賣
     "3": 快遞
     "-1": 未知
     */
    @JSONField(name = "SocialAttribute")
    private Integer socialAttribute;

    /**
     * 车辆所属单位
     -1：未知
     中通
     韵达
     圆通
     中国邮政
     顺丰
     京东
     百世汇通
     美团
     饿了么
     */
    @JSONField(name = "Enterprise")
    private Integer enterprise;

}
