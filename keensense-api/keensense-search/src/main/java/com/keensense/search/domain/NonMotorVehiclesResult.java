package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.keensense.search.tool_interface.ParameterCheck;
import com.keensense.search.utils.ParametercheckUtil;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 1400标准非机动车类
 */
@Data
@Table(name = "bike_result")
@ToString(callSuper = true, includeFieldNames = true)
public class NonMotorVehiclesResult extends Result implements java.io.Serializable, ParameterCheck {

    private static final long serialVersionUID = 13543253463463L;

    /**
     * 车辆标识
     */
    @JSONField(name = "NonMotorVehicleID")
    @Column(name = "nonmotorvehicleid")
    private String nonMotorVehicleID;

    /**
     * 有无车牌
     */
    @JSONField(name = "HasPlate")
    @Column(name = "hasplate")
    private Integer hasPlate;

    /**
     * 号牌种类
     */
    @JSONField(name = "PlateClass")
    @Column(name = "plateclass")
    private String plateClass;

    /**
     * 车牌颜色：指号牌底色，取 ColorType 中部分值： 黑色，白色，黄色，蓝色，绿色
     */
    @JSONField(name = "PlateColor")
    @Column(name = "platecolor")
    private String plateColor;

    /**
     * 车牌号：各类机动车号牌编号车牌全部无法识别的以“无车牌”标识，部分未识别的每个字符以半角‘-’
     */
    @JSONField(name = "PlateNo")
    @Column(name = "plateno")
    private String plateNo;

    /**
     * 挂车牌号：各类机动车挂车号牌编号
     */
    @JSONField(name = "PlateNoAttach")
    @Column(name = "platenoattach")
    private String plateNoAttach;

    /**
     * 车牌描述：车牌框广告信息，包括车行名称，联系电话等
     */
    @JSONField(name = "PlateDescribe")
    @Column(name = "platedescribe")
    private String plateDescribe;

    /**
     * 是否套牌
     */
    @JSONField(name = "IsDecked")
    @Column(name = "isdecked")
    private Integer isDecked;

    /**
     * 是否涂改
     */
    @JSONField(name = "IsAltered")
    @Column(name = "isaltered")
    private Integer isAltered;

    /**
     * 是否遮挡
     */
    @JSONField(name = "IsCovered")
    @Column(name = "iscovered")
    private Integer isCovered;

    /**
     * 行驶速度：单位千米每小时（km/h）
     */
    @JSONField(name = "Speed")
    @Column(name = "speed")
    private Double speed;

    /**
     * 行驶状态代码
     */
    @JSONField(name = "DrivingStatusCode")
    @Column(name = "drivingstatuscode")
    private String drivingStatusCode;

    /**
     * 车辆使用性质代码
     */
    @JSONField(name = "UsingPropertiesCode")
    @Column(name = "usingpropertiescode")
    private String usingPropertiesCode;

    /**
     * 车辆品牌：被标注车辆的品牌
     */
    @JSONField(name = "VehicleBrand")
    @Column(name = "vehiclebrand")
    private String vehicleBrand;

    /**
     * 车辆款型：被标注车辆的款式型号描述
     */
    @JSONField(name = "VehicleType")
    @Column(name = "vehicletype")
    private String vehicleType;

    /**
     * 车辆长度
     */
    @JSONField(name = "VehicleLength")
    @Column(name = "vehiclelength")
    private Integer vehicleLength;

    /**
     * 车辆宽度
     */
    @JSONField(name = "VehicleWidth")
    @Column(name = "vehiclewidth")
    private Integer vehicleWidth;

    /**
     * 车辆高度
     */
    @JSONField(name = "VehicleHeight")
    @Column(name = "vehicleheight")
    private Integer vehicleHeight;

    /**
     * 车身颜色
     */
    @JSONField(name = "VehicleColor")
    @Column(name = "vehiclecolor")
    private String vehicleColor;

    /**
     * 车前盖：对车前盖的描述
     */
    @JSONField(name = "VehicleHood")
    @Column(name = "vehiclehood")
    private String vehicleHood;

    /**
     * 车后盖：对车后盖的描述
     */
    @JSONField(name = "VehicleTrunk")
    @Column(name = "vehicletrunk")
    private String vehicleTrunk;

    /**
     * 车轮
     */
    @JSONField(name = "VehicleWheel")
    @Column(name = "vehiclewheel")
    private String vehicleWheel;

