package com.keensense.schedule.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Table;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * 1400标准人员类
 */
@Data
@Table(TableName = "objext_result")
@ToString(callSuper=true, includeFieldNames=true)
public class PersonResult extends Result implements java.io.Serializable {

    private static final long serialVersionUID = 1123L;

    /**
     * 人员标识
     */
    @JSONField(name= "PersonID")
    @Column(ColumnName = "personid")
    private String personID;

    /**
     * 位置标记时间
     */
    @JSONField(name= "LocationMarkTime", format = "yyyyMMddHHmmss")
    @Column(ColumnName = "locationmarktime")
    private Date locationMarkTime;

    /**
     * 人员出现时间
     */
    @JSONField(name= "PersonAppearTime", format = "yyyyMMddHHmmss")
    @Column(ColumnName = "personappeartime")
    private Date personAppearTime;

    /**
     * 人员消失时间
     */
    @JSONField(name= "PersonDisAppearTime", format = "yyyyMMddHHmmss")
    @Column(ColumnName = "persondisappeartime")
    private Date personDisAppearTime;

    /**
     * 证件种类
     */
    @JSONField(name= "IdType")
    @Column(ColumnName = "idtype")
    private String idType;

    /**
     * 证件号码：有效证件号码
     */
    @JSONField(name= "IdNumber")
    @Column(ColumnName = "idnumber")
    private String idNumber;

    /**
     * 姓名：人员的中文姓名全称
     */
    @JSONField(name= "Name")
    @Column(ColumnName = "name")
    private String name;

    /**
     * 曾用名：曾经在户籍管理部门正式登记注册、人事档案
     */
    @JSONField(name= "UsedName")
    @Column(ColumnName = "usedname")
    private String usedName;

    /**
     * 绰号：使用姓名及曾用名之外的名称
     */
    @JSONField(name= "Alias")
    @Column(ColumnName = "alias")
    private String alias;

    /**
     * 性别代码
     */
    @JSONField(name= "GenderCode")
    @Column(ColumnName = "gendercode")
    private String genderCode;

    /**
     * 年龄上限：最大可能年龄
     */
    @JSONField(name= "AgeUpLimit")
    @Column(ColumnName = "ageuplimit")
    private Integer ageUpLimit;

    /**
     * 年龄下限：最小可能年龄
     */
    @JSONField(name= "AgeLowerLimit")
    @Column(ColumnName = "agelowerlimit")
    private Integer ageLowerLimit;

    /**
     * 民族代码：中国各名族的罗马字母拼写法和代码
     */
    @JSONField(name= "EthicCode")
    @Column(ColumnName = "ethiccode")
    private String ethicCode;

    /**
     * 国籍代码：世界各国和地区名称代码
     */
    @JSONField(name= "NationalityCode")
    @Column(ColumnName = "nationalitycode")
    private String nationalityCode;

    /**
     * 籍贯省市县
     */
    @JSONField(name= "NativeCityCode")
    @Column(ColumnName = "nativecitycode")
    private String nativeCityCode;

    /**
     * 居住地行政区划
     */
    @JSONField(name= "ResidenceAdminDivision")
    @Column(ColumnName = "residenceadmindivision")
    private String residenceAdminDivision;

    /**
     * 汉语口音代码：汉语口音编码规则
     */
    @JSONField(name= "ChineseAccentCode")
    @Column(ColumnName = "chineseaccentcode")
    private String chineseAccentCode;

    /**
     * 单位名称：人员所在的工作单位
     */
    @JSONField(name= "PersonOrg")
    @Column(ColumnName = "personorg")
    private String personOrg;

    /**
     * 职业类别代码：职业分类与代码，不包含代码中“—”
     */
    @JSONField(name= "JobCategory")
    @Column(ColumnName = "jobcategory")
    private String jobCategory;

    /**
     * 同行人数：被标注人的同行人数
     */
    @JSONField(name= "AccompanyNumber")
    @Column(ColumnName = "accompanynumber")
    private Integer accompanyNumber;

    /**
     * 身高上限：人的身高最大可能值，单位为厘米（cm）
     */
    @JSONField(name= "HeightUpLimit")
    @Column(ColumnName = "heightuplimit")
    private Integer heightUpLimit;

