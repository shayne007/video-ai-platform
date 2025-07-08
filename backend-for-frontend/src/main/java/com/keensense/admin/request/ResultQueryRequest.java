package com.keensense.admin.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("任务查询")
public class ResultQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -7304018136120643865L;

    @ApiModelProperty("类型 [1:人, 2:车, 3:人脸, 4:骑行, 默认查全部]")
    private String type;

    @ApiModelProperty("流水号")
    @NotBlank(message = "流水号不能为空")
    private String serialnumber;

    @ApiModelProperty("任务id")
    private String taskid;

    @ApiModelProperty("监控点Id")
    private String cameraId;

    @ApiModelProperty("时间降序升序 [desc 降序, asc 升序]")
    private String timeSelect;

    @ApiModelProperty("导出分类区别[2:离线,联网录像; 1:联网实时,IPC,抓拍机]")
    private String taskTypeCode;

    @ApiModelProperty("监控组Id")
    private String monitorGroupId;

    @ApiModelProperty(value = "导出开始页码")
    private int pageStart;

    @ApiModelProperty(value = "导出结束页码")
    private int pageEnd;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    @ApiModelProperty(value = "导出图片选择: 1小图, 2大图, 3大小图")
    private Integer exportImgType;

    /**
     * 人
     */
    @ApiModelProperty("性别 [1男, 2女 -1未知]")
    private Byte sex;

    @ApiModelProperty("年龄 [4小孩, 8青年, 16中年, 32老年, -1未知]")
    private Byte age;

    @ApiModelProperty("上衣 []")
    private String upcolorStr;

    @ApiModelProperty("上衣款式 [1长袖, 2短袖, -1未知]")
    private String coatStyle;

    @ApiModelProperty("下衣 []")
    private String lowcolorStr;

    @ApiModelProperty("下衣款式 [1长裤, 2短裤, 3裙子, -1未知]")
    private String trousersStyle;

    @ApiModelProperty("方向 [128正面, 256侧面, 512背面, -1未知]")
    private Integer angle;

    @ApiModelProperty("拎东西")
    private Integer handbag;

    @ApiModelProperty("打伞")
    private Integer umbrella;

    @ApiModelProperty("眼镜[1是, 0否, -1未知]")
    private String glasses;

    @ApiModelProperty("帽子[1是, 0否, -1未知]")
    private Integer cap;

    @ApiModelProperty("口罩[1是, 0否, -1未知]")
    private String respirator;

    @ApiModelProperty("是否背包 [1是, 0否, -1未知]")
    private Integer bag;


    @ApiModelProperty("发型 [-1:未知,1:长发,2:短发]")
    private Integer hairStyle;
    @ApiModelProperty("手推车 [1 有 0 没有  -1 未知]")
    private Integer trolley;
    @ApiModelProperty("拉杆箱 [1有 0 没有 -1 未知]")
    private Integer luggage;
    @ApiModelProperty("上衣纹理 [-1:未知,1:净色,2:间条,3:格子,4:图案,5:拼接]")
    private Integer coatTexture;
    @ApiModelProperty("下衣纹理 [-1:未知,1:净色,2:间条,3:图案]")
    private Integer trousersTexture;
    @ApiModelProperty("手持刀棍 [-1:未知,1:是,0:否]")
    private Integer hasKnife;
    @ApiModelProperty("异常行为检测标志 [-1:未知,1:入侵,2:越界,3:无]")
    private Integer overlineType;
    @ApiModelProperty("是否抱小孩[1是，0为否，-1未知]")
    private Integer chestHold;
    @ApiModelProperty("行人体态 1正常，2 胖，3瘦，-1 未知")
    private Integer shape;
    @ApiModelProperty("民族[1为少数民族，0为汉族，-1未知]")
    private Integer minority;


    /**
     * 人骑车
     */
    @ApiModelProperty("性别 [1男, 2女 -1未知]")
    private Integer bikeSex;

    @ApiModelProperty("类型[1：女士摩托车；2：男士摩托车；3：自行车；4：电动车；5:三轮车。支持多选，以“,”分隔]")
    private Integer bikeGenre;

    @ApiModelProperty("上身颜色[]")
    private String passengersUpColorStr;

    @ApiModelProperty("年龄 [4小孩, 8青年, 16中年, 32老年, -1未知]")
    private Integer bikeAge;

    @ApiModelProperty("上衣款式[1长袖, 2短袖, -1未知]")
    private Integer bikeCoatStyle;
    @ApiModelProperty("上衣纹理 [-1:未知,1:净色,2:间条,3:格子,4:图案,5:拼接]")
    private Integer bikeCoatTexture;
    @ApiModelProperty("方向 [128正面, 256侧面, 512背面, -1未知]")
    private Integer bikeAngle;
    @ApiModelProperty("眼镜[1是, 0否, -1未知]")
    private String bikeGlasses;
    @ApiModelProperty("口罩[1是, 0否, -1未知]")
    private String bikeRespirator;
    @ApiModelProperty("车身颜色[]")
    private String bikecolor;
    @ApiModelProperty("是否背包 [1是, 0否, -1未知]")
    private Integer bikeBag;

    @ApiModelProperty("类别[2 二轮车, 3 三轮车, -1未知]")
    private Byte wheels;

    @ApiModelProperty("是否戴头盔[1是, 0否, -1未知]")
    private String helmet;

    @ApiModelProperty("头盔颜色[]")
    private String helmetcolorStr;

    @ApiModelProperty("是否挂牌[-1:未知,1:挂牌 0:不挂牌]")
    private Short bikeHasPlate;

    @ApiModelProperty("是否打伞[1 打伞, 0 未打伞, -1未知]")
    private Integer bikeUmbrella;

    @ApiModelProperty("异常行为检测标志 [-1:未知,1:入侵,2:越界,3:无]")
    private Integer overlineTypeBike;

    @ApiModelProperty("车灯形状[1-三角，2 菱形，-1未知]")
    private String lampShape;

    @ApiModelProperty("是否载客[1是，0为否，-1未知]")
    private String carryPassenger;

    /**
     * 车  差天线
     */
    @ApiModelProperty("车牌号码")
    private String license;
    @ApiModelProperty("品牌")
    private String carlogo;
    @ApiModelProperty("车系")
    private String vehicleseries;
    @ApiModelProperty("车型[]")
    private String vehiclekind;
    @ApiModelProperty("车身颜色[]")
    private String carcolor;
    @ApiModelProperty("主驾驶是否系安全带 [1有,0没有]")
    private Integer mainDriver;
    @ApiModelProperty("副驾驶是否系安全带 [1有,0没有]")
    private Integer coDriver;
    @ApiModelProperty("挂饰 [1有 0没有]")
    private Byte drop;
    @ApiModelProperty("纸巾盒 [1有 0没有]")
    private Integer tissueBox;
    @ApiModelProperty("是否打电话 [1有 0没有]")
    private Integer hasCall;
    @ApiModelProperty("年检标数量：[0表示没有，1表示1个，2表示2个]")
    private Integer tagNum;
    @ApiModelProperty("遮阳板 [1收起 0放下]")
    private Byte sun;
    @ApiModelProperty("危化品车 [1是 0否]")
    private Byte hasDanger;
    @ApiModelProperty("撞损车车 [1是 0否]")
    private String hasCrash;
    @ApiModelProperty("天窗 [1有 0没有]")
    private Byte sunRoof;
    @ApiModelProperty("行李架 [1有 0没有]")
    private Byte rack;
    @ApiModelProperty("天线 [1是 0否]")
    private String aerial;
    @ApiModelProperty("车牌颜色 [1黄, 2蓝, 3黑, 4白, 5绿, 100黄绿]")
    private Integer plateColorCode;
    @ApiModelProperty("行驶方向")
    private  String direction;
    @ApiModelProperty("摆件 [1有 0没有]")
    private String decoration;
    @ApiModelProperty("异常行为检测标志 [-1:未知,1:入侵,2:越界,3:无]")
    private Integer overlineTypeCar;
    @ApiModelProperty("是否有车牌[1 是，0否，-1未知]")
    private Integer vehicleHasPlate;
    @ApiModelProperty("车牌类型[]")
    private String plateClass;
    @ApiModelProperty("姿态")
    private String vehicleAngle;

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

    @ApiModelProperty("车辆社会属性 1普通, 2外賣, 3快遞, -1未知")
    private Integer socialAttribute;
}
