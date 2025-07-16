package com.keensense.search.constant;

import com.keensense.search.domain.FaceResult;
import com.keensense.search.domain.NonMotorVehiclesResult;
import com.keensense.search.domain.PersonResult;
import com.keensense.search.domain.VlprResult;
import com.keensense.search.tool_interface.SingleParameterCheck;
import com.keensense.search.utils.ParametercheckUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanx xiaohui on 2019-11-22.
 */
public class ParameterChcekConst {

    public static final Map<String, SingleParameterCheck> faceMap = new HashMap<>();
    public static final Map<String, SingleParameterCheck> personMap = new HashMap<>();
    public static final Map<String, SingleParameterCheck> motorvehicleMap = new HashMap<>();
    public static final Map<String, SingleParameterCheck> nonMotorvehicleMap = new HashMap<>();

    static {
        faceMap.put("faceID".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("faceID", parameter, 0, 48);
            ParametercheckUtil.checkSpecialChar("faceID", parameter);
        });

        faceMap.put("InfoKind".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("InfoKind", parameter);
        });
        faceMap.put("SourceID".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("SourceID", parameter, 0, 41);
            ParametercheckUtil.checkSpecialChar("SourceID", parameter);
        });
        faceMap.put("DeviceID".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("DeviceID", parameter, 0, 20);
            ParametercheckUtil.checkSpecialChar("DeviceID", parameter);
        });
        faceMap.put("LeftTopX".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("LeftTopX", parameter);
        });
        faceMap.put("LeftTopY".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("LeftTopY", parameter);
        });
        faceMap.put("RightBtmX".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("RightBtmX", parameter);
        });
        faceMap.put("RightBtmY".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("RightBtmY", parameter);
        });
        faceMap.put("LocationMarkTime".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDate("LocationMarkTime", parameter);
        });
        faceMap.put("FaceAppearTime".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDate("FaceAppearTime", parameter);
        });
        faceMap.put("FaceDisAppearTime".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDate("FaceDisAppearTime", parameter);
        });
        faceMap.put("IDType".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("IDType", parameter, 0, 3);
            ParametercheckUtil.checkSpecialChar("IDType", parameter);
        });
        faceMap.put("IDNumber".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("IDNumber", parameter, 0, 30);
            ParametercheckUtil.checkSpecialChar("IDNumber", parameter);
        });
        faceMap.put("Name".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("Name", parameter, 0, 50);
            ParametercheckUtil.checkSpecialChar("Name", parameter);
        });
        faceMap.put("UsedName".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("UsedName", parameter, 0, 50);
            ParametercheckUtil.checkSpecialChar("UsedName", parameter);
        });
        faceMap.put("Alias".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("Alias", parameter, 0, 50);
            ParametercheckUtil.checkSpecialChar("Alias", parameter);
        });
        faceMap.put("GenderCode".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("GenderCode", parameter, 0, 1);
            ParametercheckUtil.checkSpecialChar("GenderCode", parameter);
        });
        faceMap.put("AgeUpLimit".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("AgeUpLimit", parameter);
        });
        faceMap.put("AgeLowerLimit".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("AgeLowerLimit", parameter);
        });
        faceMap.put("EthicCode".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("EthicCode", parameter, 0, 2);
            ParametercheckUtil.checkSpecialChar("EthicCode", parameter);
        });
        faceMap.put("NationalityCode".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("NationalityCode", parameter, 0, 3);
            ParametercheckUtil.checkSpecialChar("NationalityCode", parameter);
        });
        faceMap.put("NativeCityCode".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("NativeCityCode", parameter, 0, 6);
            ParametercheckUtil.checkSpecialChar("NativeCityCode", parameter);
        });
        faceMap.put("ResidenceAdminDivision".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("ResidenceAdminDivision", parameter, 0, 6);
            ParametercheckUtil.checkSpecialChar("ResidenceAdminDivision", parameter);
        });
        faceMap.put("ChineseAccentCode".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("ChineseAccentCode", parameter, 0, 6);
            ParametercheckUtil.checkSpecialChar("ChineseAccentCode", parameter);
        });
        faceMap.put("JobCategory".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("JobCategory", parameter, 0, 3);
            ParametercheckUtil.checkSpecialChar("JobCategory", parameter);
        });
        faceMap.put("AccompanyNumber".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("AccompanyNumber", parameter);
        });

        faceMap.put("SkinColor".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("SkinColor", parameter, 0, 20);
            ParametercheckUtil.checkSpecialChar("SkinColor", parameter);
        });
        faceMap.put("HairStyle".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("HairStyle", parameter, 0, 2);
            ParametercheckUtil.checkSpecialChar("HairStyle", parameter);
        });
        faceMap.put("HairColor".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("HairColor", parameter, 0, 5);
            ParametercheckUtil.checkSpecialChar("HairColor", parameter);
        });
        faceMap.put("FaceStyle".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("FaceStyle", parameter, 0, 4);
            ParametercheckUtil.checkSpecialChar("FaceStyle", parameter);
        });
        faceMap.put("FacialFeature".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("FacialFeature", parameter, 0, 40);
            ParametercheckUtil.checkSpecialChar("FacialFeature", parameter);
        });
        faceMap.put("PhysicalFeature".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("PhysicalFeature", parameter, 0, 200);
            ParametercheckUtil.checkSpecialChar("PhysicalFeature", parameter);
        });
        faceMap.put("RespiratorColor".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("RespiratorColor", parameter, 0, 5);
            ParametercheckUtil.checkSpecialChar("RespiratorColor", parameter);
        });
        faceMap.put("CapStyle".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("CapStyle", parameter, 0, 2);
            ParametercheckUtil.checkSpecialChar("CapStyle", parameter);
        });
        faceMap.put("CapColor".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("CapColor", parameter, 0, 5);
            ParametercheckUtil.checkSpecialChar("CapColor", parameter);
        });
        faceMap.put("GlassStyle".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("GlassStyle", parameter, 0, 2);
            ParametercheckUtil.checkSpecialChar("GlassStyle", parameter);
        });
        faceMap.put("GlassColor".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("GlassColor", parameter, 0, 5);
            ParametercheckUtil.checkSpecialChar("GlassColor", parameter);
        });
        faceMap.put("IsDriver".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("IsDriver", parameter);
        });
        faceMap.put("IsForeigner".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("IsForeigner", parameter);
        });
        faceMap.put("PassportType".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("PassportType", parameter, 0, 2);
            ParametercheckUtil.checkSpecialChar("PassportType", parameter);
        });
        faceMap.put("ImmigrantTypeCode".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("ImmigrantTypeCode", parameter, 0, 2);
            ParametercheckUtil.checkSpecialChar("ImmigrantTypeCode", parameter);
        });
        faceMap.put("IsSuspectedTerrorist".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("IsSuspectedTerrorist", parameter);
        });
        faceMap.put("SuspectedTerroristNumber".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("SuspectedTerroristNumber", parameter, 0, 10);
            ParametercheckUtil.checkSpecialChar("SuspectedTerroristNumber", parameter);
        });
        faceMap.put("IsCriminalInvolved".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("IsCriminalInvolved", parameter);
        });
        faceMap.put("CriminalInvolvedSpecilisationCode".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("CriminalInvolvedSpecilisationCode", parameter, 0, 10);
            ParametercheckUtil.checkSpecialChar("CriminalInvolvedSpecilisationCode", parameter);
        });
        faceMap.put("BodySpeciallMark".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("BodySpeciallMark", parameter, 0, 7);
            ParametercheckUtil.checkSpecialChar("BodySpeciallMark", parameter);
        });
        faceMap.put("CrimeMethod".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("CrimeMethod", parameter, 0, 5);
            ParametercheckUtil.checkSpecialChar("CrimeMethod", parameter);
        });
        faceMap.put("CrimeCharacterCode".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("CrimeCharacterCode", parameter, 0, 3);
            ParametercheckUtil.checkSpecialChar("CrimeCharacterCode", parameter);
        });
        faceMap.put("EscapedCriminalNumber".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("EscapedCriminalNumber", parameter, 0, 100);
            ParametercheckUtil.checkSpecialChar("EscapedCriminalNumber", parameter);
        });
        faceMap.put("IsDetainees".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("IsDetainees", parameter);
        });

        faceMap.put("DetentionHouseCode".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("DetentionHouseCode", parameter, 0, 9);
            ParametercheckUtil.checkSpecialChar("DetentionHouseCode", parameter);
        });
        faceMap.put("DetaineesIdentity".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("DetaineesIdentity", parameter, 0, 2);
            ParametercheckUtil.checkSpecialChar("DetaineesIdentity", parameter);
        });
        faceMap.put("DetaineesSpecialIdentity".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("DetaineesSpecialIdentity", parameter, 0, 2);
            ParametercheckUtil.checkSpecialChar("DetaineesSpecialIdentity", parameter);
        });
        faceMap.put("MemberTypeCode".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("MemberTypeCode", parameter, 0, 2);
            ParametercheckUtil.checkSpecialChar("MemberTypeCode", parameter);
        });
        faceMap.put("IsVictim".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("IsVictim", parameter);
        });

        faceMap.put("VictimType".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("VictimType", parameter, 0, 3);
            ParametercheckUtil.checkSpecialChar("VictimType", parameter);
        });
        faceMap.put("InjuredDegree".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("InjuredDegree", parameter, 0, 1);
            ParametercheckUtil.checkSpecialChar("InjuredDegree", parameter);
        });
        faceMap.put("CorpseConditionCode".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("CorpseConditionCode", parameter, 0, 2);
            ParametercheckUtil.checkSpecialChar("CorpseConditionCode", parameter);
        });
        faceMap.put("IsSuspiciousPerson".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("IsSuspiciousPerson", parameter);
        });
        faceMap.put("Attitude".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("Attitude", parameter);
        });
        faceMap.put("Similaritydegree".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDouble("Similaritydegree", parameter);
        });

        faceMap.put("EyebrowStyle".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("EyebrowStyle", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("EyebrowStyle", parameter);
        });
        faceMap.put("NoseStyle".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("NoseStyle", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("NoseStyle", parameter);
        });
        faceMap.put("MustacheStyle".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("MustacheStyle", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("MustacheStyle", parameter);
        });
        faceMap.put("LipStyle".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("LipStyle", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("LipStyle", parameter);
        });
        faceMap.put("WrinklePouch".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("WrinklePouch", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("WrinklePouch", parameter);
        });
        faceMap.put("AcneStain".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("AcneStain", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("AcneStain", parameter);
        });
        faceMap.put("FreckleBirthmark".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("FreckleBirthmark ", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("FreckleBirthmark", parameter);
        });
        faceMap.put("ScarDimple".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("ScarDimple ", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("ScarDimple", parameter);
        });
        faceMap.put("OtherFeature".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("OtherFeature ", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("OtherFeature", parameter);
        });
        faceMap.put("SubImageList".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("SubImageList ", parameter, 0, 2048);
        });
        faceMap.put("FeatureObject".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("FeatureObject ", parameter, 0, 4096);
        });
        faceMap.put("FrameIdx".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLong("FrameIdx ", parameter);
        });
        faceMap.put("FramePts".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLong("FramePts ", parameter);
        });
        faceMap.put("HorizontalAngle".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("HorizontalAngle ", parameter, 0, 20);
            ParametercheckUtil.checkSpecialChar("HorizontalAngle", parameter);
        });
        faceMap.put("VerticalAngle".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("VerticalAngle ", parameter, 0, 20);
            ParametercheckUtil.checkSpecialChar("VerticalAngle", parameter);
        });
        faceMap.put("RotatingAngle".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("RotatingAngle ", parameter, 0, 20);
            ParametercheckUtil.checkSpecialChar("RotatingAngle", parameter);
        });
        faceMap.put("StartFrameIdx".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLong("StartFrameIdx", parameter);
        });
        faceMap.put("EndFrameIdx".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLong("EndFrameIdx", parameter);
        });
        faceMap.put("InsertTime".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDate("InsertTime", parameter);
        });
        faceMap.put("ObjId".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("ObjId", parameter);
        });
        faceMap.put("SlaveIp".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("SlaveIp", parameter, 0, 16);
            ParametercheckUtil.checkSpecialChar("SlaveIp", parameter);
        });
        faceMap.put("ConnectObjectType".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("ConnectObjectType", parameter);
        });
        faceMap.put("ConnectObjectId".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("ConnectObjectId ", parameter, 0, 48);
            ParametercheckUtil.checkSpecialChar("ConnectObjectId", parameter);
        });
        faceMap.put("FirstObj".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("FirstObj", parameter);
        });
        faceMap.put("CreateTime".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDate("CreateTime", parameter);
        });

        faceMap.put("Proportion".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("Proportion", parameter, 0, 10);
            ParametercheckUtil.checkSpecialChar("Proportion", parameter);
        });
        faceMap.put("ArchivesId".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("ArchivesId", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("ArchivesId", parameter);
        });
        faceMap.put("TrailSource".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("TrailSource", parameter, 0, 5);
            ParametercheckUtil.checkSpecialChar("TrailSource", parameter);
        });
        faceMap.put("FaceScore".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkFloat("FaceScore", parameter);
        });
        faceMap.put("Serialnumber".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("Serialnumber", parameter, 0, 64);
            ParametercheckUtil.checkSpecialChar("Serialnumber", parameter);
        });
        faceMap.put("AnalysisId".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("AnalysisId", parameter, 0, 64);
            ParametercheckUtil.checkSpecialChar("AnalysisId", parameter);
        });
        faceMap.put("FaceQuality".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkFloat("FaceQuality", parameter);
        });

        faceMap.put("LibId".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("LibId", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("LibId", parameter);
        });
        faceMap.put("LibPersonId".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("LibPersonId", parameter, 0, 32);
            ParametercheckUtil.checkSpecialChar("LibPersonId", parameter);
        });
        faceMap.put("LibPersonUrl".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("LibPersonUrl", parameter, 0, 256);
            ParametercheckUtil.checkSpecialChar("LibPersonUrl", parameter);
        });
        faceMap.put("LibScore".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkFloat("LibScore", parameter);
        });
        faceMap.put("ObjType".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("ObjType", parameter);
        });

        faceMap.put("AssistSnapshot".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("AssistSnapshot", parameter, 0, 1000);
//            ParametercheckUtil.checkSpecialChar("AssistSnapshot", parameter);
        });
        faceMap.put("Id".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("Id", parameter, 0, 48);
            ParametercheckUtil.checkSpecialChar("Id", parameter);
        });
        faceMap.put("Imgurl".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("Imgurl", parameter, 0, 256);
            ParametercheckUtil.checkSpecialChar("Imgurl", parameter);
        });
        faceMap.put("BigImgUrl".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLength("BigImgUrl", parameter, 0, 256);
            ParametercheckUtil.checkSpecialChar("BigImgUrl", parameter);
        });
        faceMap.put("MarkTime".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDate("MarkTime", parameter);
        });
        faceMap.put("AppearTime".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDate("AppearTime", parameter);
        });
        faceMap.put("DisappearTime".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDate("DisappearTime", parameter);
        });
        faceMap.put("StartFramePts".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkLong("StartFramePts", parameter);
        });
    }

    static {
        personMap.put("PersonID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PersonID", parameter, 0, 48);
                ParametercheckUtil.checkSpecialChar("PersonID", parameter);
            });
        personMap.put("InfoKind".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("InfoKind", parameter);
            });
        personMap.put("SourceID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SourceID", parameter, 0, 41);
                ParametercheckUtil.checkSpecialChar("SourceID", parameter);
            });
        personMap.put("DeviceID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("DeviceID", parameter, 0, 20);
                ParametercheckUtil.checkSpecialChar("DeviceID", parameter);
            });
        personMap.put("LeftTopX".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("LeftTopX", parameter);
        });
        personMap.put("LeftTopY".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("LeftTopY", parameter);
        });
        personMap.put("RightBtmX".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("RightBtmX", parameter);
        });
        personMap.put("RightBtmY".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("RightBtmY", parameter);
        });
        personMap.put("LocationMarkTime".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDate("LocationMarkTime", parameter);
        });
        personMap.put("PersonAppearTime".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDate("PersonAppearTime", parameter);
        });
        personMap.put("PersonDisAppearTime".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkDate("PersonDisAppearTime", parameter);
        });
        personMap.put("IDType".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("IDType", parameter, 0, 3);
                ParametercheckUtil.checkSpecialChar("IDType", parameter);
            });
        personMap.put("IDNumber".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("IDNumber", parameter, 0, 30);
                ParametercheckUtil.checkSpecialChar("IDNumber", parameter);
            });
        personMap.put("Name".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Name", parameter, 0, 50);
                ParametercheckUtil.checkSpecialChar("Name", parameter);
            });
        personMap.put("UsedName".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("UsedName", parameter, 0, 50);
                ParametercheckUtil.checkSpecialChar("UsedName", parameter);
            });
        personMap.put("Alias".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Alias", parameter, 0, 50);
                ParametercheckUtil.checkSpecialChar("Alias", parameter);
            });
        personMap.put("GenderCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("GenderCode", parameter, 0, 1);
                ParametercheckUtil.checkSpecialChar("GenderCode", parameter);
            });
        personMap.put("AgeUpLimit".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("AgeUpLimit", parameter);
            });
        personMap.put("AgeLowerLimit".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("AgeLowerLimit", parameter);
            });
        personMap.put("EthicCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("EthicCode", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("EthicCode", parameter);
            });
        personMap.put("NationalityCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("NationalityCode", parameter, 0, 3);
                ParametercheckUtil.checkSpecialChar("NationalityCode", parameter);
            });
        personMap.put("NativeCityCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("NativeCityCode", parameter, 0, 6);
                ParametercheckUtil.checkSpecialChar("NativeCityCode", parameter);
            });
        personMap.put("ResidenceAdminDivision".toLowerCase(), (parameter) -> {
            ParametercheckUtil
                .checkLength("ResidenceAdminDivision", parameter, 0, 6);
            ParametercheckUtil.checkSpecialChar("ResidenceAdminDivision", parameter);
        });
        personMap.put("ChineseAccentCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("ChineseAccentCode", parameter, 0, 6);
                ParametercheckUtil.checkSpecialChar("ChineseAccentCode", parameter);
            });
        personMap.put("PersonOrg".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PersonOrg", parameter, 0, 100);
                ParametercheckUtil.checkSpecialChar("PersonOrg", parameter);
            });
        personMap.put("JobCategory".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("JobCategory", parameter, 0, 3);
                ParametercheckUtil.checkSpecialChar("JobCategory", parameter);
            });
        personMap.put("AccompanyNumber".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("AccompanyNumber", parameter);
            });
        personMap.put("HeightUpLimit".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HeightUpLimit", parameter);
            });
        personMap.put("HeightLowerLimi".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HeightLowerLimi", parameter);
            });
        personMap.put("BodyType".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("BodyType", parameter, 0, 10);
                ParametercheckUtil.checkSpecialChar("BodyType", parameter);
            });
        personMap.put("SkinColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SkinColor", parameter, 0, 10);
                ParametercheckUtil.checkSpecialChar("SkinColor", parameter);
            });
        personMap.put("HairStyle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("HairStyle", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("HairStyle", parameter);
            });
        personMap.put("HairColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("HairColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("HairColor", parameter);
            });
        personMap.put("Gesture".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Gesture", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("Gesture", parameter);
            });
        personMap.put("Status".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Status", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("Status", parameter);
            });
        personMap.put("FaceStyle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("FaceStyle", parameter, 0, 4);
                ParametercheckUtil.checkSpecialChar("FaceStyle", parameter);
            });
        personMap.put("FacialFeature".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("FacialFeature", parameter, 0, 40);
                ParametercheckUtil.checkSpecialChar("FacialFeature", parameter);
            });
        personMap.put("PhysicalFeature".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PhysicalFeature", parameter, 0, 200);
                ParametercheckUtil.checkSpecialChar("PhysicalFeature", parameter);
            });
        personMap.put("BodyFeature".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("BodyFeature", parameter, 0, 70);
                ParametercheckUtil.checkSpecialChar("BodyFeature", parameter);
            });
        personMap.put("HabitualMovement".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("HabitualMovement", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("HabitualMovement", parameter);
            });
        personMap.put("Behavior".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Behavior", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("Behavior", parameter);
            });
        personMap.put("BehaviorDescription".toLowerCase(), (parameter) -> {
            ParametercheckUtil
                .checkLength("BehaviorDescription", parameter, 0, 256);
            ParametercheckUtil.checkSpecialChar("BehaviorDescription", parameter);
        });
        personMap.put("Appendant".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Appendant", parameter, 0, 128);
                ParametercheckUtil.checkSpecialChar("Appendant", parameter);
            });
        personMap.put("AppendantDescription".toLowerCase(), (parameter) -> {
            ParametercheckUtil
                .checkLength("AppendantDescription", parameter, 0, 256);
            ParametercheckUtil.checkSpecialChar("AppendantDescription", parameter);
        });
        personMap.put("UmbrellaColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("UmbrellaColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("UmbrellaColor", parameter);
            });
        personMap.put("RespiratorColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("RespiratorColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("RespiratorColor", parameter);
            });
        personMap.put("CapStyle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CapStyle", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("CapStyle", parameter);
            });
        personMap.put("CapColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CapColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("CapColor", parameter);
            });
        personMap.put("GlassStyle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("GlassStyle", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("GlassStyle", parameter);
            });
        personMap.put("GlassColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("GlassColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("GlassColor", parameter);
            });
        personMap.put("ScarfColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("ScarfColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("ScarfColor", parameter);
            });
        personMap.put("BagStyle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("BagStyle", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("BagStyle", parameter);
            });
        personMap.put("BagColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("BagColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("BagColor", parameter);
            });
        personMap.put("CoatStyle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CoatStyle", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("CoatStyle", parameter);
            });
        personMap.put("CoatLength".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CoatLength", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("CoatLength", parameter);
            });
        personMap.put("CoatColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CoatColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("CoatColor", parameter);
            });
        personMap.put("TrousersStyle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("TrousersStyle", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("TrousersStyle", parameter);
            });
        personMap.put("TrousersColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("TrousersColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("TrousersColor", parameter);
            });
        personMap.put("TrousersLen".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("TrousersLen", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("TrousersLen", parameter);
            });
        personMap.put("ShoesStyle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("ShoesStyle", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("ShoesStyle", parameter);
            });
        personMap.put("ShoesColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("ShoesColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("ShoesColor", parameter);
            });
        personMap.put("IsDriver".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsDriver", parameter);
            });
        personMap.put("IsForeigner".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsForeigner", parameter);
            });
        personMap.put("PassportType".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PassportType", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("PassportType", parameter);
            });
        personMap.put("ImmigrantTypeCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("ImmigrantTypeCode", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("ImmigrantTypeCode", parameter);
            });
        personMap.put("IsSuspectedTerrorist".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsSuspectedTerrorist", parameter);
            });
        personMap.put("SuspectedTerroristNumber".toLowerCase(), (parameter) -> {
            ParametercheckUtil
                .checkLength("SuspectedTerroristNumber", parameter, 0, 19);
            ParametercheckUtil.checkSpecialChar("SuspectedTerroristNumber", parameter);
        });
        personMap.put("IsCriminalInvolved".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsCriminalInvolved", parameter);
            });
        personMap.put("CriminalInvolvedSpecilisationCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil
                    .checkLength("CriminalInvolvedSpecilisationCode", parameter, 0, 20);
                ParametercheckUtil.checkSpecialChar("CriminalInvolvedSpecilisationCode", parameter);
            });
        personMap.put("BodySpeciallMark".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("BodySpeciallMark", parameter, 0, 7);
                ParametercheckUtil.checkSpecialChar("BodySpeciallMark", parameter);
            });
        personMap.put("CrimeMethod".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CrimeMethod", parameter, 0, 4);
                ParametercheckUtil.checkSpecialChar("CrimeMethod", parameter);
            });
        personMap.put("CrimeCharacterCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CrimeCharacterCode", parameter, 0, 3);
                ParametercheckUtil.checkSpecialChar("CrimeCharacterCode", parameter);
            });
        personMap.put("EscapedCriminalNumber".toLowerCase(), (parameter) -> {
            ParametercheckUtil
                .checkLength("EscapedCriminalNumber", parameter, 0, 100);
            ParametercheckUtil.checkSpecialChar("EscapedCriminalNumber", parameter);
        });
        personMap.put("IsDetainees".toLowerCase(), (parameter) -> {
            ParametercheckUtil.checkInteger("IsDetainees", parameter);
        });

        personMap.put("DetentionHouseCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("DetentionHouseCode", parameter, 0, 9);
                ParametercheckUtil.checkSpecialChar("DetentionHouseCode", parameter);
            });
        personMap.put("DetaineesIdentity".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("DetaineesIdentity", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("DetaineesIdentity", parameter);
            });
        personMap.put("DetaineesSpecialIdentity".toLowerCase(), (parameter) -> {
            ParametercheckUtil
                .checkLength("DetaineesSpecialIdentity", parameter, 0, 2);
            ParametercheckUtil.checkSpecialChar("DetaineesSpecialIdentity", parameter);
        });
        personMap.put("MemberTypeCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("MemberTypeCode", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("MemberTypeCode", parameter);
            });
        personMap.put("IsVictim".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsVictim", parameter);
            });
        personMap.put("VictimType".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VictimType", parameter, 0, 3);
                ParametercheckUtil.checkSpecialChar("VictimType", parameter);
            });
        personMap.put("InjuredDegree".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("InjuredDegree", parameter, 0, 1);
                ParametercheckUtil.checkSpecialChar("InjuredDegree", parameter);
            });
        personMap.put("CorpseConditionCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CorpseConditionCode", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("CorpseConditionCode", parameter);
            });
        personMap.put("IsSuspiciousPerson".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsSuspiciousPerson", parameter);
            });
        personMap.put("SubImageList".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SubImageList", parameter, 0, 2048);
            });
        personMap.put("FeatureObject".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("FeatureObject", parameter, 0, 4096);
            });

        personMap.put("StartFrameIdx".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("StartFrameIdx", parameter);
            });
        personMap.put("EndFrameIdx".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("EndFrameIdx", parameter);
            });
        personMap.put("InsertTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("InsertTime", parameter);
            });
        personMap.put("ObjId".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("ObjId", parameter);
            });
        personMap.put("SlaveIp".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SlaveIp", parameter, 0, 16);
                ParametercheckUtil.checkSpecialChar("SlaveIp", parameter);
            });
        personMap.put("FrameIdx".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("FrameIdx", parameter);
            });
        personMap.put("FramePts".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("FramePts", parameter);
            });
        personMap.put("UpcolorTag1".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("UpcolorTag1", parameter);
            });
        personMap.put("UpcolorTag2".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("UpcolorTag2", parameter);
            });
        personMap.put("LowcolorTag1".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("LowcolorTag1", parameter);
            });
        personMap.put("LowcolorTag2".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("LowcolorTag2", parameter);
            });
        personMap.put("MaincolorTag1".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("MaincolorTag1", parameter);
            });
        personMap.put("MaincolorTag2".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("MaincolorTag2", parameter);
            });
        personMap.put("MaincolorTag3".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("MaincolorTag3", parameter);
            });
        personMap.put("Angle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Angle", parameter);
            });
        personMap.put("Serialnumber".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Serialnumber", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("Serialnumber", parameter);
            });
        personMap.put("AnalysisId".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("AnalysisId", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("AnalysisId", parameter);
            });
        personMap.put("Handbag".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Handbag", parameter);
            });
        personMap.put("Cap".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Cap", parameter);
            });
        personMap.put("CoatTexture".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("CoatTexture", parameter);
            });
        personMap.put("TrousersTexture".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("TrousersTexture", parameter);
            });
        personMap.put("Trolley".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Trolley", parameter);
            });
        personMap.put("Luggage".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Luggage", parameter);
            });

        personMap.put("FaceUUID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("FaceUUID", parameter, 0, 48);
                ParametercheckUtil.checkSpecialChar("FaceUUID", parameter);
            });
        personMap.put("FirstObj".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("FirstObj", parameter);
            });
        personMap.put("CreateTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("CreateTime", parameter);
            });
        personMap.put("BodyQuality".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkFloat("BodyQuality", parameter);
            });
        personMap.put("Bag".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Bag", parameter);
            });
        personMap.put("Glasses".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Glasses", parameter);
            });
        personMap.put("Umbrella".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Umbrella", parameter);
            });
        personMap.put("Respirator".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Respirator", parameter);
            });
        personMap.put("ObjType".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("ObjType", parameter);
            });
        personMap.put("AssistSnapshot".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("AssistSnapshot", parameter, 0, 1000);
//                ParametercheckUtil.checkSpecialChar("AssistSnapshot", parameter);
            });
        personMap.put("Id".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Id", parameter, 0, 48);
                ParametercheckUtil.checkSpecialChar("Id", parameter);
            });
        personMap.put("Imgurl".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Imgurl", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("Imgurl", parameter);
            });
        personMap.put("BigImgUrl".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("BigImgUrl", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("BigImgUrl", parameter);
            });
        personMap.put("MarkTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("MarkTime", parameter);
            });
        personMap.put("AppearTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("AppearTime", parameter);
            });
        personMap.put("DisappearTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("DisappearTime", parameter);
            });
        personMap.put("StartFramePts".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("StartFramePts", parameter);
            });
        personMap.put("HasKnife".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HasKnife", parameter);
            });
        personMap.put("ChestHold".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("ChestHold", parameter);
            });
        personMap.put("Shape".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Shape", parameter);
            });
        personMap.put("Minority".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Minority", parameter);
            });
        personMap.put("HeadLeftTopX".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("HeadLeftTopX", parameter);
                });
        personMap.put("HeadLeftTopY".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("HeadLeftTopY", parameter);
                });
        personMap.put("HeadRightBtmX".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("HeadRightBtmX", parameter);
                });
        personMap.put("HeadRightBtmY".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("HeadRightBtmY", parameter);
                });
        personMap.put("UpperLeftTopX".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("UpperLeftTopX", parameter);
                });
        personMap.put("UpperLeftTopY".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("UpperLeftTopY", parameter);
                });
        personMap.put("UpperRightBtmX".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("UpperRightBtmX", parameter);
                });
        personMap.put("UpperRightBtmY".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("UpperRightBtmY", parameter);
                });
        personMap.put("LowerLeftTopX".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("LowerLeftTopX", parameter);
                });
        personMap.put("LowerLeftTopY".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("LowerLeftTopY", parameter);
                });
        personMap.put("LowerRightBtmX".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("LowerRightBtmX", parameter);
                });
        personMap.put("LowerRightBtmY".toLowerCase(),
                (parameter) -> {
                    ParametercheckUtil.checkInteger("LowerRightBtmY", parameter);
                });
    }

    static {
        motorvehicleMap.put("MotorVehicleID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("MotorVehicleID", parameter, 0, 48);
                ParametercheckUtil.checkSpecialChar("MotorVehicleID", parameter);
            });
        motorvehicleMap.put("InfoKind".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("InfoKind", parameter);
            });

        motorvehicleMap.put("SourceID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SourceID", parameter, 0, 41);
                ParametercheckUtil.checkSpecialChar("SourceID", parameter);
            });
        motorvehicleMap.put("TollgateID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("TollgateID", parameter, 0, 20);
                ParametercheckUtil.checkSpecialChar("TollgateID", parameter);
            });
        motorvehicleMap.put("DeviceID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("DeviceID", parameter, 0, 20);
                ParametercheckUtil.checkSpecialChar("DeviceID", parameter);
            });
        motorvehicleMap.put("StorageUrl1".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("StorageUrl1", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("StorageUrl1", parameter);
            });
        motorvehicleMap.put("StorageUrl2".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("StorageUrl2", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("StorageUrl2", parameter);
            });
        motorvehicleMap.put("StorageUrl3".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("StorageUrl3", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("StorageUrl3", parameter);
            });
        motorvehicleMap.put("StorageUrl4".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("StorageUrl4", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("StorageUrl4", parameter);
            });
        motorvehicleMap.put("StorageUrl5".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("StorageUrl5", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("StorageUrl5", parameter);
            });
        motorvehicleMap.put("LeftTopX".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("LeftTopX", parameter);
            });
        motorvehicleMap.put("LeftTopY".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("LeftTopY", parameter);
            });
        motorvehicleMap.put("RightBtmX".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("RightBtmX", parameter);
            });
        motorvehicleMap.put("RightBtmY".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("RightBtmY", parameter);
            });
        motorvehicleMap.put("MarkTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("MarkTime", parameter);
            });
        motorvehicleMap.put("AppearTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("AppearTime", parameter);
            });
        motorvehicleMap.put("DisappearTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("DisappearTime", parameter);
            });
        motorvehicleMap.put("LaneNo".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("LaneNo", parameter);
            });
        motorvehicleMap.put("HasPlate".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HasPlate", parameter);
            });

        motorvehicleMap.put("PlateClass".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PlateClass", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("PlateClass", parameter);
            });
        motorvehicleMap.put("PlateColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PlateColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("PlateColor", parameter);
            });
        motorvehicleMap.put("PlateNo".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PlateNo", parameter, 0, 15);
                ParametercheckUtil.checkSpecialChar("PlateNo", parameter);
            });
        motorvehicleMap.put("PlateNoAttach".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PlateNoAttach", parameter, 0, 15);
                ParametercheckUtil.checkSpecialChar("PlateNoAttach", parameter);
            });
        motorvehicleMap.put("PlateDescribe".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PlateDescribe", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("PlateDescribe", parameter);
            });
        motorvehicleMap.put("IsDecked".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsDecked", parameter);
            });
        motorvehicleMap.put("IsAltered".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsAltered", parameter);
            });
        motorvehicleMap.put("IsCovered".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsCovered", parameter);
            });
        motorvehicleMap.put("Speed".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDouble("Speed", parameter);
            });

        motorvehicleMap.put("Direction".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Direction", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("Direction", parameter);
            });
        motorvehicleMap.put("DrivingStatusCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("DrivingStatusCode", parameter, 0, 4);
                ParametercheckUtil.checkSpecialChar("DrivingStatusCode", parameter);
            });
        motorvehicleMap.put("UsingPropertiesCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("UsingPropertiesCode", parameter);
            });
        motorvehicleMap.put("VehicleClass".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleClass", parameter, 0, 3);
                ParametercheckUtil.checkSpecialChar("VehicleClass", parameter);
            });
        motorvehicleMap.put("VehicleBrand".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleBrand", parameter, 0, 32);
                ParametercheckUtil.checkSpecialChar("VehicleBrand", parameter);
            });
        motorvehicleMap.put("VehicleModel".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleModel", parameter, 0, 32);
                ParametercheckUtil.checkSpecialChar("VehicleModel", parameter);
            });
        motorvehicleMap.put("VehicleStyles".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleStyles", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleStyles", parameter);
            });
        motorvehicleMap.put("VehicleLength".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("VehicleLength", parameter);
            });
        motorvehicleMap.put("VehicleWidth".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("VehicleWidth", parameter);
            });
        motorvehicleMap.put("VehicleHeight".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("VehicleHeight", parameter);
            });
        motorvehicleMap.put("VehicleColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("VehicleColor", parameter);
            });
        motorvehicleMap.put("VehicleColorDepth".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleColorDepth", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("VehicleColorDepth", parameter);
            });
        motorvehicleMap.put("VehicleHood".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleHood", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleHood", parameter);
            });
        motorvehicleMap.put("VehicleTrunk".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleTrunk", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleTrunk", parameter);
            });
        motorvehicleMap.put("VehicleWheel".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleWheel", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleWheel", parameter);
            });
        motorvehicleMap.put("WheelPrintedPatten".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("WheelPrintedPatten", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("WheelPrintedPatten", parameter);
            });
        motorvehicleMap.put("VehicleWindow".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleWindow", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleWindow", parameter);
            });
        motorvehicleMap.put("VehicleRoof".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleRoof", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleRoof", parameter);
            });
        motorvehicleMap.put("VehicleDoor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleDoor", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleDoor", parameter);
            });
        motorvehicleMap.put("SideOfVehicle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SideOfVehicle", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("SideOfVehicle", parameter);
            });
        motorvehicleMap.put("CarOfVehicle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CarOfVehicle", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("CarOfVehicle", parameter);
            });
        motorvehicleMap.put("RearviewMirror".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("RearviewMirror", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("RearviewMirror", parameter);
            });
        motorvehicleMap.put("VehicleChassis".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleChassis", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleChassis", parameter);
            });
        motorvehicleMap.put("VehicleShielding".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleShielding", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleShielding", parameter);
            });
        motorvehicleMap.put("FilmColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("FilmColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("FilmColor", parameter);
            });
        motorvehicleMap.put("IsModified".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsModified", parameter);
            });

        motorvehicleMap.put("HitMarkInfo".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("HitMarkInfo", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("HitMarkInfo", parameter);
            });
        motorvehicleMap.put("VehicleBodyDesc".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleBodyDesc", parameter, 0, 128);
                ParametercheckUtil.checkSpecialChar("VehicleBodyDesc", parameter);
            });
        motorvehicleMap.put("VehicleFrontItem".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleFrontItem", parameter, 0, 50);
                ParametercheckUtil.checkSpecialChar("VehicleFrontItem", parameter);
            });
        motorvehicleMap.put("DescOfFrontItem".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("DescOfFrontItem", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("DescOfFrontItem", parameter);
            });
        motorvehicleMap.put("VehicleRearItem".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleRearItem", parameter, 0, 50);
                ParametercheckUtil.checkSpecialChar("VehicleRearItem", parameter);
            });
        motorvehicleMap.put("DescOfRearItem".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("DescOfRearItem", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("DescOfRearItem", parameter);
            });
        motorvehicleMap.put("NumOfPassenger".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("NumOfPassenger", parameter);
            });
        motorvehicleMap.put("PassTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("PassTime", parameter);
            });
        motorvehicleMap.put("NameOfPassedRoad".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("NameOfPassedRoad", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("NameOfPassedRoad", parameter);
            });
        motorvehicleMap.put("IsSuspicious".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsSuspicious", parameter);
            });
        motorvehicleMap.put("Sunvisor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Sunvisor", parameter);
            });
        motorvehicleMap.put("SafetyBelt".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("SafetyBelt", parameter);
            });
        motorvehicleMap.put("Calling".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Calling", parameter);
            });
        motorvehicleMap.put("PlateReliability".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("PlateReliability", parameter);
            });
        motorvehicleMap.put("PlateCharReliability".toLowerCase(), (parameter) -> {
            ParametercheckUtil
                .checkLength("PlateCharReliability", parameter, 0, 64);
            ParametercheckUtil.checkSpecialChar("PlateCharReliability", parameter);
        });
        motorvehicleMap.put("BrandReliability".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("BrandReliability", parameter, 0, 3);
                ParametercheckUtil.checkSpecialChar("BrandReliability", parameter);
            });
        motorvehicleMap.put("SubImageList".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SubImageList", parameter, 0, 2048);
            });
        motorvehicleMap.put("FeatureObject".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("FeatureObject", parameter, 0, 4096);
            });
        motorvehicleMap.put("FirstObj".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("FirstObj", parameter);
            });
        motorvehicleMap.put("StartFrameIdx".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("StartFrameIdx", parameter);
            });
        motorvehicleMap.put("EndFrameIdx".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("EndFrameIdx", parameter);
            });
        motorvehicleMap.put("InsertTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("InsertTime", parameter);
            });
        motorvehicleMap.put("ObjId".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("ObjId", parameter);
            });
        motorvehicleMap.put("SlaveIp".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SlaveIp", parameter, 0, 16);
                ParametercheckUtil.checkSpecialChar("SlaveIp", parameter);
            });
        motorvehicleMap.put("FrameIdx".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("FrameIdx", parameter);
            });
        motorvehicleMap.put("FramePts".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("FramePts", parameter);
            });

        motorvehicleMap.put("Serialnumber".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Serialnumber", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("Serialnumber", parameter);
            });
        motorvehicleMap.put("AnalysisId".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("AnalysisId", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("AnalysisId", parameter);
            });
        motorvehicleMap.put("LicenseAttribution".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("LicenseAttribution", parameter, 0, 100);
                ParametercheckUtil.checkSpecialChar("LicenseAttribution", parameter);
            });
        motorvehicleMap.put("HasCrash".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HasCrash", parameter);
            });
        motorvehicleMap.put("HasDanger".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HasDanger", parameter);
            });
        motorvehicleMap.put("LocationInfo".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("LocationInfo", parameter, 0, 100);
                ParametercheckUtil.checkSpecialChar("LocationInfo", parameter);
            });
        motorvehicleMap.put("CarLogo".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CarLogo", parameter, 0, 50);
                ParametercheckUtil.checkSpecialChar("CarLogo", parameter);
            });
        motorvehicleMap.put("SecondBelt".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("SecondBelt", parameter);
            });
        motorvehicleMap.put("VehicleConfidence".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("VehicleConfidence", parameter);
            });
        motorvehicleMap.put("SunRoof".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("SunRoof", parameter);
            });
        motorvehicleMap.put("SpareTire".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("SpareTire", parameter);
            });
        motorvehicleMap.put("Rack".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Rack", parameter);
            });
        motorvehicleMap.put("CreateTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("CreateTime", parameter);
            });
        motorvehicleMap.put("FaceUUID1".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("FaceUUID1", parameter, 0, 48);
                ParametercheckUtil.checkSpecialChar("FaceUUID1", parameter);
            });
        motorvehicleMap.put("FaceUUID2".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("FaceUUID2", parameter, 0, 48);
                ParametercheckUtil.checkSpecialChar("FaceUUID2", parameter);
            });
        motorvehicleMap.put("ObjType".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("ObjType", parameter);
            });

        motorvehicleMap.put("AssistSnapshot".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("AssistSnapshot", parameter, 0, 1000);
                //ParametercheckUtil.checkSpecialChar("AssistSnapshot", parameter);
            });
        motorvehicleMap.put("MainDriver".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("MainDriver", parameter, 0, 100);
                ParametercheckUtil.checkSpecialChar("MainDriver", parameter);
            });
        motorvehicleMap.put("CoDriver".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CoDriver", parameter, 0, 100);
                ParametercheckUtil.checkSpecialChar("CoDriver", parameter);
            });
        motorvehicleMap.put("TagNum".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("TagNum", parameter);
            });
        motorvehicleMap.put("HasSmoke".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HasSmoke", parameter);
            });
        motorvehicleMap.put("HasFaceCover".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HasFaceCover", parameter);
            });
        motorvehicleMap.put("Sex".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Sex", parameter, 0, 10);
                ParametercheckUtil.checkSpecialChar("Sex", parameter);
            });
        motorvehicleMap.put("Id".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Id", parameter, 0, 48);
                ParametercheckUtil.checkSpecialChar("Id", parameter);
            });
        motorvehicleMap.put("Imgurl".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Imgurl", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("Imgurl", parameter);
            });
        motorvehicleMap.put("BigImgUrl".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("BigImgUrl", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("BigImgUrl", parameter);
            });
        motorvehicleMap.put("Tag".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Tag", parameter);
            });
        motorvehicleMap.put("Paper".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Paper", parameter);
            });
        motorvehicleMap.put("Sun".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Sun", parameter);
            });
        motorvehicleMap.put("Drop".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Drop", parameter);
            });
        motorvehicleMap.put("Aerial".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Aerial", parameter);
            });
        motorvehicleMap.put("Decoration".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Decoration", parameter);
            });
        motorvehicleMap.put("CoSunvisor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("CoSunvisor", parameter);
            });
        motorvehicleMap.put("StartFramePts".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("StartFramePts", parameter);
            });
        motorvehicleMap.put("Angle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Angle", parameter);
            });
    }

    static {

        nonMotorvehicleMap.put("NonMotorVehicleID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("NonMotorVehicleID", parameter, 0, 48);
                ParametercheckUtil.checkSpecialChar("NonMotorVehicleID", parameter);
            });
        nonMotorvehicleMap.put("InfoKind".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("InfoKind", parameter);
            });
        nonMotorvehicleMap.put("SourceID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SourceID", parameter, 0, 41);
                ParametercheckUtil.checkSpecialChar("SourceID", parameter);
            });
        nonMotorvehicleMap.put("DeviceID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("DeviceID", parameter, 0, 20);
                ParametercheckUtil.checkSpecialChar("DeviceID", parameter);
            });
        nonMotorvehicleMap.put("LeftTopX".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("LeftTopX", parameter);
            });
        nonMotorvehicleMap.put("LeftTopY".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("LeftTopY", parameter);
            });
        nonMotorvehicleMap.put("RightBtmX".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("RightBtmX", parameter);
            });
        nonMotorvehicleMap.put("RightBtmY".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("RightBtmY", parameter);
            });
        nonMotorvehicleMap.put("MarkTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("MarkTime", parameter);
            });
        nonMotorvehicleMap.put("AppearTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("AppearTime", parameter);
            });
        nonMotorvehicleMap.put("DisappearTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("DisappearTime", parameter);
            });
        nonMotorvehicleMap.put("HasPlate".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HasPlate", parameter);
            });
        nonMotorvehicleMap.put("PlateClass".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PlateClass", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("PlateClass", parameter);
            });
        nonMotorvehicleMap.put("PlateColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PlateColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("PlateColor", parameter);
            });
        nonMotorvehicleMap.put("PlateNo".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PlateNo", parameter, 0, 16);
                ParametercheckUtil.checkSpecialChar("PlateNo", parameter);
            });
        nonMotorvehicleMap.put("PlateNoAttach".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PlateNoAttach", parameter, 0, 16);
                ParametercheckUtil.checkSpecialChar("PlateNoAttach", parameter);
            });
        nonMotorvehicleMap.put("PlateDescribe".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("PlateDescribe", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("PlateDescribe", parameter);
            });
        nonMotorvehicleMap.put("IsDecked".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsDecked", parameter);
            });
        nonMotorvehicleMap.put("IsAltered".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsAltered", parameter);
            });
        nonMotorvehicleMap.put("IsCovered".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsCovered", parameter);
            });
        nonMotorvehicleMap.put("Speed".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDouble("Speed", parameter);
            });
        nonMotorvehicleMap.put("DrivingStatusCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("DrivingStatusCode", parameter, 0, 4);
                ParametercheckUtil.checkSpecialChar("DrivingStatusCode", parameter);
            });
        nonMotorvehicleMap.put("UsingPropertiesCode".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("UsingPropertiesCode", parameter);
            });
        nonMotorvehicleMap.put("VehicleBrand".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleBrand", parameter, 0, 32);
                ParametercheckUtil.checkSpecialChar("VehicleBrand", parameter);
            });
        nonMotorvehicleMap.put("VehicleType".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleType", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleType", parameter);
            });
        nonMotorvehicleMap.put("VehicleLength".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("VehicleLength", parameter);
            });
        nonMotorvehicleMap.put("VehicleWidth".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("VehicleWidth", parameter);
            });
        nonMotorvehicleMap.put("VehicleHeight".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("VehicleHeight", parameter);
            });
        nonMotorvehicleMap.put("VehicleColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleColor", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("VehicleColor", parameter);
            });
        nonMotorvehicleMap.put("VehicleHood".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleHood", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleHood", parameter);
            });
        nonMotorvehicleMap.put("VehicleTrunk".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleTrunk", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleTrunk", parameter);
            });
        nonMotorvehicleMap.put("VehicleWheel".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleWheel", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleWheel", parameter);
            });

        nonMotorvehicleMap.put("WheelPrintedPattern".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("WheelPrintedPattern", parameter, 0, 2);
                ParametercheckUtil.checkSpecialChar("WheelPrintedPattern", parameter);
            });
        nonMotorvehicleMap.put("VehicleWindow".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleWindow", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleWindow", parameter);
            });
        nonMotorvehicleMap.put("VehicleRoof".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleRoof", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleRoof", parameter);
            });
        nonMotorvehicleMap.put("VehicleDoor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleDoor", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleDoor", parameter);
            });
        nonMotorvehicleMap.put("SideOfVehicle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SideOfVehicle", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("SideOfVehicle", parameter);
            });
        nonMotorvehicleMap.put("CarOfVehicle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CarOfVehicle", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("CarOfVehicle", parameter);
            });
        nonMotorvehicleMap.put("RearviewMirror".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("RearviewMirror", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("RearviewMirror", parameter);
            });
        nonMotorvehicleMap.put("VehicleChassis".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleChassis", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleChassis", parameter);
            });
        nonMotorvehicleMap.put("VehicleShielding".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("VehicleShielding", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("VehicleShielding", parameter);
            });
        nonMotorvehicleMap.put("FilmColor".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("FilmColor", parameter);
            });
        nonMotorvehicleMap.put("IsModified".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("IsModified", parameter);
            });
        nonMotorvehicleMap.put("SubImageList".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SubImageList", parameter, 0, 2048);
            });
        nonMotorvehicleMap.put("FeatureObject".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("FeatureObject", parameter, 0, 4096);
            });
        nonMotorvehicleMap.put("StartFrameIdx".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("StartFrameIdx", parameter);
            });
        nonMotorvehicleMap.put("EndFrameIdx".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("EndFrameIdx", parameter);
            });
        nonMotorvehicleMap.put("InsertTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("InsertTime", parameter);
            });
        nonMotorvehicleMap.put("ObjId".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("ObjId", parameter);
            });
        nonMotorvehicleMap.put("SlaveIp".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("SlaveIp", parameter, 0, 16);
                ParametercheckUtil.checkSpecialChar("SlaveIp", parameter);
            });
        nonMotorvehicleMap.put("FrameIdx".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("FrameIdx", parameter);
            });
        nonMotorvehicleMap.put("FramePts".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("FramePts", parameter);
            });
        nonMotorvehicleMap.put("Serialnumber".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Serialnumber", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("Serialnumber", parameter);
            });
        nonMotorvehicleMap.put("AnalysisId".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("AnalysisId", parameter, 0, 64);
                ParametercheckUtil.checkSpecialChar("AnalysisId", parameter);
            });
        nonMotorvehicleMap.put("UpColorTag1".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("UpColorTag1", parameter);
            });
        nonMotorvehicleMap.put("UpColorTag2".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("UpColorTag2", parameter);
            });
        nonMotorvehicleMap.put("MainColorTag".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("MainColorTag", parameter);
            });
        nonMotorvehicleMap.put("MainColorTag1".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("MainColorTag1", parameter);
            });
        nonMotorvehicleMap.put("MainColorTag2".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("MainColorTag2", parameter);
            });
        nonMotorvehicleMap.put("Sex".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Sex", parameter);
            });
        nonMotorvehicleMap.put("Age".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Age", parameter);
            });
        nonMotorvehicleMap.put("Helmet".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Helmet", parameter);
            });
        nonMotorvehicleMap.put("HelmetColorTag1".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HelmetColorTag1", parameter);
            });
        nonMotorvehicleMap.put("HelmetColorTag2".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HelmetColorTag2", parameter);
            });
        nonMotorvehicleMap.put("Glasses".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Glasses", parameter);
            });
        nonMotorvehicleMap.put("Bag".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Bag", parameter);
            });
        nonMotorvehicleMap.put("Umbrella".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Umbrella", parameter);
            });
        nonMotorvehicleMap.put("Angle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Angle", parameter);
            });
        nonMotorvehicleMap.put("Handbag".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Handbag", parameter);
            });
        nonMotorvehicleMap.put("CoatStyle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CoatStyle", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("CoatStyle", parameter);
            });
        nonMotorvehicleMap.put("TrousersStyle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("TrousersStyle", parameter, 0, 5);
                ParametercheckUtil.checkSpecialChar("TrousersStyle", parameter);
            });
        nonMotorvehicleMap.put("LamShape".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("LamShape", parameter);
            });
        nonMotorvehicleMap.put("Respirator".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Respirator", parameter);
            });
        nonMotorvehicleMap.put("Cap".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Cap", parameter);
            });
        nonMotorvehicleMap.put("HairStyle".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("HairStyle", parameter);
            });
        nonMotorvehicleMap.put("CoatTexture".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("CoatTexture", parameter);
            });
        nonMotorvehicleMap.put("TrousersTexture".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("TrousersTexture", parameter);
            });
        nonMotorvehicleMap.put("FaceUUID".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("FaceUUID", parameter, 0, 48);
                ParametercheckUtil.checkSpecialChar("FaceUUID", parameter);
            });
        nonMotorvehicleMap.put("CreateTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("CreateTime", parameter);
            });
        nonMotorvehicleMap.put("CreateTime".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkDate("CreateTime", parameter);
            });
        nonMotorvehicleMap.put("FirstObj".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("FirstObj", parameter);
            });
        nonMotorvehicleMap.put("Wheels".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Wheels", parameter);
            });
        nonMotorvehicleMap.put("BikeGenre".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("BikeGenre", parameter);
            });
        nonMotorvehicleMap.put("ObjType".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("ObjType", parameter);
            });
        nonMotorvehicleMap.put("AssistSnapshot".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("AssistSnapshot", parameter, 0, 1000);
//                ParametercheckUtil.checkSpecialChar("AssistSnapshot", parameter);
            });
        nonMotorvehicleMap.put("CoatColor1".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CoatColor1", parameter, 0, 10);
                ParametercheckUtil.checkSpecialChar("CoatColor1", parameter);
            });
        nonMotorvehicleMap.put("CoatColor2".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("CoatColor2", parameter, 0, 10);
                ParametercheckUtil.checkSpecialChar("CoatColor2", parameter);
            });
        nonMotorvehicleMap.put("TrousersColor1".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("TrousersColor1", parameter, 0, 10);
                ParametercheckUtil.checkSpecialChar("TrousersColor1", parameter);
            });
        nonMotorvehicleMap.put("TrousersColor2".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("TrousersColor2", parameter, 0, 10);
                ParametercheckUtil.checkSpecialChar("TrousersColor2", parameter);
            });
        nonMotorvehicleMap.put("Id".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Id", parameter, 0, 48);
                ParametercheckUtil.checkSpecialChar("Id", parameter);
            });
        nonMotorvehicleMap.put("Imgurl".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("Imgurl", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("Imgurl", parameter);
            });
        nonMotorvehicleMap.put("BigImgUrl".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLength("BigImgUrl", parameter, 0, 256);
                ParametercheckUtil.checkSpecialChar("BigImgUrl", parameter);
            });
        nonMotorvehicleMap.put("StartFramePts".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkLong("StartFramePts", parameter);
            });
        nonMotorvehicleMap.put("LampShape".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("LampShape", parameter);
            });
        nonMotorvehicleMap.put("CarryPassenger".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("CarryPassenger", parameter);
            });
        nonMotorvehicleMap.put("Canopy".toLowerCase(),
            (parameter) -> {
                ParametercheckUtil.checkInteger("Canopy", parameter);
            });
    }

    public static void checkParameter(Class tclass, String key, Object value) {
        SingleParameterCheck check = null;
        if (tclass == PersonResult.class) {
            check = personMap.get(key);
        } else if (tclass == FaceResult.class) {
            check = faceMap.get(key);
        } else if (tclass == NonMotorVehiclesResult.class) {
            check = nonMotorvehicleMap.get(key);
        } else if (tclass == VlprResult.class) {
            check = motorvehicleMap.get(key);
        }
        if (null != check) {
            check.checkParameter(value);
        }
    }
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-11-22 11:02
 **/