    /**
     * 身高下限：人的身高最小可能值，单位为厘米（cm）
     */
    @JSONField(name= "HeightLowerLimit")
    @Column(ColumnName = "heightlowerlimit")
    private Integer heightLowerLimit;

    /**
     * 体型
     */
    @JSONField(name= "BodyType")
    @Column(ColumnName = "bodytype")
    private String bodyType;

    /**
     * 肤色
     */
    @JSONField(name= "SkinColor")
    @Column(ColumnName = "skincolor")
    private String skinColor;

    /**
     * 发型
     */
    @JSONField(name= "HairStyle")
    @Column(ColumnName = "hairstyle")
    private String hairStyle;

    /**
     * 发色
     */
    @JSONField(name= "HairColor")
    @Column(ColumnName = "haircolor")
    private String hairColor;

    /**
     * 姿态
     */
    @JSONField(name= "Gesture")
    @Column(ColumnName = "gesture")
    private String gesture;

    /**
     * 状态
     */
    @JSONField(name= "Status")
    @Column(ColumnName = "status")
    private String status;

    /**
     * 脸型
     */
    @JSONField(name= "FaceStyle")
    @Column(ColumnName = "facestyle")
    private String faceStyle;

    /**
     * 脸部特征
     */
    @JSONField(name= "FacialFeature")
    @Column(ColumnName = "facialfeature")
    private String facialFeature;

    /**
     * 体貌特征
     */
    @JSONField(name= "PhysicalFeature")
    @Column(ColumnName = "physicalfeature")
    private String physicalFeature;

    /**
     * 体表特征
     */
    @JSONField(name= "BodyFeature")
    @Column(ColumnName = "bodyfeature")
    private String bodyFeature;

    /**
     * 习惯动作
     */
    @JSONField(name= "HabitualMovement")
    @Column(ColumnName = "habitualmovement")
    private String habitualMovement;

    /**
     * 行为
     */
    @JSONField(name= "Behavior")
    @Column(ColumnName = "behavior")
    private String behavior;

    /**
     * 行为描述：对行为项备注中没有的行为进行描述
     */
    @JSONField(name= "BehaviorDescription")
    @Column(ColumnName = "behaviordescription")
    private String behaviorDescription;

    /**
     * 附属物：当有多个时用英文半角分号”
     */
    @JSONField(name= "Appendant")
    @Column(ColumnName = "appendant")
    private String appendant;

    /**
     * 附属物描述
     */
    @JSONField(name= "AppendantDescription")
    @Column(ColumnName = "appendantdescription")
    private String appendantDescription;

    /**
     * 伞颜色
     */
    @JSONField(name= "UmbrellaColor")
    @Column(ColumnName = "umbrellacolor")
    private String umbrellaColor;

    /**
     * 口罩颜色
     */
    @JSONField(name= "RespiratorColor")
    @Column(ColumnName = "respiratorcolor")
    private String respiratorColor;

    /**
     * 帽子款式
     */
    @JSONField(name= "CapStyle")
    @Column(ColumnName = "capstyle")
    private String capStyle;

    /**
     * 帽子颜色
     */
    @JSONField(name= "CapColor")
    @Column(ColumnName = "capcolor")
    private String capColor;

    /**
     * 眼镜款式
     */
    @JSONField(name= "GlassStyle")
    @Column(ColumnName = "glassstyle")
    private String glassStyle;

    /**
     * 眼镜颜色
     */
    @JSONField(name= "GlassColor")
    @Column(ColumnName = "glasscolor")
    private String glassColor;

    /**
     * 围巾颜色
     */
    @JSONField(name= "ScarfColor")
    @Column(ColumnName = "scarfcolor")
    private String scarfColor;

    /**
     * 包款式
     */
    @JSONField(name= "BagStyle")
    @Column(ColumnName = "bagstyle")
    private String bagStyle;

    /**
     * 包颜色
     */
    @JSONField(name= "BagColor")
    @Column(ColumnName = "bagcolor")
    private String bagColor;


    /**
     * 上衣款式
     */
    @JSONField(name= "CoatStyle")
    @Column(ColumnName = "coatstyle")
    private String coatStyle;

    /**
     * 上衣长度
     */
    @JSONField(name= "CoatLength")
    @Column(ColumnName = "coatlength")
    private String coatLength;

    /**
     * 上衣颜色
     */
    @JSONField(name= "CoatColor")
    @Column(ColumnName = "coatcolor")
    private String coatColor;

