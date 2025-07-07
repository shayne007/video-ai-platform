package com.keensense.schedule.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Table;
import lombok.Data;
import lombok.ToString;

/**
 * 1400标准非机动车类
 */
@Data
@Table(TableName = "bike_result")
@ToString(callSuper = true, includeFieldNames = true)
public class NonMotorVehiclesResult extends Result implements java.io.Serializable {
    
    private static final long serialVersionUID = 13543253463463L;
    
    /**
     * 车辆标识
     */
    @JSONField(name = "NonMotorVehicleID")
    @Column(ColumnName = "nonmotorvehicleid")
    private String nonMotorVehicleID;
    
    /**
     * 有无车牌
     */
    @JSONField(name = "HasPlate")
    @Column(ColumnName = "hasplate")
    private Integer hasPlate;
    
    /**
     * 号牌种类
     */
    @JSONField(name = "PlateClass")
    @Column(ColumnName = "plateclass")
    private String plateClass;
    
    /**
     * 车牌颜色：指号牌底色，取 ColorType 中部分值： 黑色，白色，黄色，蓝色，绿色
     */
    @JSONField(name = "PlateColor")
    @Column(ColumnName = "platecolor")
    private String plateColor;
    
    /**
     * 车牌号：各类机动车号牌编号车牌全部无法识别的以“无车牌”标识，部分未识别的每个字符以半角‘-’
     */
    @JSONField(name = "PlateNo")
    @Column(ColumnName = "plateno")
    private String plateNo;
    
    /**
     * 挂车牌号：各类机动车挂车号牌编号
     */
    @JSONField(name = "PlateNoAttach")
    @Column(ColumnName = "platenoattach")
    private String plateNoAttach;
    
    /**
     * 车牌描述：车牌框广告信息，包括车行名称，联系电话等
     */
    @JSONField(name = "PlateDescribe")
    @Column(ColumnName = "platedescribe")
    private String plateDescribe;
    
    /**
     * 是否套牌
     */
    @JSONField(name = "IsDecked")
    @Column(ColumnName = "isdecked")
    private Integer isDecked;
    
    /**
     * 是否涂改
     */
    @JSONField(name = "IsAltered")
    @Column(ColumnName = "isaltered")
    private Integer isAltered;
    
    /**
     * 是否遮挡
     */
    @JSONField(name = "IsCovered")
    @Column(ColumnName = "iscovered")
    private Integer isCovered;
    
    /**
     * 行驶速度：单位千米每小时（km/h）
     */
    @JSONField(name = "Speed")
    @Column(ColumnName = "speed")
    private Double speed;
    
    /**
     * 行驶状态代码
     */
    @JSONField(name = "DrivingStatusCode")
    @Column(ColumnName = "drivingstatuscode")
    private String drivingStatusCode;
    
    /**
     * 车辆使用性质代码
     */
    @JSONField(name = "UsingPropertiesCode")
    @Column(ColumnName = "usingpropertiescode")
    private String usingPropertiesCode;
    
    /**
     * 车辆品牌：被标注车辆的品牌
     */
    @JSONField(name = "VehicleBrand")
    @Column(ColumnName = "vehiclebrand")
    private String vehicleBrand;
    
    /**
     * 车辆款型：被标注车辆的款式型号描述
     */
    @JSONField(name = "VehicleType")
    @Column(ColumnName = "vehicletype")
    private String vehicleType;
    
    /**
     * 车辆长度
     */
    @JSONField(name = "VehicleLength")
    @Column(ColumnName = "vehiclelength")
    private Integer vehicleLength;
    
    /**
     * 车辆宽度
     */
    @JSONField(name = "VehicleWidth")
    @Column(ColumnName = "vehiclewidth")
    private Integer vehicleWidth;
    
    /**
     * 车辆高度
     */
    @JSONField(name = "VehicleHeight")
    @Column(ColumnName = "vehicleheight")
    private Integer vehicleHeight;
    
    /**
     * 车身颜色
     */
    @JSONField(name = "VehicleColor")
    @Column(ColumnName = "vehiclecolor")
    private String vehicleColor;
    
    /**
     * 车前盖：对车前盖的描述
     */
    @JSONField(name = "VehicleHood")
    @Column(ColumnName = "vehiclehood")
    private String vehicleHood;
    
    /**
     * 车后盖：对车后盖的描述
     */
    @JSONField(name = "VehicleTrunk")
    @Column(ColumnName = "vehicletrunk")
    private String vehicleTrunk;
    
    /**
     * 车轮
     */
    @JSONField(name = "VehicleWheel")
    @Column(ColumnName = "vehiclewheel")
    private String vehicleWheel;
    
