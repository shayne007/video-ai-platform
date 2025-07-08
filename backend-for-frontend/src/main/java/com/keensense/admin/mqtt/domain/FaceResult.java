package com.keensense.admin.mqtt.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 1400标准人脸类
 */
@Data
public class FaceResult extends Result implements Serializable {

    private static final long serialVersionUID = 3144118679767429115L;

    /**
     * 人脸标识
     */
    @JSONField(name= "FaceID")
    private String faceID;

    /**
     * 位置标记时间：人工采集时有效
     */
    @JSONField(name= "LocationMarkTime", format = "yyyyMMddHHmmss")
    private Date locationMarkTime;

    /**
     * 人脸出现时间：人工采集时有效
     */
    @JSONField(name= "FaceAppearTime", format = "yyyyMMddHHmmss")
    private Date faceAppearTime;

    /**
     * 人脸消失时间：人工采集时有效
     */
    @JSONField(name= "FaceDisAppearTime", format = "yyyyMMddHHmmss")
    private Date faceDisAppearTime;

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
     * 被害人种类
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

    /**
     * 姿态分布：1：平视；2：微仰；3：微俯；4：左微侧脸；5：左斜侧脸；6：左全侧脸；7：右微侧脸；8：右斜侧脸；9：右全侧脸
     */
    @JSONField(name= "Attitude")
    private Integer attitude;

    /**
     * 相似度：人脸相似度，[0，1]
     */
    @JSONField(name= "Similaritydegree")
    private Double similaritydegree;

    /**
     * 眉型：1：上扬眉；2：一字眉；3：八字眉；4：淡眉毛 5：浓眉毛；6：弯眉；7：细眉；8：短眉毛；9：特殊眉； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name= "EyebrowStyle")
    private String eyebrowStyle;

    /**
     * 鼻型：1：普通鼻；2：扁鼻子；3：尖鼻子；4：朝天鼻；5：鹰钩鼻；6：蒜头鼻；7：宽鼻子；8：小鼻子；9：露孔鼻；10：特殊鼻； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name= "NoseStyle")
    private String noseStyle;


    /**
     * 胡型：1：一字胡；2：八字胡；3：淡胡子；4：络胡；5：山羊胡；6：花白胡；7：一点胡
     */
    @JSONField(name= "MustacheStyle")
    private String mustacheStyle;

    /**
     * 嘴唇：1：常见嘴；2：厚嘴唇；3：薄嘴唇；4：厚嘴巴；5：上翘嘴；6：下撇嘴；7：凸嘴；8：凹嘴；9：露齿嘴；10：小嘴； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name= "LipStyle")
    private String lipStyle;

    /**
     * 皱纹眼袋：1：抬头纹；2：左眼角纹；3：右眼角纹；4：眉间纹；5：左眼纹；6：右眼纹；7：眼袋；8：左笑纹；9：右笑纹；10：鼻间纹；11：左瘦纹；12：右瘦纹； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name= "WrinklePouch")
    private String wrinklePouch;

    /**
     * 痤疮色斑：1：痤疮（单）；2：痤疮（少）；3：痤疮（多）；4：雀斑（稀）；5：雀斑（少）；6：雀斑（多）；7：色斑； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name= "AcneStain")
    private String acneStain;

    /**
     * 黑痣胎记：1：痣（小）；2：痣（中）；3：痣（大）；4：黑痣（小）；5：黑痣（中）；6：黑痣（大）；7：胎记； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name= "FreckleBirthmark")
    private String freckleBirthmark;

    /**
     * 疤痕酒窝：1：疤痕；2：酒窝左；3：酒窝右； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name= "ScarDimple")
    private String scarDimple;

    /**
     * 其他特征：1：酒渣鼻（小）；2：酒渣鼻（大）；3：酒渣鼻（重）；4：招风耳左；5：招风耳右；6：大耳
     */
    @JSONField(name= "OtherFeature")
    private String otherFeature;

    // *****************data附加字段*****************

    /**
     *
     */
    @JSONField(name= "HorizontalAngle")
    private String horizontalAngle;

    /**
     *
     */
    @JSONField(name= "VerticalAngle")
    private String verticalAngle;

    /**
     *
     */
    @JSONField(name= "RotatingAngle")
    private String rotatingAngle;

    /**
     * 人脸关联类型
     */
    @JSONField(name= "ConnectObjectType")
    private Integer connectObjectType;

    /**
     * 人脸关联ID
     */
    @JSONField(name= "ConnectObjectId")
    private String connectObjectId;

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
    @JSONField(name= "FaceScore")
    private Float faceScore = 1.0f;

    // *****************kafka附加字段*****************
    /**
     * 人脸图片地址
     */
    @JSONField(name= "FaceUrl")
    private String faceUrl;

}