    /**
     * 车轮印花纹
     */
    @JSONField(name = "WheelPrintedPattern")
    @Column(name = "wheelprintedpattern")
    private String wheelPrintedPattern;

    /**
     * 车窗：对车窗的描述
     */
    @JSONField(name = "VehicleWindow")
    @Column(name = "vehiclewindow")
    private String vehicleWindow;

    /**
     * 车顶：对车顶的描述
     */
    @JSONField(name = "VehicleRoof")
    @Column(name = "vehicleroof")
    private String vehicleRoof;

    /**
     * 车门：对车门的描述
     */
    @JSONField(name = "VehicleDoor")
    @Column(name = "vehicledoor")
    private String vehicleDoor;

    /**
     * 车侧：对车侧面的描述，不包括门
     */
    @JSONField(name = "SideOfVehicle")
    @Column(name = "sideofvehicle")
    private String sideOfVehicle;

    /**
     * 车厢：对车厢的描述
     */
    @JSONField(name = "CarOfVehicle")
    @Column(name = "carofvehicle")
    private String carOfVehicle;

    /**
     * 后视镜：对后视镜的描述
     */
    @JSONField(name = "RearviewMirror")
    @Column(name = "rearviewmirror")
    private String rearviewMirror;

    /**
     * 底盘：对车底盘的描述
     */
    @JSONField(name = "VehicleChassis")
    @Column(name = "vehiclechassis")
    private String vehicleChassis;

    /**
     * 遮挡：对车遮挡物的描述
     */
    @JSONField(name = "VehicleShielding")
    @Column(name = "vehicleshielding")
    private String vehicleShielding;

    /**
     * 贴膜颜色：0：深色，1：浅色，2：无
     */
    @JSONField(name = "FilmColor")
    @Column(name = "filmcolor")
    private Integer filmColor;

    /**
     * 改装标志：0：未改装，1：改装
     */
    @JSONField(name = "IsModified")
    @Column(name = "ismodified")
    private Integer isModified;

    // *****************data附加字段*****************

    /**
     * 上半身颜色标签1
     */
    @JSONField(name = "UpcolorTag1")
    @Column(name = "upcolortag1")
    private Integer upcolorTag1;

    /**
     * 上半身颜色标签2
     */
    @JSONField(name = "UpcolorTag2")
    @Column(name = "upcolortag2")
    private Integer upcolorTag2;

    /**
     * 主颜色标签标签1
     */
    @JSONField(name = "MaincolorTag")
    @Column(name = "maincolortag")
    private Integer maincolorTag;

    /**
     * 主颜色标签2
     */
    @JSONField(name = "MaincolorTag1")
    @Column(name = "maincolortag1")
    private Integer maincolorTag1;

    /**
     * 主颜色标签3
     */
    @JSONField(name = "MaincolorTag2")
    @Column(name = "maincolortag2")
    private Integer maincolorTag2;

    /**
     * 性别。0：未知，1：男性，2：女性
     */
    @JSONField(name = "Sex")
    @Column(name = "sex")
    private Integer sex;

    /**
     * 年龄。0：未知，4：小朋友，8:青年，16：中年，32：老年，64：备用
     */
    @JSONField(name = "Age")
    @Column(name = "age")
    private Integer age;

    /**
     * 是否戴头盔。2：未知，1：戴，0：不戴
     */
    @JSONField(name = "Helmet")
    @Column(name = "helmet")
    private Integer helmet;

    /**
     * 乘客1头盔颜色标签
     */
    @JSONField(name = "HelmetColorTag1")
    @Column(name = "helmetcolortag1")
    private Integer helmetColorTag1;

    /**
     * 乘客2头盔颜色标签
     */
    @JSONField(name = "HelmetColorTag2")
    @Column(name = "helmetcolortag2")
    private Integer helmetColorTag2;

    /**
     * 是否戴眼镜。-1：未知，1：戴眼镜，0：不戴眼镜
     */
    @JSONField(name = "Glasses")
    @Column(name = "glasses")
    private Integer glasses;

    /**
     * 是否背包。-1：未知，1：背包，0：不背包
     */
    @JSONField(name = "Bag")
    @Column(name = "bag")
    private Integer bag;

    /**
     * 是否打伞。-1：未知，1：打伞，0：不打伞
     */
    @JSONField(name = "Umbrella")
    @Column(name = "umbrella")
    private Integer umbrella;

    /**
     * 角度。0：未知，128：正面，256：侧面，512：背面
     */
    @JSONField(name = "Angle")
    @Column(name = "angle")
    private Integer angle;