    /**
     * 车轮印花纹
     */
    @JSONField(name = "WheelPrintedPattern")
    @Column(ColumnName = "wheelprintedpattern")
    private String wheelPrintedPattern;
    
    /**
     * 车窗：对车窗的描述
     */
    @JSONField(name = "VehicleWindow")
    @Column(ColumnName = "vehiclewindow")
    private String vehicleWindow;
    
    /**
     * 车顶：对车顶的描述
     */
    @JSONField(name = "VehicleRoof")
    @Column(ColumnName = "vehicleroof")
    private String vehicleRoof;
    
    /**
     * 车门：对车门的描述
     */
    @JSONField(name = "VehicleDoor")
    @Column(ColumnName = "vehicledoor")
    private String vehicleDoor;
    
    /**
     * 车侧：对车侧面的描述，不包括门
     */
    @JSONField(name = "SideOfVehicle")
    @Column(ColumnName = "sideofvehicle")
    private String sideOfVehicle;
    
    /**
     * 车厢：对车厢的描述
     */
    @JSONField(name = "CarOfVehicle")
    @Column(ColumnName = "carofvehicle")
    private String carOfVehicle;
    
    /**
     * 后视镜：对后视镜的描述
     */
    @JSONField(name = "RearviewMirror")
    @Column(ColumnName = "rearviewmirror")
    private String rearviewMirror;
    
    /**
     * 底盘：对车底盘的描述
     */
    @JSONField(name = "VehicleChassis")
    @Column(ColumnName = "vehiclechassis")
    private String vehicleChassis;
    
    /**
     * 遮挡：对车遮挡物的描述
     */
    @JSONField(name = "VehicleShielding")
    @Column(ColumnName = "vehicleshielding")
    private String vehicleShielding;
    
    /**
     * 贴膜颜色：0：深色，1：浅色，2：无
     */
    @JSONField(name = "FilmColor")
    @Column(ColumnName = "filmcolor")
    private Integer filmColor;
    
    /**
     * 改装标志：0：未改装，1：改装
     */
    @JSONField(name = "IsModified")
    @Column(ColumnName = "ismodified")
    private Integer isModified;
    
    // *****************data附加字段*****************
    
    /**
     * 上半身颜色标签1
     */
    @JSONField(name = "UpcolorTag1")
    @Column(ColumnName = "upcolortag1")
    private Integer upcolorTag1;
    
    /**
     * 上半身颜色标签2
     */
    @JSONField(name = "UpcolorTag2")
    @Column(ColumnName = "upcolortag2")
    private Integer upcolorTag2;
    
    /**
     * 主颜色标签标签1
     */
    @JSONField(name = "MaincolorTag")
    @Column(ColumnName = "maincolortag")
    private Integer maincolorTag;
    
    /**
     * 主颜色标签2
     */
    @JSONField(name = "MaincolorTag1")
    @Column(ColumnName = "maincolortag1")
    private Integer maincolorTag1;
    
    /**
     * 主颜色标签3
     */
    @JSONField(name = "MaincolorTag2")
    @Column(ColumnName = "maincolortag2")
    private Integer maincolorTag2;
    
    /**
     * 性别。0：未知，1：男性，2：女性
     */
    @JSONField(name = "Sex")
    @Column(ColumnName = "sex")
    private Integer sex;
    
    /**
     * 年龄。0：未知，4：小朋友，8:青年，16：中年，32：老年，64：备用
     */
    @JSONField(name = "Age")
    @Column(ColumnName = "age")
    private Integer age;
    
    /**
     * 是否戴头盔。2：未知，1：戴，0：不戴
     */
    @JSONField(name = "Helmet")
    @Column(ColumnName = "helmet")
    private Integer helmet;
    
    /**
     * 乘客1头盔颜色标签
     */
    @JSONField(name = "HelmetColorTag1")
    @Column(ColumnName = "helmetcolortag1")
    private Integer helmetColorTag1;
    
    /**
     * 乘客2头盔颜色标签
     */
    @JSONField(name = "HelmetColorTag2")
    @Column(ColumnName = "helmetcolortag2")
    private Integer helmetColorTag2;
    
    /**
     * 是否戴眼镜。-1：未知，1：戴眼镜，0：不戴眼镜
     */
    @JSONField(name = "Glasses")
    @Column(ColumnName = "glasses")
    private Integer glasses;
    
    /**
     * 是否背包。-1：未知，1：背包，0：不背包
     */
    @JSONField(name = "Bag")
    @Column(ColumnName = "bag")
    private Integer bag;
    
    /**
     * 是否打伞。-1：未知，1：打伞，0：不打伞
     */
    @JSONField(name = "Umbrella")
    @Column(ColumnName = "umbrella")
    private Integer umbrella;
    