    /**
     * 裤子款式
     */
    @JSONField(name= "TrousersStyle")
    @Column(ColumnName = "trousersstyle")
    private String trousersStyle;

    /**
     * 裤子颜色
     */
    @JSONField(name= "TrousersColor")
    @Column(ColumnName = "trouserscolor")
    private String trousersColor;

    /**
     * 裤子长度
     */
    @JSONField(name= "TrousersLen")
    @Column(ColumnName = "trouserslen")
    private String trousersLen;

    /**
     * 鞋子款式
     */
    @JSONField(name= "ShoesStyle")
    @Column(ColumnName = "shoesstyle")
    private String shoesStyle;

    /**
     * 鞋子颜色
     */
    @JSONField(name= "ShoesColor")
    @Column(ColumnName = "shoescolor")
    private String shoesColor;

    /**
     * 是否驾驶员：人工采集时必选 0：否；1：是；2：不确定
     */
    @JSONField(name= "IsDriver")
    @Column(ColumnName = "isdriver")
    private Integer isDriver;

    /**
     * 是否涉外人员：0：否；1：是；2：不确定
     */
    @JSONField(name= "IsForeigner")
    @Column(ColumnName = "isforeigner")
    private Integer isForeigner;

    /**
     * 护照证件种类
     */
    @JSONField(name= "PassportType")
    @Column(ColumnName = "passporttype")
    private String passportType;

    /**
     * 出入境人员类别代码：出入境人员分类代码
     */
    @JSONField(name= "ImmigrantTypeCode")
    @Column(ColumnName = "immigranttypecode")
    private String immigrantTypeCode;

    /**
     * 是否涉恐人员：0：否；1：是；2：不确定
     */
    @JSONField(name= "IsSuspectedTerrorist")
    @Column(ColumnName = "issuspectedterrorist")
    private Integer isSuspectedTerrorist;

    /**
     * 涉恐人员编号
     */
    @JSONField(name= "SuspectedTerroristNumber")
    @Column(ColumnName = "suspectedterroristnumber")
    private String suspectedTerroristNumber;

    /**
     * 是否涉案人员：0：否；1：是；2：不确定
     */
    @JSONField(name= "IsCriminalInvolved")
    @Column(ColumnName = "iscriminalinvolved")
    private Integer isCriminalInvolved;

    /**
     * 涉案人员专长代码
     */
    @JSONField(name= "CriminalInvolvedSpecilisationCode")
    @Column(ColumnName = "criminalinvolvedspecilisationcode")
    private String criminalInvolvedSpecilisationCode;

    /**
     * 体表特殊标记
     */
    @JSONField(name= "BodySpeciallMark")
    @Column(ColumnName = "bodyspeciallmark")
    private String bodySpeciallMark;

    /**
     * 作案手段
     */
    @JSONField(name= "CrimeMethod")
    @Column(ColumnName = "crimemethod")
    private String crimeMethod;

    /**
     * 作案特点代码
     */
    @JSONField(name= "CrimeCharacterCode")
    @Column(ColumnName = "crimecharactercode")
    private String crimeCharacterCode;

    /**
     * 在逃人员编号
     */
    @JSONField(name= "EscapedCriminalNumber")
    @Column(ColumnName = "escapedcriminalnumber")
    private String escapedCriminalNumber;

    /**
     * 是否在押人员：0：否；1：是；2：不确定，人工采集必填
     */
    @JSONField(name= "IsDetainees")
    @Column(ColumnName = "isdetainees")
    private Integer isDetainees;

    /**
     * 看守所编码
     */
    @JSONField(name= "DetentionHouseCode")
    @Column(ColumnName = "detentionhousecode")
    private String detentionHouseCode;

    /**
     * 在押人员身份：详细取值见附录 B
     */
    @JSONField(name= "DetaineesIdentity")
    @Column(ColumnName = "detaineesidentity")
    private String detaineesIdentity;

    /**
     * 在押人员特殊身份
     */
    @JSONField(name= "DetaineesSpecialIdentity")
    @Column(ColumnName = "detaineesspecialidentity")
    private String detaineesSpecialIdentity;

    /**
     * 成员类型代码
     */
    @JSONField(name= "MemberTypeCode")
    @Column(ColumnName = "membertypecode")
    private String memberTypeCode;

