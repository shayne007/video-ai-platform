package com.keensense.admin.vo;


import com.alibaba.fastjson.annotation.JSONField;
import com.keensense.admin.dto.ObjextResultDto;
import com.keensense.admin.dto.VlprResultDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ResultQueryVo implements Serializable {

    private String type;
    private String taskTypeCode;

    // 以图搜图相似度
    private String similarity;

    private String compareType;

    private String videoHttpUrl;

    private String trackUrl;

    private String id;

    private String serialnumber;

    //特征文件路径
    private String featuredataurl;

    //时间降序升序
    private String timeSelect;

    // 联网录像分析结构序列号
    private String taskSerialnumbers;

    private Integer fromType;

    //vlpr 车牌信息 objext 目标信息
    private String tableName;

    //vlpr字段
    private VlprResultDto vlprResult;

    //vlpr字段
    private ObjextResultDto objextResult;

    private String taskid;

    private String cameraId;

    private String cameraName;

    private String cameraType;

    private String license;

    private String licenseattribution;

    private String platecolor;

    private String plateType;

    private String platetypeName;

    private Short confidence;

    private Short bright;

    private String direction;

    private Short locationleft;

    private Short locationtop;

    private Short locationright;

    private Short locationbottom;

    private Short costtime;

    private String carbright;

    private String carcolor;

    private String carlogo;

    private String imagepath;

    private Date resulttime;

    private Date createtime;

    private Long frameIndex;

    private Double carSpeed;

    private String labelinfodata;

    private String vehiclekind;

    private String vehiclebrand;

    private String vehicleseries;

    private String vehicleStyle;

    private Byte tag;

    private Byte paper;

    private String sun;

    private Byte drop;

    private Byte call;

    private Byte crash;

    private Byte danger;

    private Byte mainbelt;

    private Byte secondbelt;

    private Short vehicleleft;

    private Short vehicletop;

    private Short vehicleright;

    private Short vehiclebootom;

    private Short vehicleconfidence;

    private Byte face;

    private String faceUrl1;

    private String faceUrl2;

    private String vehicleurl;

    private String createtimeStr;

    private Integer sex;


    private String imgurl;

    private int imgWidth;

    private int imgHeight;

    private String bigImgurl;

    private Short objtype;
    private Short batchNo;
    private Integer helmetcolor;
    private Integer helmetcolorTag1;
    private String helmetcolorTag1Str;

    /**
     * 拉杆箱
     */
    private Integer luggage;
    /**
     * 手推车
     */
    private Integer trolley;

    private String featurFollowarea;//特征区域坐标

    private Short bikeHasPlate;//是否挂牌

    private String relation;//是否添加了关联图片(即是否多图搜索)

    private String taskSerial;//任务序号objext_result表的serialnumber字段，vlpr_result表的serialnum字段(v3.1.1.3版本新以图搜图接口用到)

    private int cutX;//截取图片的x坐标

    private int cutY;//截取图片的y坐标

    private int cutW;//截取图片的宽度

    private int cutH;//截取图片的高度

    private String helmetcolorTag1Name;
    private Integer helmetcolorTag2;
    private String helmetcolorTag2Str;
    private String helmetcolorTag2Name;

    private String helmetcolorStr;
    private String helmetcolorName;
    private Integer passengersUpColor;
    private String passengersUpColorStr;
    private String passengersUpColorName;
    private String bikeColor;
    private String bikecolorStr;
    private String bikecolorName;

    private Integer bikeGenre;
    private String bikeGenreName;
    private Integer seatingCount;
    private Integer helmet;
    private String helmet1;
    private String helmet2;

    private String helmetStr;

    private Integer maincolor;
    private Integer maincolorTag1;
    private Integer maincolorTag2;
    private Integer mainColorTag3;

    private Integer maincolorCar;
    private String maincolorStr;
    private String maincolorName;

    private Integer upcolor;
    private Integer upcolor1;
    private Integer upcolor2;
    private Integer upcolor3;

    private String upcolorStr1;
    private String upcolorStr2;
    private String upcolorStr;
    private String upcolorName;

    private Long vlprResultId;

    private Integer lowcolor;
    private Integer lowcolor1;
    private Integer lowcolor2;
    private Integer lowcolor3;

    private String lowcolorStr1;
    private String lowcolorStr2;
    private String lowcolorStr;

    private String lowcolorName;


    private String qualityscore;
    private float horizontalangle;// 水平角度
    private String horizontalangleStr;

    private float verticalangle;//垂直角度
    private String verticalangleStr;

    private float rotatingangle;//侧角
    private String rotatingangleStr;
    private Integer nation;

    private String humanfeature;

    private Integer upcolorTag;
    private Integer upcolorTag1;
    private Integer upcolorTag2;
    private Integer upcolorTag3;

    private Integer lowcolorTag;
    private Integer lowcolorTag1;
    private Integer lowcolorTag2;
    private Integer lowcolorTag3;

    private Integer age;

    private Integer faceAge;

    private Integer faceSex;

    private Byte bikeAge;

    private Byte bikeSex;

    private Byte wheels;

    private String wheelsName;

    private Byte size;

    private String sizeStr;

    private Integer tubeid;

    private Integer objid;

    private Integer startframeidx;

    private Integer endframeidx;

    private Long startframepts;

    // 结果再离线视频中出现的时间
    private String timeLocation;

    private Long endframepts;

    private Integer frameidx;

    private Integer glasses;
    private Integer bag;
    private Integer handbag;
    private Integer umbrella;

    private Integer umbrellaColorTag;

    private String umbrellaColorName;

    private Integer angle;//方向
    private Integer angle2;

    private String fileUrl;

    private String pictureLocalPath;
    //人脸path1
    private String pictureLocalPath1;

    // 车灯形状tag
    private Integer lamShape;

    private String lamShapeName;

    private Integer faceGlassess;
    private Short width;

    private Short height;

    private Short x;

    private Short y;


    private Short w;

    private Short h;
    /**
     * 体态特征
     */
    private String bodyCharacter;

    private Float distance;

    private String startTime;

    private String endTime;

    private String isTrack;  //是否加入轨迹库 0没有 1有


    private Integer peccancy;

    private String resultId;

    private Integer helmetColorTag1;

    private Integer helmetColorTag2;

    private String xywh;

    private String recogId;

    private String recogIdYMD;


    private String slaveIp;

    private String coatStyle;
    private String trousersStyle;

    //是否戴口罩
    private String respirator;
    //是否戴帽子
    private String cap;
    //发型
    private String hairStyle;
    //上衣纹理
    private String coatTexture;
    //下衣纹理
    private String trousersTexture;

    private String mainDriver;//主驾驶是否系安全带 [1有,0没有]
    private String coDriver;//副驾驶是否系安全带 [1有,0没有]
    private Integer hasCall;//是否打电话 [1有 0没有]
    private Integer tagNum;//年检标数量：[0表示没有，1表示1个，2表示2个]
    //是否有行李架 [1有，0没有]
    private String rack;
    //车牌颜色
    private String plateColorCode;
    //天窗 [1有，0没有]
    private String sunRoof;
    //行驶速度
    private Double speed;
    //纸巾盒
    private String tissueBox;
    //人脸对应的人或骑行uuid
    private String connectObjectId;
    //人脸对应的人或骑行图片url
    private String connectObjectUrl;

    @ApiModelProperty("手持刀棍 [-1:未知,1:是,0:未知]")
    private Integer hasKnife;
    private String faceUUID;
    @ApiModelProperty("上衣款式[1长袖, 2短袖, -1未知]")
    private String bikeCoatStyle;
    @ApiModelProperty("上衣纹理 [-1:未知,1:净色,2:间条,3:格子,4:图案,5:拼接]")
    private String bikeCoatTexture;
    @ApiModelProperty("姿态 [128正面, 256侧面, 512背面, -1未知]")
    private Integer bikeAngle;
    @ApiModelProperty("方向")
    private Integer bikeAngle2;
    @ApiModelProperty("眼镜[1是, 0否, -1未知]")
    private String bikeGlasses;
    @ApiModelProperty("口罩[1是, 0否, -1未知]")
    private String bikeRespirator;
    @ApiModelProperty("车身颜色[]")
    private String bikecolor;
    @ApiModelProperty("是否背包 [1是, 0否, -1未知]")
    private String bikeBag;
    @ApiModelProperty("是否打伞[1 打伞, 0 未打伞, -1未知]")
    private Integer bikeUmbrella;
    @ApiModelProperty("摆件")
    private String decoration;

    private Integer searchStatus;

    private String longitude;

    private String latitude;

    @ApiModelProperty("车辆是否有车牌")
    private String vehicleHasPlate;
    @ApiModelProperty("是否抱小孩[1是，0为否，-1未知]")
    private String chestHold;
    @ApiModelProperty("行人体态 1正常，2 胖，3瘦，-1 未知")
    private String shape;
    @ApiModelProperty("民族[1为少数民族，0为汉族，-1未知]")
    private String minority;
    @ApiModelProperty("车灯形状[1-三角，2 菱形，-1未知]")
    private String lampShape;
    @ApiModelProperty("是否载客[1是，0为否，-1未知]")
    private String carryPassenger;
    @ApiModelProperty("车牌类型")
    private String plateClass;
    @ApiModelProperty("撞损车")
    private String hasCrash;
    @ApiModelProperty("危化品车")
    private String hasDanger;
    @ApiModelProperty("姿态 1正面, 2侧面, 3背面, -1未知")
    private String vehicleAngle;
    //入侵越界标志
    private Integer overlineType;
    private Integer overlineTypeBike;
    private Integer overlineTypeCar;
    private String trackId;
    @ApiModelProperty("人脸图关联的类型")
    private Integer connectObjectType;

    /**
     * 车辆社会属性
     * "1": 普通
     * "2": 外賣
     * "3": 快遞
     * "-1": 未知
     */
    @ApiModelProperty("车辆社会属性 1普通, 2外賣, 3快遞, -1未知")
    private Integer socialAttribute;

    @ApiModelProperty("车辆社会属性描述")
    private String socialAttributeStr;

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
    @ApiModelProperty("车辆所属单位  1: 中通、2: 韻達、3: 圓通、4: 申通、5: 中國郵政、6: 順豐、7: 京東、8: 百世匯通、9: 美團、10: 餓了麼、-1: 未知")
    private Integer enterprise;

    @ApiModelProperty("车辆所属单位描述")
    private String enterpriseStr;
    private String aerial;

    /**
     * 视图库查回的子任务号
     */
    private String analysisid;
}
