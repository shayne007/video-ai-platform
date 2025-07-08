package com.keensense.schedule.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Table;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * 1400标准人脸类
 */
@Data
@Table(name = "face_result")
@ToString(callSuper = true, includeFieldNames = true)
public class FaceResult extends Result implements Serializable {
    
    private static final long serialVersionUID = 324235325L;
    
    /**
     * 人脸标识
     */
    @JSONField(name = "FaceID")
    @Column(name = "faceid")
    private String faceID;
    
    /**
     * 位置标记时间：人工采集时有效
     */
    @JSONField(name = "LocationMarkTime", format = "yyyyMMddHHmmss")
    @Column(name = "locationmarktime")
    private Date locationMarkTime;
    
    /**
     * 人脸出现时间：人工采集时有效
     */
    @JSONField(name = "FaceAppearTime", format = "yyyyMMddHHmmss")
    @Column(name = "faceappeartime")
    private Date faceAppearTime;
    
    /**
     * 人脸消失时间：人工采集时有效
     */
    @JSONField(name = "FaceDisAppearTime", format = "yyyyMMddHHmmss")
    @Column(name = "facedisappeartime")
    private Date faceDisAppearTime;
    
    /**
     * 证件种类
     */
    @JSONField(name = "IdType")
    @Column(name = "idtype")
    private String idType;
    
    /**
     * 证件号码：有效证件号码
     */
    @JSONField(name = "IdNumber")
    @Column(name = "idnumber")
    private String idNumber;
    
    /**
     * 姓名：人员的中文姓名全称
     */
    @JSONField(name = "Name")
    @Column(name = "name")
    private String name;
    
    /**
     * 曾用名：曾经在户籍管理部门正式登记注册、人事档案
     */
    @JSONField(name = "UsedName")
    @Column(name = "usedname")
    private String usedName;
    
    /**
     * 绰号：使用姓名及曾用名之外的名称
     */
    @JSONField(name = "Alias")
    @Column(name = "alias")
    private String alias;
    
    /**
     * 性别代码
     */
    @JSONField(name = "GenderCode")
    @Column(name = "gendercode")
    private String genderCode;
    
    /**
     * 年龄上限：最大可能年龄
     */
    @JSONField(name = "AgeUpLimit")
    @Column(name = "ageuplimit")
    private Integer ageUpLimit;
    
    /**
     * 年龄下限：最小可能年龄
     */
    @JSONField(name = "AgeLowerLimit")
    @Column(name = "agelowerlimit")
    private Integer ageLowerLimit;
    
    /**
     * 民族代码：中国各名族的罗马字母拼写法和代码
     */
    @JSONField(name = "EthicCode")
    @Column(name = "ethiccode")
    private String ethicCode;
    
    /**
     * 国籍代码：世界各国和地区名称代码
     */
    @JSONField(name = "NationalityCode")
    @Column(name = "nationalitycode")
    private String nationalityCode;
    
    /**
     * 籍贯省市县
     */
    @JSONField(name = "NativeCityCode")
    @Column(name = "nativecitycode")
    private String nativeCityCode;
    
    /**
     * 居住地行政区划
     */
    @JSONField(name = "ResidenceAdminDivision")
    @Column(name = "residenceadmindivision")
    private String residenceAdminDivision;
    
    /**
     * 汉语口音代码：汉语口音编码规则
     */
    @JSONField(name = "ChineseAccentCode")
    @Column(name = "chineseaccentcode")
    private String chineseAccentCode;
    
    /**
     * 职业类别代码：职业分类与代码，不包含代码中“—”
     */
    @JSONField(name = "JobCategory")
    @Column(name = "jobcategory")
    private String jobCategory;
    
    /**
     * 同行人数：被标注人的同行人数
     */
    @JSONField(name = "AccompanyNumber")
    @Column(name = "accompanynumber")
    private Integer accompanyNumber;
    
    /**
     * 肤色
     */
    @JSONField(name = "SkinColor")
    @Column(name = "skincolor")
    private String skinColor;
    
    /**
     * 发型
     */
    @JSONField(name = "HairStyle")
    @Column(name = "hairstyle")
    private String hairStyle;
    
    /**
     * 发色
     */
    @JSONField(name = "HairColor")
    @Column(name = "haircolor")
    private String hairColor;
    
    /**
     * 脸型
     */
    @JSONField(name = "FaceStyle")
    @Column(name = "facestyle")
    private String faceStyle;
    
    /**
     * 脸部特征
     */
    @JSONField(name = "FacialFeature")
    @Column(name = "facialfeature")
    private String facialFeature;
    