    /**
     * 是否被害人：人工采集时必选 0：否；1：是；2：不确定
     */
    @JSONField(name= "IsVictim")
    @Column(ColumnName = "isvictim")
    private Integer isVictim;

    /**
     *  被害人种类
     */
    @JSONField(name= "VictimType")
    @Column(ColumnName = "victimtype")
    private String victimType;

    /**
     * 受伤害程度
     */
    @JSONField(name= "InjuredDegree")
    @Column(ColumnName = "injureddegree")
    private String injuredDegree;

    /**
     * 尸体状况代码
     */
    @JSONField(name= "CorpseConditionCode")
    @Column(ColumnName = "corpseconditioncode")
    private String corpseConditionCode;

    /**
     * 是否可疑人：人工采集时必选 0：否；1：是；2：不确定
     */
    @JSONField(name= "IsSuspiciousPerson")
    @Column(ColumnName = "issuspiciousperson")
    private Integer isSuspiciousPerson;

    // *****************data模块附加字段******************

    /**
     * 上半身颜色标签1
     */
    @JSONField(name= "UpcolorTag1")
    @Column(ColumnName = "upcolortag1")
    private Integer upcolorTag1;

    /**
     * 上半身颜色标签2
     */
    @JSONField(name= "UpcolorTag2")
    @Column(ColumnName = "upcolortag2")
    private Integer upcolorTag2;

    /**
     * 下半身颜色标签1
     */
    @JSONField(name= "LowcolorTag1")
    @Column(ColumnName = "lowcolortag1")
    private Integer lowcolorTag1;

    /**
     * 下半身颜色标签2
     */
    @JSONField(name= "LowcolorTag2")
    @Column(ColumnName = "lowcolortag2")
    private Integer lowcolorTag2;

    /**
     * 主颜色标签1
     */
    @JSONField(name= "MaincolorTag1")
    @Column(ColumnName = "maincolortag1")
    private Integer maincolorTag1;

    /**
     * 主颜色标签2
     */
    @JSONField(name= "MaincolorTag2")
    @Column(ColumnName = "maincolortag2")
    private Integer maincolorTag2;

    /**
     * 主颜色标签3
     */
    @JSONField(name= "MaincolorTag3")
    @Column(ColumnName = "maincolortag3")
    private Integer maincolorTag3;

    /**
     * 角度,0:未知,128:正面 256:侧面，512:背面
     */
    @JSONField(name= "Angle")
    @Column(ColumnName = "angle")
    private Integer angle;

    /**
     * 是否有手提包,-1:未知,1:有手提包 0:没有手提包
     */
    @JSONField(name= "Handbag")
    @Column(ColumnName = "handbag")
    private Integer handbag;

    /**
     * 抓拍的人脸图片
     */
    @JSONField(name= "FaceImgUrl")
    @Column(ColumnName = "faceimgurl")
    private String faceImgurl;

    /**
     * 帽子
     */
    @JSONField(name= "Cap")
    @Column(ColumnName = "cap")
    private Integer cap;

    /**
     * 上衣纹理
     */
    @JSONField(name= "CoatTexture")
    @Column(ColumnName = "coattexture")
    private Integer coatTexture;

    /**
     * 下衣纹理
     */
    @JSONField(name= "TrousersTexture")
    @Column(ColumnName = "trouserstexture")
    private Integer trousersTexture;

    /**
     * 手推车
     */
    @JSONField(name= "Trolley")
    @Column(ColumnName = "trolley")
    private Integer trolley;

    /**
     * 拉杆箱
     */
    @JSONField(name= "Luggage")
    @Column(ColumnName = "luggage")
    private Integer luggage;

    /**
     * 关联人脸ID
     */
    @JSONField(name= "FaceUUID")
    @Column(ColumnName = "faceuuid")
    private String faceUUID;

    /**
     *
     */
    @JSONField(name= "Proportion")
    @Column(ColumnName = "proportion")
    private String proportion;

    /**
     *
     */
    @JSONField(name= "ArchivesID")
    @Column(ColumnName = "archivesid")
    private String archivesID;

    /**
     *
     */
    @JSONField(name= "TrailSource")
    @Column(ColumnName = "trailsource")
    private String trailSource;

    /**
     *
     */
    @JSONField(name= "BodyScore")
    @Column(ColumnName = "bodyscore")
    private Float bodyScore = 1.0f;