    /**
     * 角度。0：未知，128：正面，256：侧面，512：背面
     */
    @JSONField(name = "Angle")
    @Column(ColumnName = "angle")
    private Integer angle;
    
    /**
     * 是否有手提包。-1：未知，1：有手提包，0：没有手提包
     */
    @JSONField(name = "Handbag")
    @Column(ColumnName = "handbag")
    private Integer handbag;
    
    /**
     * 上身衣着
     */
    @JSONField(name = "CoatStyle")
    @Column(ColumnName = "coatstyle")
    private String coatStyle;
    
    /**
     * 下身衣着
     */
    @JSONField(name = "TrousersStyle")
    @Column(ColumnName = "trousersstyle")
    private String trousersStyle;
    
    /**
     *
     */
    @JSONField(name = "TubeId")
    @Column(ColumnName = "tubeid")
    private Integer tubeId;
    
    /**
     * 人脸图片URL
     */
    @JSONField(name = "FaceImgurl")
    @Column(ColumnName = "faceimgurl")
    private String faceImgurl;
    
    /**
     *
     */
    @JSONField(name = "LamShape")
    @Column(ColumnName = "lamshape")
    private Integer lamShape;
    
    /**
     * 是否带口罩
     */
    @JSONField(name = "Respirator")
    @Column(ColumnName = "respirator")
    private Integer respirator;
    
    /**
     * 是否带帽子
     */
    @JSONField(name = "Cap")
    @Column(ColumnName = "cap")
    private Integer cap;
    
    /**
     * 发型
     */
    @JSONField(name = "HairStyle")
    @Column(ColumnName = "hairstyle")
    private Integer hairStyle;
    
    /**
     * 上衣纹理
     */
    @JSONField(name = "CoatTexture")
    @Column(ColumnName = "coattexture")
    private Integer coatTexture;
    
    /**
     * 下衣纹理
     */
    @JSONField(name = "TrousersTexture")
    @Column(ColumnName = "trouserstexture")
    private Integer trousersTexture;
    
    /**
     * 关联人脸ID
     */
    @JSONField(name = "FaceUUID")
    @Column(ColumnName = "faceuuid")
    private String faceUUID;
    
    /**
     *
     */
    @JSONField(name = "Wheels")
    @Column(ColumnName = "wheels")
    private Integer wheels;
    
    /**
     *
     */
    @JSONField(name = "BikeGenre")
    @Column(ColumnName = "bikegenre")
    private Integer bikeGenre;
    
    // *****************kafka附加字段*****************
    /**
     * 目标标志后主色
     */
    @JSONField(name = "MainColor1")
    @Column(ColumnName = "maincolor1")
    private String mainColor1;
    
    /**
     *
     */
    @JSONField(name = "MainColor2")
    @Column(ColumnName = "maincolor2")
    private String mainColor2;
    
    /**
     *
     */
    @JSONField(name = "CoatColor1")
    @Column(ColumnName = "coatcolor1")
    private String coatColor1;
    
    /**
     *
     */
    @JSONField(name = "CoatColor2")
    @Column(ColumnName = "coatcolor2")
    private String coatColor2;
    
    /**
     *
     */
    @JSONField(name = "TrousersColor1")
    @Column(ColumnName = "trouserscolor1")
    private String trousersColor1;
    
    /**
     *
     */
    @JSONField(name = "TrousersColor2")
    @Column(ColumnName = "trouserscolor2")
    private String trousersColor2;
    
    /**
     *
     */
    @JSONField(name = "CarryBag")
    @Column(ColumnName = "carrybag")
    private String carryBag;
    
    /**
     * 车灯形状（1-三角，2 菱形，-1未知）
     */
    @JSONField(name = "LampShape")
    @Column(ColumnName = "lampshape")
    private Integer lampShape;
    
    /**
     * 是否载客（1是，0为否，-1未知）
     */
    @JSONField(name = "CarryPassenger")
    @Column(ColumnName = "carrypassenger")
    private Integer carryPassenger;
    
    /**
     * 是否有蓬（1是，0为否，-1未知）
     */
    @JSONField(name = "Canopy")
    @Column(ColumnName = "canopy")
    private Integer canopy;
    
    /**
     * 车辆社会属性
     * "1": 普通
     * "2": 外賣
     * "3": 快遞
     * "-1": 未知
     */
    @JSONField(name = "SocialAttribute")
    @Column(ColumnName = "socialattribute")
    private Integer socialattribute;
    
    /**
     * 车辆所属单位
     * -1：未知
     * 中通
     * 韵达
     * 圆通
     * 中国邮政
     * 顺丰
     * 京东
     * 百世汇通
     * 美团
     * 饿了么
     */
    @JSONField(name = "Enterprise")
    @Column(ColumnName = "enterprise")
    private Integer enterprise;
    
}