    /**
     * 体貌特征
     */
    @JSONField(name = "PhysicalFeature")
    @Column(name = "physicalfeature")
    private String physicalFeature;
    
    /**
     * 口罩颜色
     */
    @JSONField(name = "RespiratorColor")
    @Column(name = "respiratorcolor")
    private String respiratorColor;
    
    /**
     * 帽子款式
     */
    @JSONField(name = "CapStyle")
    @Column(name = "capstyle")
    private String capStyle;
    
    /**
     * 帽子颜色
     */
    @JSONField(name = "CapColor")
    @Column(name = "capcolor")
    private String capColor;
    
    /**
     * 眼镜款式
     */
    @JSONField(name = "GlassStyle")
    @Column(name = "glassstyle")
    private String glassStyle;
    
    /**
     * 眼镜颜色
     */
    @JSONField(name = "GlassColor")
    @Column(name = "glasscolor")
    private String glassColor;
    
    /**
     * 是否驾驶员：人工采集时必选 0：否；1：是；2：不确定
     */
    @JSONField(name = "IsDriver")
    @Column(name = "isdriver")
    private Integer isDriver;
    
    /**
     * 是否涉外人员：0：否；1：是；2：不确定
     */
    @JSONField(name = "IsForeigner")
    @Column(name = "isforeigner")
    private Integer isForeigner;
    
    /**
     * 护照证件种类
     */
    @JSONField(name = "PassportType")
    @Column(name = "passporttype")
    private String passportType;
    
    /**
     * 出入境人员类别代码：出入境人员分类代码
     */
    @JSONField(name = "ImmigrantTypeCode")
    @Column(name = "immigranttypecode")
    private String immigrantTypeCode;
    
    /**
     * 是否涉恐人员：0：否；1：是；2：不确定
     */
    @JSONField(name = "IsSuspectedTerrorist")
    @Column(name = "issuspectedterrorist")
    private Integer isSuspectedTerrorist;
    
    /**
     * 涉恐人员编号
     */
    @JSONField(name = "SuspectedTerroristNumber")
    @Column(name = "suspectedterroristnumber")
    private String suspectedTerroristNumber;
    
    /**
     * 是否涉案人员：0：否；1：是；2：不确定
     */
    @JSONField(name = "IsCriminalInvolved")
    @Column(name = "iscriminalinvolved")
    private Integer isCriminalInvolved;
    
    /**
     * 涉案人员专长代码
     */
    @JSONField(name = "CriminalInvolvedSpecilisationCode")
    @Column(name = "criminalinvolvedspecilisationcode")
    private String criminalInvolvedSpecilisationCode;
    
    /**
     * 体表特殊标记
     */
    @JSONField(name = "BodySpeciallMark")
    @Column(name = "bodyspeciallmark")
    private String bodySpeciallMark;
    
    /**
     * 作案手段
     */
    @JSONField(name = "CrimeMethod")
    @Column(name = "crimemethod")
    private String crimeMethod;
    
    /**
     * 作案特点代码
     */
    @JSONField(name = "CrimeCharacterCode")
    @Column(name = "crimecharactercode")
    private String crimeCharacterCode;
    
    /**
     * 在逃人员编号
     */
    @JSONField(name = "EscapedCriminalNumber")
    @Column(name = "escapedcriminalnumber")
    private String escapedCriminalNumber;
    
    /**
     * 是否在押人员：0：否；1：是；2：不确定，人工采集必填
     */
    @JSONField(name = "IsDetainees")
    @Column(name = "isdetainees")
    private Integer isDetainees;
    
    /**
     * 看守所编码
     */
    @JSONField(name = "DetentionHouseCode")
    @Column(name = "detentionhousecode")
    private String detentionHouseCode;
    
    /**
     * 在押人员身份：详细取值见附录 B
     */
    @JSONField(name = "DetaineesIdentity")
    @Column(name = "detaineesidentity")
    private String detaineesIdentity;
    
    /**
     * 在押人员特殊身份
     */
    @JSONField(name = "DetaineesSpecialIdentity")
    @Column(name = "detaineesspecialidentity")
    private String detaineesSpecialIdentity;
    
    /**
     * 成员类型代码
     */
    @JSONField(name = "MemberTypeCode")
    @Column(name = "membertypecode")
    private String memberTypeCode;
    
    /**
     * 是否被害人：人工采集时必选 0：否；1：是；2：不确定
     */
    @JSONField(name = "IsVictim")
    @Column(name = "isvictim")
    private Integer isVictim;
    
    /**
     * 被害人种类
     */
    @JSONField(name = "VictimType")
    @Column(name = "victimtype")
    private String victimType;
    