    /**
     * 是否有手提包。-1：未知，1：有手提包，0：没有手提包
     */
    @JSONField(name = "Handbag")
    @Column(name = "handbag")
    private Integer handbag;

    /**
     * 上身衣着
     */
    @JSONField(name = "CoatStyle")
    @Column(name = "coatstyle")
    private String coatStyle;

    /**
     * 下身衣着
     */
    @JSONField(name = "TrousersStyle")
    @Column(name = "trousersstyle")
    private String trousersStyle;

    /**
     *
     */
    @JSONField(name = "TubeId")
    @Column(name = "tubeid")
    private Integer tubeId;

    /**
     * 人脸图片URL
     */
    @JSONField(name = "FaceImgurl")
    @Column(name = "faceimgurl")
    private String faceImgurl;

    /**
     *
     */
    @JSONField(name = "LamShape")
    @Column(name = "lamshape")
    private Integer lamShape;

    /**
     * 是否带口罩
     */
    @JSONField(name = "Respirator")
    @Column(name = "respirator")
    private Integer respirator;

    /**
     * 是否带帽子
     */
    @JSONField(name = "Cap")
    @Column(name = "cap")
    private Integer cap;

    /**
     * 发型
     */
    @JSONField(name = "HairStyle")
    @Column(name = "hairstyle")
    private Integer hairStyle;

    /**
     * 上衣纹理
     */
    @JSONField(name = "CoatTexture")
    @Column(name = "coattexture")
    private Integer coatTexture;

    /**
     * 下衣纹理
     */
    @JSONField(name = "TrousersTexture")
    @Column(name = "trouserstexture")
    private Integer trousersTexture;

    /**
     * 关联人脸ID
     */
    @JSONField(name = "FaceUUID")
    @Column(name = "faceuuid")
    private String faceUUID;

    /**
     *
     */
    @JSONField(name = "Wheels")
    @Column(name = "wheels")
    private Integer wheels;

    /**
     *
     */
    @JSONField(name = "BikeGenre")
    @Column(name = "bikegenre")
    private Integer bikeGenre;

    // *****************kafka附加字段*****************
    /**
     * 目标标志后主色
     */
    @JSONField(name = "MainColor1")
    @Column(name = "maincolor1")
    private String mainColor1;

    /**
     *
     */
    @JSONField(name = "MainColor2")
    @Column(name = "maincolor2")
    private String mainColor2;

    /**
     *
     */
    @JSONField(name = "CoatColor1")
    @Column(name = "coatcolor1")
    private String coatColor1;

    /**
     *
     */
    @JSONField(name = "CoatColor2")
    @Column(name = "coatcolor2")
    private String coatColor2;

    /**
     *
     */
    @JSONField(name = "TrousersColor1")
    @Column(name = "trouserscolor1")
    private String trousersColor1;

    /**
     *
     */
    @JSONField(name = "TrousersColor2")
    @Column(name = "trouserscolor2")
    private String trousersColor2;

    /**
     *
     */
    @JSONField(name = "CarryBag")
    @Column(name = "carrybag")
    private String carryBag;

    /**
     * 车灯形状（1-三角，2 菱形，-1未知）
     */
    @JSONField(name = "LampShape")
    @Column(name = "lampshape")
    private Integer lampShape;

    /**
     * 是否载客（1是，0为否，-1未知）
     */
    @JSONField(name = "CarryPassenger")
    @Column(name = "carrypassenger")
    private Integer carryPassenger;

    /**
     * 是否有蓬（1是，0为否，-1未知）
     */
    @JSONField(name = "Canopy")
    @Column(name = "canopy")
    private Integer canopy;

    /**
     * 车辆社会属性
     "1": 普通
     "2": 外賣
     "3": 快遞
     "-1": 未知
     */
    @JSONField(name = "SocialAttribute")
    @Column(name = "socialattribute")
    private Integer socialattribute;

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
    @Column(name = "enterprise")
    private Integer enterprise;

    @Override
    public void checkParameter() {
        ParametercheckUtil.checkEmpty("NonMotorVehicleID", nonMotorVehicleID);
        ParametercheckUtil.checkEmpty("MarkTime", markTime);
        ParametercheckUtil.checkEmpty("Serialnumber", serialnumber);
        ParametercheckUtil.checkEmpty("SubImageList", subImageList);
        ParametercheckUtil.checkEmpty("FeatureOject", featureObject);
    }
}
