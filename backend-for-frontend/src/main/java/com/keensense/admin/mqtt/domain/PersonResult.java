package com.keensense.admin.mqtt.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * 1400标准人员类
 */
@Data
public class PersonResult extends Result implements java.io.Serializable {

    private static final long serialVersionUID = -7104665250657410207L;

    /**
     * 人员标识
     */
    @JSONField(name= "PersonID")
    private String personID;

    /**
     * 位置标记时间
     */
    @JSONField(name= "LocationMarkTime", format = "yyyyMMddHHmmss")
    private Date locationMarkTime;

    /**
     * 人员出现时间
     */
    @JSONField(name= "PersonAppearTime", format = "yyyyMMddHHmmss")
    private Date personAppearTime;

    /**
     * 人员消失时间
     */
    @JSONField(name= "PersonDisAppearTime", format = "yyyyMMddHHmmss")
    private Date personDisAppearTime;

    /**
     * 证件种类
     */
    @JSONField(name= "IdType")
    private String idType;

    /**
     * 证件号码：有效证件号码
     */
    @JSONField(name= "IdNumber")
    private String idNumber;

    /**
     * 姓名：人员的中文姓名全称
     */
    @JSONField(name= "Name")
    private String name;

    /**
     * 曾用名：曾经在户籍管理部门正式登记注册、人事档案
     */
    @JSONField(name= "UsedName")
    private String usedName;

    /**
     * 绰号：使用姓名及曾用名之外的名称
     */
    @JSONField(name= "Alias")
    private String alias;

    /**
     * 性别代码
     */
    @JSONField(name= "GenderCode")
    private String genderCode;

    /**
     * 年龄上限：最大可能年龄
     */
    @JSONField(name= "AgeUpLimit")
    private Integer ageUpLimit;

    /**
     * 年龄下限：最小可能年龄
     */
    @JSONField(name= "AgeLowerLimit")
    private Integer ageLowerLimit;

    /**
     * 民族代码：中国各名族的罗马字母拼写法和代码
     */
    @JSONField(name= "EthicCode")
    private String ethicCode;

    /**
     * 国籍代码：世界各国和地区名称代码
     */
    @JSONField(name= "NationalityCode")
    private String nationalityCode;

    /**
     * 籍贯省市县
     */
    @JSONField(name= "NativeCityCode")
    private String nativeCityCode;

    /**
     * 居住地行政区划
     */
    @JSONField(name= "ResidenceAdminDivision")
    private String residenceAdminDivision;

    /**
     * 汉语口音代码：汉语口音编码规则
     */
    @JSONField(name= "ChineseAccentCode")
    private String chineseAccentCode;

    /**
     * 单位名称：人员所在的工作单位
     */
    @JSONField(name= "PersonOrg")
    private String personOrg;

    /**
     * 职业类别代码：职业分类与代码，不包含代码中“—”
     */
    @JSONField(name= "JobCategory")
    private String jobCategory;

    /**
     * 同行人数：被标注人的同行人数
     */
    @JSONField(name= "AccompanyNumber")
    private Integer accompanyNumber;

    /**
     * 身高上限：人的身高最大可能值，单位为厘米（cm）
     */
    @JSONField(name= "HeightUpLimit")
    private Integer heightUpLimit;

    /**
     * 身高下限：人的身高最小可能值，单位为厘米（cm）
     */
    @JSONField(name= "HeightLowerLimit")
    private Integer heightLowerLimit;

    /**
     * 体型
     */
    @JSONField(name= "BodyType")
    private String bodyType;

    /**
     * 肤色
     */
    @JSONField(name= "SkinColor")
    private String skinColor;

    /**
     * 发型
     */
    @JSONField(name= "HairStyle")
    private String hairStyle;

    /**
     * 发色
     */
    @JSONField(name= "HairColor")
    private String hairColor;

    /**
     * 姿态
     */
    @JSONField(name= "Gesture")
    private String gesture;

    /**
     * 状态
     */
    @JSONField(name= "Status")
    private String status;

    /**
     * 脸型
     */
    @JSONField(name= "FaceStyle")
    private String faceStyle;

    /**
     * 脸部特征
     */
    @JSONField(name= "FacialFeature")
    private String facialFeature;

    /**
     * 体貌特征
     */
    @JSONField(name= "PhysicalFeature")
    private String physicalFeature;

    /**
     * 体表特征
     */
    @JSONField(name= "BodyFeature")
    private String bodyFeature;

    /**
     * 习惯动作
     */
    @JSONField(name= "BabitualMovement")
    private String habitualMovement;