    /**
     * 受伤害程度
     */
    @JSONField(name = "InjuredDegree")
    @Column(name = "injureddegree")
    private String injuredDegree;
    
    /**
     * 尸体状况代码
     */
    @JSONField(name = "CorpseConditionCode")
    @Column(name = "corpseconditioncode")
    private String corpseConditionCode;
    
    /**
     * 是否可疑人：人工采集时必选 0：否；1：是；2：不确定
     */
    @JSONField(name = "IsSuspiciousPerson")
    @Column(name = "issuspiciousperson")
    private Integer isSuspiciousPerson;
    
    /**
     * 姿态分布：1：平视；2：微仰；3：微俯；4：左微侧脸；5：左斜侧脸；6：左全侧脸；7：右微侧脸；8：右斜侧脸；9：右全侧脸
     */
    @JSONField(name = "Attitude")
    @Column(name = "attitude")
    private Integer attitude;
    
    /**
     * 相似度：人脸相似度，[0，1]
     */
    @JSONField(name = "Similaritydegree")
    @Column(name = "similaritydegree")
    private Double similaritydegree;
    
    /**
     * 眉型：1：上扬眉；2：一字眉；3：八字眉；4：淡眉毛 5：浓眉毛；6：弯眉；7：细眉；8：短眉毛；9：特殊眉； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name = "EyebrowStyle")
    @Column(name = "eyebrowstyle")
    private String eyebrowStyle;
    
    /**
     * 鼻型：1：普通鼻；2：扁鼻子；3：尖鼻子；4：朝天鼻；5：鹰钩鼻；6：蒜头鼻；7：宽鼻子；8：小鼻子；9：露孔鼻；10：特殊鼻； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name = "NoseStyle")
    @Column(name = "nosestyle")
    private String noseStyle;
    
    
    /**
     * 胡型：1：一字胡；2：八字胡；3：淡胡子；4：络胡；5：山羊胡；6：花白胡；7：一点胡
     */
    @JSONField(name = "MustacheStyle")
    @Column(name = "mustachestyle")
    private String mustacheStyle;
    
    /**
     * 嘴唇：1：常见嘴；2：厚嘴唇；3：薄嘴唇；4：厚嘴巴；5：上翘嘴；6：下撇嘴；7：凸嘴；8：凹嘴；9：露齿嘴；10：小嘴； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name = "LipStyle")
    @Column(name = "lipstyle")
    private String lipStyle;
    
    /**
     * 皱纹眼袋：1：抬头纹；2：左眼角纹；3：右眼角纹；4：眉间纹；5：左眼纹；6：右眼纹；7：眼袋；8：左笑纹；9：右笑纹；10：鼻间纹；11：左瘦纹；12：右瘦纹；
     * 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name = "WrinklePouch")
    @Column(name = "wrinklepouch")
    private String wrinklePouch;
    
    /**
     * 痤疮色斑：1：痤疮（单）；2：痤疮（少）；3：痤疮（多）；4：雀斑（稀）；5：雀斑（少）；6：雀斑（多）；7：色斑； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name = "AcneStain")
    @Column(name = "acnestain")
    private String acneStain;
    
    /**
     * 黑痣胎记：1：痣（小）；2：痣（中）；3：痣（大）；4：黑痣（小）；5：黑痣（中）；6：黑痣（大）；7：胎记； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name = "FreckleBirthmark")
    @Column(name = "frecklebirthmark")
    private String freckleBirthmark;
    
    /**
     * 疤痕酒窝：1：疤痕；2：酒窝左；3：酒窝右； 有多个特征时用英文半角分号”;”分隔
     */
    @JSONField(name = "ScarDimple")
    @Column(name = "scardimple")
    private String scarDimple;
    
    /**
     * 其他特征：1：酒渣鼻（小）；2：酒渣鼻（大）；3：酒渣鼻（重）；4：招风耳左；5：招风耳右；6：大耳
     */
    @JSONField(name = "OtherFeature")
    @Column(name = "otherfeature")
    private String otherFeature;
    
    // *****************data附加字段*****************
    
    /**
     *
     */
    @JSONField(name = "HorizontalAngle")
    @Column(name = "horizontalangle")
    private String horizontalAngle;
    
    /**
     *
     */
    @JSONField(name = "VerticalAngle")
    @Column(name = "verticalangle")
    private String verticalAngle;
    
    /**
     *
     */
    @JSONField(name = "RotatingAngle")
    @Column(name = "rotatingangle")
    private String rotatingAngle;
    
    /**
     * 人脸关联类型
     */
    @JSONField(name = "ConnectObjectType")
    @Column(name = "connectobjecttype")
    private Integer connectObjectType;
    