    // *****************kafka数据附加字段*****************

    /**
     * 行人/骑车人是否有戴口罩. objType为HUMAN或BIKE时, 数值包括: -1-未知, 0-没戴口罩, 1-有戴口罩
     */
    @JSONField(name= "Respirator")
    @Column(ColumnName = "respirator")
    private Integer respirator;

    /**
     * 行人/骑车人是否有背包 参考配置文件objext_transcode.json配置节点bag
     */
    @JSONField(name= "Bag")
    @Column(ColumnName = "bag")
    private Integer bag;

    /**
     * 行人/骑车人是否有手提包 参考配置文件objext_transcode.json配置节点carryBag
     */
    @JSONField(name= "CarryBag")
    @Column(ColumnName = "carrybag")
    private Integer carryBag;

    /**
     * 行人/骑车人是否有戴眼镜 参考配置文件objext_transcode.json配置节点glasses
     */
    @JSONField(name= "Glasses")
    @Column(ColumnName = "glasses")
    private Integer glasses;

    /**
     * 行人/骑车人是否有打伞 参考配置文件objext_transcode.json配置节点umbrella
     */
    @JSONField(name= "Umbrella")
    @Column(ColumnName = "umbrella")
    private Integer umbrella;

    /**
     * 是否手持棍棒
     */
    @JSONField(name= "HasKnife")
    @Column(ColumnName = "hasknife")
    private Integer hasKnife;

    /**
     * 是否抱小孩 (1是，0为否，-1未知)
     */
    @JSONField(name= "ChestHold")
    @Column(ColumnName = "chesthold")
    private Integer chestHold;

    /**
     * 行人体态（1正常，2 胖，3瘦，-1 未知）
     */
    @JSONField(name= "Shape")
    @Column(ColumnName = "shape")
    private Integer shape;

    /**
     * 名族  (1为少数民族，0为汉族，-1未知)
     */
    @JSONField(name= "Minority")
    @Column(ColumnName = "minority")
    private Integer minority;

    /**
     * 頭部物框字段
     */
    @JSONField(name= "HeadLeftTopX")
    @Column(ColumnName = "headlefttopx")
    private Integer headlefttopx;

    /**
     * 頭部物框字段
     */
    @JSONField(name= "HeadLeftTopY")
    @Column(ColumnName = "headlefttopy")
    private Integer headlefttopy;

    /**
     * 頭部物框字段
     */
    @JSONField(name= "HeadRightBtmX")
    @Column(ColumnName = "headrightbtmx")
    private Integer headrightbtmx;

    /**
     * 頭部物框字段
     */
    @JSONField(name= "HeadRightBtmY")
    @Column(ColumnName = "headrightbtmy")
    private Integer headrightbtmy;

    /**
     * 上半身物框字段
     */
    @JSONField(name= "UpperLeftTopX")
    @Column(ColumnName = "upperlefttopx")
    private Integer upperlefttopx;

    /**
     * 上半身物框字段
     */
    @JSONField(name= "UpperLeftTopY")
    @Column(ColumnName = "upperlefttopy")
    private Integer upperlefttopy;

    /**
     * 上半身物框字段
     */
    @JSONField(name= "UpperRightBtmX")
    @Column(ColumnName = "upperrightbtmx")
    private Integer upperrightbtmx;

    /**
     * 上半身物框字段
     */
    @JSONField(name= "UpperRightBtmY")
    @Column(ColumnName = "upperrightbtmy")
    private Integer upperrightbtmy;

    /**
     * 下半身物框字段
     */
    @JSONField(name= "LowerLeftTopX")
    @Column(ColumnName = "lowerlefttopx")
    private Integer lowerlefttopx;

    /**
     * 下半身物框字段
     */
    @JSONField(name= "LowerLeftTopY")
    @Column(ColumnName = "lowerlefttopy")
    private Integer lowerlefttopy;

    /**
     * 下半身物框字段
     */
    @JSONField(name= "LowerRightBtmX")
    @Column(ColumnName = "lowerrightbtmx")
    private Integer lowerrightbtmx;

    /**
     * 下半身物框字段
     */
    @JSONField(name= "LowerRightBtmY")
    @Column(ColumnName = "lowerrightbtmy")
    private Integer lowerrightbtmy;

}