    /**
     * 行为
     */
    @JSONField(name= "Behavior")
    private String behavior;

    /**
     * 行为描述：对行为项备注中没有的行为进行描述
     */
    @JSONField(name= "BehaviorDescription")
    private String behaviorDescription;

    /**
     * 附属物：当有多个时用英文半角分号”
     */
    @JSONField(name= "Appendant")
    private String appendant;

    /**
     * 附属物描述
     */
    @JSONField(name= "AppendantDescription")
    private String appendantDescription;

    /**
     * 伞颜色
     */
    @JSONField(name= "UmbrellaColor")
    private String umbrellaColor;

    /**
     * 口罩颜色
     */
    @JSONField(name= "RespiratorColor")
    private String respiratorColor;

    /**
     * 帽子款式
     */
    @JSONField(name= "CapStyle")
    private String capStyle;

    /**
     * 帽子颜色
     */
    @JSONField(name= "CapColor")
    private String capColor;

    /**
     * 眼镜款式
     */
    @JSONField(name= "GlassStyle")
    private String glassStyle;

    /**
     * 眼镜颜色
     */
    @JSONField(name= "GlassColor")
    private String glassColor;

    /**
     * 围巾颜色
     */
    @JSONField(name= "ScarfColor")
    private String scarfColor;

    /**
     * 包款式
     */
    @JSONField(name= "BagStyle")
    private String bagStyle;

    /**
     * 包颜色
     */
    @JSONField(name= "BagColor")
    private String bagColor;


    /**
     * 上衣款式
     */
    @JSONField(name= "CoatStyle")
    private String coatStyle;

    /**
     * 上衣长度
     */
    @JSONField(name= "CoatLength")
    private String coatLength;

    /**
     * 上衣颜色
     */
    @JSONField(name= "CoatColor")
    private String coatColor;

    /**
     * 裤子款式
     */
    @JSONField(name= "TrousersStyle")
    private String trousersStyle;

    /**
     * 裤子颜色
     */
    @JSONField(name= "TrousersColor")
    private String trousersColor;

    /**
     * 裤子长度
     */
    @JSONField(name= "TrousersLen")
    private String trousersLen;

    /**
     * 鞋子款式
     */
    @JSONField(name= "ShoesStyle")
    private String shoesStyle;

    /**
     * 鞋子颜色
     */
    @JSONField(name= "ShoesColor")
    private String shoesColor;

    /**
     * 是否驾驶员：人工采集时必选 0：否；1：是；2：不确定
     */
    @JSONField(name= "IsDriver")
    private Integer isDriver;

    /**
     * 是否涉外人员：0：否；1：是；2：不确定
     */
    @JSONField(name= "IsForeigner")
    private Integer isForeigner;

    /**
     * 护照证件种类
     */
    @JSONField(name= "PassportType")
    private String passportType;

    /**
     * 出入境人员类别代码：出入境人员分类代码
     */
    @JSONField(name= "ImmigrantTypeCode")
    private String immigrantTypeCode;

    /**
     * 是否涉恐人员：0：否；1：是；2：不确定
     */
    @JSONField(name= "IsSuspectedTerrorist")
    private Integer isSuspectedTerrorist;

    /**
     * 涉恐人员编号
     */
    @JSONField(name= "SuspectedTerroristNumber")
    private String suspectedTerroristNumber;

    /**
     * 是否涉案人员：0：否；1：是；2：不确定
     */
    @JSONField(name= "IsCriminalInvolved")
    private Integer isCriminalInvolved;

    /**
     * 涉案人员专长代码
     */
    @JSONField(name= "CriminalInvolvedSpecilisationCode")
    private String criminalInvolvedSpecilisationCode;

    /**
     * 体表特殊标记
     */
    @JSONField(name= "BodySpeciallMark")
    private String bodySpeciallMark;

    /**
     * 作案手段
     */
    @JSONField(name= "CrimeMethod")
    private String crimeMethod;

    /**
     * 作案特点代码
     */
    @JSONField(name= "CrimeCharacterCode")
    private String crimeCharacterCode;

    /**
     * 在逃人员编号
     */
    @JSONField(name= "EscapedCriminalNumber")
    private String escapedCriminalNumber;

    /**
     * 是否在押人员：0：否；1：是；2：不确定，人工采集必填
     */
    @JSONField(name= "IsDetainees")
    private Integer isDetainees;

    /**
     * 看守所编码
     */
    @JSONField(name= "DetentionHouseCode")
    private String detentionHouseCode;