    /**
     * 人脸关联ID
     */
    @JSONField(name = "ConnectObjectId")
    @Column(name = "connectobjectid")
    private String connectObjectId;
    
    // *****************kafka附加字段*****************
    /**
     * 人脸图片地址
     */
    @JSONField(name = "FaceUrl")
    @Column(name = "faceurl")
    private String faceUrl;
    
    // *****************人脸盒子附加字段*****************
    //底库ID
    @JSONField(name = "LibId")
    @Column(name = "libid")
    private String libId;
    
    //底库人员ID
    @JSONField(name = "LibPersonId")
    @Column(name = "libpersonid")
    private String libPersonId;
    
    //底库人员url
    @JSONField(name = "LibPersonUrl")
    @Column(name = "libpersonurl")
    private String libPersonUrl;
    
    @JSONField(name = "LibScore")
    @Column(name = "libscore")
    private Float libScore;
    // *****************人脸盒子附加字段*****************
    // *****************一人一档附加字段*****************
    /**
     *
     */
    @JSONField(name = "Proportion")
    @Column(name = "proportion")
    private String proportion;
    
    /**
     * 档案ID
     */
    @JSONField(name = "ArchivesID")
    @Column(name = "archivesid")
    private String archivesID;
    
    /**
     * 以什么绑定档案，人脸or人形
     */
    @JSONField(name = "TrailSource")
    @Column(name = "trailsource")
    private String trailSource;
    
    /**
     * 人脸分数
     */
    @JSONField(name = "FaceScore")
    @Column(name = "facescore")
    private Float faceScore = 1.0f;
    
    /**
     * 第三方库的特征id
     */
    @JSONField(name = "FaceFeatureId")
    @Column(name = "facefeatureid")
    private String faceFeatureId;
    
    /**
     * pitch是围绕X轴旋转，也叫做俯仰角
     */
    @JSONField(name = "Pitch")
    @Column(name = "pitch")
    private Float pitch;
    /**
     * yaw是围绕Y轴旋转,也叫偏航角
     */
    @JSONField(name = "Yaw")
    @Column(name = "yaw")
    private Float yaw;
    /**
     * roll也叫偏航角roll是围绕Z轴旋转，也叫翻滚角
     */
    @JSONField(name = "Roll")
    @Column(name = "roll")
    private Float roll;
    
    /**
     * 模糊度
     */
    @JSONField(name = "Blurry")
    @Column(name = "blurry")
    private Float blurry;
    
