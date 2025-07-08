package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:00 2019/11/30
 * @Version v0.1
 */
@Data
@ApiModel("更新细类属性")
public class ResultUpdateRequest extends PageRequest implements Serializable {


    @ApiModelProperty("类型 [1:人, 2:车, 3:人脸, 4:骑行, 默认查全部]")
    private String type;
    @ApiModelProperty("uuid")
    private String uuid;

    /**
     * 人形
     */
    @ApiModelProperty("性别 [1男, 2女 -1未知]")
    private String sex;

    @ApiModelProperty("年龄 [4小孩, 8青年, 16中年, 32老年, -1未知]")
    private String age;

    @ApiModelProperty("上衣颜色")
    private String upcolorStr;

    @ApiModelProperty("上衣款式 [1长袖, 2短袖, -1未知]")
    private String coatStyle;

    @ApiModelProperty("下衣颜色")
    private String trousersColor;

    @ApiModelProperty("下衣款式 [1长裤, 2短裤, 3裙子, -1未知]")
    private String trousersStyle;

    @ApiModelProperty("姿态 [1正面, 2侧面, 3背面, -1未知]")
    private String angle;

    @ApiModelProperty("手提包")
    private String handbag;

    @ApiModelProperty("打伞")
    private String umbrella;

    @ApiModelProperty("眼镜[1是, 0否, -1未知]")
    private String glasses;

    @ApiModelProperty("帽子[1是, 0否, -1未知]")
    private String cap;

    @ApiModelProperty("口罩[1是, 0否, -1未知]")
    private String respirator;

    @ApiModelProperty("是否背包 [1是, 0否, -1未知]")
    private String bag;

    @ApiModelProperty("发型 [-1:未知,1:长发,2:短发]")
    private String hairStyle;

    @ApiModelProperty("手推车 [1 有 0 没有  -1 未知]")
    private String trolley;

    @ApiModelProperty("拉杆箱 [1有 0 没有 -1 未知]")
    private String luggage;

    @ApiModelProperty("上衣纹理 [-1:未知,1:净色,2:间条,3:格子,4:图案,5:拼接]")
    private String coatTexture;

    @ApiModelProperty("下衣纹理 [-1:未知,1:净色,2:间条,3:图案]")
    private String trousersTexture;

    @ApiModelProperty("手持刀棍 [-1:未知,1:是,0:否]")
    private String hasKnife;

    @ApiModelProperty("体态 [-1:未知,1:正常,2:胖,3:瘦]")
    private String shape;

    @ApiModelProperty("抱小孩 [-1:未知,1:是,0:否]")
    private String chestHold;

    @ApiModelProperty("民族 [-1:未知,1:少数民族,0:汉族]")
    private String minority;

    /**
     * 骑行
     */
    @ApiModelProperty("性别 [1男, 2女 -1未知]")
    private String bikeSex;

    @ApiModelProperty("类型[1、2：男士摩托车；3：自行车 5:三轮车。支持多选，以“,”分隔]")
    private String bikeGenre;

    @ApiModelProperty("上身颜色[]")
    private String passengersUpColorStr;

    @ApiModelProperty("年龄 [4小孩, 8青年, 16中年, 32老年, -1未知]")
    private String bikeAge;

    @ApiModelProperty("上衣款式[1长袖, 2短袖, -1未知]")
    private String bikeCoatStyle;

    @ApiModelProperty("上衣纹理 [-1:未知,1:净色,2:间条,3:格子,4:图案,5:拼接]")
    private String bikeCoatTexture;

    @ApiModelProperty("方向 [1正面, 2侧面, 3背面, -1未知]")
    private String bikeAngle;

    @ApiModelProperty("眼镜[1是, 0否, -1未知]")
    private String bikeGlasses;

    @ApiModelProperty("口罩[1是, 0否, -1未知]")
    private String bikeRespirator;

    @ApiModelProperty("是否背包 [1是, 0否, -1未知]")
    private String bikeBag;

    @ApiModelProperty("类别[2 二轮车, 3 三轮车, -1未知]")
    private String wheels;

    @ApiModelProperty("是否戴头盔[1是, 0否, -1未知]")
    private String helmet;

    @ApiModelProperty("头盔颜色[]")
    private String helmetcolorStr;

    @ApiModelProperty("是否挂牌[-1:未知,1:挂牌 0:不挂牌]")
    private String bikeHasPlate;

    @ApiModelProperty("是否打伞[1 打伞, 0 未打伞, -1未知]")
    private String bikeUmbrella;

    @ApiModelProperty("车灯形状[1-三角，2 菱形，-1未知]")
    private String lampShape;

    @ApiModelProperty("是否载客[1是，0为否，-1未知]")
    private String carryPassenger;

    /**
     * 车辆
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
    private String mainDriver;

    @ApiModelProperty("副驾驶是否系安全带 [1有,0没有]")
    private String coDriver;

    @ApiModelProperty("挂饰 [1有 0没有]")
    private String drop;

    @ApiModelProperty("纸巾盒 [1有 0没有]")
    private String tissueBox;

    @ApiModelProperty("是否打电话 [1有 0没有]")
    private String hasCall;

    @ApiModelProperty("年检标数量：[0表示没有，1表示1个，2表示2个]")
    private String tagNum;

    @ApiModelProperty("遮阳板 [1收起 0放下]")
    private String sun;

    @ApiModelProperty("危化品车 [1是 0否]")
    private String danger;

    @ApiModelProperty("天窗 [1有 0没有]")
    private String sunRoof;

    @ApiModelProperty("行李架 [1有 0没有]")
    private String rack;

    @ApiModelProperty("天线 [1有 0没有]")
    private String aerial;

    @ApiModelProperty("车牌颜色")
    private String plateColorCode;

    @ApiModelProperty("行驶方向")
    private  String direction;

    @ApiModelProperty("摆件 [1有 0没有]")
    private String decoration;

    @ApiModelProperty("是否有牌(1:是 0:否 -1:未知")
    private String vehicleHasPlate;

    @ApiModelProperty("车速 1:是 0:否 -1:未知")
    private String carSpeed;

    @ApiModelProperty("姿态 1正面, 2侧面, 3背面, -1未知")
    private String vehicleAngle;

    @ApiModelProperty("车牌类型")
    private String plateClass;

    @ApiModelProperty("撞损车")
    private String hasCrash;

    @ApiModelProperty("危化品车")
    private String hasDanger;

    @ApiModelProperty("年款")
    private String vehicleStyle;
}