    /**
     * 在押人员身份：详细取值见附录 B
     */
    @JSONField(name= "DetaineesIdentity")
    private String detaineesIdentity;

    /**
     * 在押人员特殊身份
     */
    @JSONField(name= "DetaineesSpecialIdentity")
    private String detaineesSpecialIdentity;

    /**
     * 成员类型代码
     */
    @JSONField(name= "MemberTypeCode")
    private String memberTypeCode;

    /**
     * 是否被害人：人工采集时必选 0：否；1：是；2：不确定
     */
    @JSONField(name= "IsVictim")
    private Integer isVictim;

    /**
     *  被害人种类
     */
    @JSONField(name= "VictimType")
    private String victimType;

    /**
     * 受伤害程度
     */
    @JSONField(name= "InjuredDegree")
    private String injuredDegree;

    /**
     * 尸体状况代码
     */
    @JSONField(name= "CorpseConditionCode")
    private String corpseConditionCode;

    /**
     * 是否可疑人：人工采集时必选 0：否；1：是；2：不确定
     */
    @JSONField(name= "IsSuspiciousPerson")
    private Integer isSuspiciousPerson;

    // *****************data模块附加字段******************

    /**
     * 上半身颜色标签1
     */
    @JSONField(name= "UpcolorTag1")
    private Integer upcolorTag1;

    /**
     * 上半身颜色标签2
     */
    @JSONField(name= "UpcolorTag2")
    private Integer upcolorTag2;

    /**
     * 下半身颜色标签1
     */
    @JSONField(name= "LowcolorTag1")
    private Integer lowcolorTag1;

    /**
     * 下半身颜色标签2
     */
    @JSONField(name= "LowcolorTag2")
    private Integer lowcolorTag2;

    /**
     * 主颜色标签1
     */
    @JSONField(name= "MaincolorTag1")
    private Integer maincolorTag1;

    /**
     * 主颜色标签2
     */
    @JSONField(name= "MaincolorTag2")
    private Integer maincolorTag2;

    /**
     * 主颜色标签3
     */
    @JSONField(name= "MaincolorTag3")
    private Integer maincolorTag3;

    /**
     * 角度,0:未知,128:正面 256:侧面，512:背面
     */
    @JSONField(name= "Angle")
    private Integer angle;

    /**
     * 是否有手提包,-1:未知,1:有手提包 0:没有手提包
     */
    @JSONField(name= "Handbag")
    private Integer handbag;

    /**
     * 抓拍的人脸图片
     */
    @JSONField(name= "FaceImgurl")
    private String faceImgurl;

    /**
     * 帽子
     */
    @JSONField(name= "Cap")
    private Integer cap;

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
     * 手推车
     */
    @JSONField(name= "Trolley")
    private Integer trolley;

    /**
     * 拉杆箱
     */
    @JSONField(name= "Luggage")
    private Integer luggage;

    /**
     * 关联人脸ID
     */
    @JSONField(name= "FaceUUID")
    private String faceUUID;

    /**
     *
     */
    @JSONField(name= "Proportion")
    private String proportion;

    /**
     *
     */
    @JSONField(name= "ArchivesID")
    private String archivesID;

    /**
     *
     */
    @JSONField(name= "TrailSource")
    private String trailSource;

    /**
     *
     */
    @JSONField(name= "BodyScore")
    private Float bodyScore = 1.0f;

    // *****************kafka数据附加字段*****************

    /**
     * 行人/骑车人是否有戴口罩. objType为HUMAN或BIKE时, 数值包括: -1-未知, 0-没戴口罩, 1-有戴口罩
     */
    @JSONField(name= "Respirator")
    private String respirator;

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
     * 行人/骑车人是否有背包 参考配置文件objext_transcode.json配置节点bag
     */
    @JSONField(name= "Bag")
    private String bag;

    /**
     * 行人/骑车人是否有手提包 参考配置文件objext_transcode.json配置节点carryBag
     */
    @JSONField(name= "CarryBag")
    private String carryBag;

    /**
     * 行人/骑车人是否有戴眼镜 参考配置文件objext_transcode.json配置节点glasses
     */
    @JSONField(name= "Glasses")
    private String glasses;

    /**
     * 行人/骑车人是否有打伞 参考配置文件objext_transcode.json配置节点umbrella
     */
    @JSONField(name= "Umbrella")
    private String umbrella;

    /**
     * 行人是否手持棍棒 是 1 不是 0 未知 -1
     */
    @JSONField(name= "HasKnife")
    private String hasKnife;


}