    // *****************一人一档附加字段*****************
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FaceResult{");
        sb.append("faceID='").append(faceID).append('\'');
        sb.append(", locationMarkTime=").append(locationMarkTime);
        sb.append(", faceAppearTime=").append(faceAppearTime);
        sb.append(", faceDisAppearTime=").append(faceDisAppearTime);
        sb.append(", idType='").append(idType).append('\'');
        sb.append(", idNumber='").append(idNumber).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", usedName='").append(usedName).append('\'');
        sb.append(", alias='").append(alias).append('\'');
        sb.append(", genderCode='").append(genderCode).append('\'');
        sb.append(", ageUpLimit=").append(ageUpLimit);
        sb.append(", ageLowerLimit=").append(ageLowerLimit);
        sb.append(", ethicCode='").append(ethicCode).append('\'');
        sb.append(", nationalityCode='").append(nationalityCode).append('\'');
        sb.append(", nativeCityCode='").append(nativeCityCode).append('\'');
        sb.append(", residenceAdminDivision='").append(residenceAdminDivision).append('\'');
        sb.append(", chineseAccentCode='").append(chineseAccentCode).append('\'');
        sb.append(", jobCategory='").append(jobCategory).append('\'');
        sb.append(", accompanyNumber=").append(accompanyNumber);
        sb.append(", skinColor='").append(skinColor).append('\'');
        sb.append(", hairStyle='").append(hairStyle).append('\'');
        sb.append(", hairColor='").append(hairColor).append('\'');
        sb.append(", faceStyle='").append(faceStyle).append('\'');
        sb.append(", facialFeature='").append(facialFeature).append('\'');
        sb.append(", physicalFeature='").append(physicalFeature).append('\'');
        sb.append(", respiratorColor='").append(respiratorColor).append('\'');
        sb.append(", capStyle='").append(capStyle).append('\'');
        sb.append(", capColor='").append(capColor).append('\'');
        sb.append(", glassStyle='").append(glassStyle).append('\'');
        sb.append(", glassColor='").append(glassColor).append('\'');
        sb.append(", isDriver=").append(isDriver);
        sb.append(", isForeigner=").append(isForeigner);
        sb.append(", passportType='").append(passportType).append('\'');
        sb.append(", immigrantTypeCode='").append(immigrantTypeCode).append('\'');
        sb.append(", isSuspectedTerrorist=").append(isSuspectedTerrorist);
        sb.append(", suspectedTerroristNumber='").append(suspectedTerroristNumber).append('\'');
        sb.append(", isCriminalInvolved=").append(isCriminalInvolved);
        sb.append(", criminalInvolvedSpecilisationCode='").append(criminalInvolvedSpecilisationCode)
            .append('\'');
        sb.append(", bodySpeciallMark='").append(bodySpeciallMark).append('\'');
        sb.append(", crimeMethod='").append(crimeMethod).append('\'');
        sb.append(", crimeCharacterCode='").append(crimeCharacterCode).append('\'');
        sb.append(", escapedCriminalNumber='").append(escapedCriminalNumber).append('\'');
        sb.append(", isDetainees=").append(isDetainees);
        sb.append(", detentionHouseCode='").append(detentionHouseCode).append('\'');
        sb.append(", detaineesIdentity='").append(detaineesIdentity).append('\'');
        sb.append(", detaineesSpecialIdentity='").append(detaineesSpecialIdentity).append('\'');
        sb.append(", memberTypeCode='").append(memberTypeCode).append('\'');
        sb.append(", isVictim=").append(isVictim);
        sb.append(", victimType='").append(victimType).append('\'');
        sb.append(", injuredDegree='").append(injuredDegree).append('\'');
        sb.append(", corpseConditionCode='").append(corpseConditionCode).append('\'');
        sb.append(", isSuspiciousPerson=").append(isSuspiciousPerson);
        sb.append(", attitude=").append(attitude);
        sb.append(", similaritydegree=").append(similaritydegree);
        sb.append(", eyebrowStyle='").append(eyebrowStyle).append('\'');
        sb.append(", noseStyle='").append(noseStyle).append('\'');
        sb.append(", mustacheStyle='").append(mustacheStyle).append('\'');
        sb.append(", lipStyle='").append(lipStyle).append('\'');
        sb.append(", wrinklePouch='").append(wrinklePouch).append('\'');
        sb.append(", acneStain='").append(acneStain).append('\'');
        sb.append(", freckleBirthmark='").append(freckleBirthmark).append('\'');
        sb.append(", scarDimple='").append(scarDimple).append('\'');
        sb.append(", otherFeature='").append(otherFeature).append('\'');
        sb.append(", horizontalAngle='").append(horizontalAngle).append('\'');
        sb.append(", verticalAngle='").append(verticalAngle).append('\'');
        sb.append(", rotatingAngle='").append(rotatingAngle).append('\'');
        sb.append(", connectObjectType=").append(connectObjectType);
        sb.append(", connectObjectId='").append(connectObjectId).append('\'');
        sb.append(", proportion='").append(proportion).append('\'');
        sb.append(", archivesID='").append(archivesID).append('\'');
        sb.append(", trailSource='").append(trailSource).append('\'');
        sb.append(", faceScore='").append(faceScore).append('\'');
        sb.append(", faceUrl='").append(faceUrl).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", serialnumber='").append(serialnumber).append('\'');
        sb.append(", appearTime=").append(appearTime);
        sb.append(", disappearTime=").append(disappearTime);
        sb.append(", infoKind=").append(infoKind);
        sb.append(", sourceId='").append(sourceId).append('\'');
        sb.append(", deviceId='").append(deviceId).append('\'');
        sb.append(", leftTopX=").append(leftTopX);
        sb.append(", leftTopY=").append(leftTopY);
        sb.append(", rightBtmX=").append(rightBtmX);
        sb.append(", rightBtmY=").append(rightBtmY);
        sb.append(", firstObj=").append(firstObj);
        sb.append(", slaveIp='").append(slaveIp).append('\'');
        sb.append(", objId=").append(objId);
        sb.append(", startFrameidx=").append(startFrameidx);
        sb.append(", endFrameidx=").append(endFrameidx);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", frameIdx=").append(frameIdx);
        sb.append(", framePts=").append(framePts);
        sb.append(", featureObject='").append(featureObject).append('\'');
        sb.append(", subImageList='").append(subImageList).append('\'');
        sb.append(", imgUrl='").append(imgUrl).append('\'');
        sb.append(", bigImgUrl='").append(bigImgUrl).append('\'');
        sb.append(", objType=").append(objType);
        sb.append(", createTime=").append(createTime);
        sb.append('}');
        return sb.toString();
    }
}
