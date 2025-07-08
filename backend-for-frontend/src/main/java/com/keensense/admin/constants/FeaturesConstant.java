package com.keensense.admin.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段对应的featuresCode
 *
 * @author Administrator
 */
public class FeaturesConstant {
    private FeaturesConstant() {
    }
    // ************************大类********************/
    /**
     * 人员基本特征
     */
    public static final String FEATURE_CLASS_PERSON_BASE = "29";

    /**
     * 人员基本特征名
     */
    public static final String FEATURE_CLASS_PERSON_BASE_NAME = "基本特征";
    /**
     * 人员服饰特征
     */
    public static final String FEATURE_CLASS_PERSON_CLOSE = "30";

    // ************************小类********************/
    /**
     * 人员类型
     */
    public static final String USERTYPE = "62";
    /**
     * 人员类型
     */
    public static final Long USERTYPELONG = 62l;
    /**
     * 姓名
     */
    public static final String USERNAME = "63";
    /**
     * 姓名
     */
    public static final Long USERNAMELONG = 63l;
    /**
     * 性别
     */
    public static final String SEX = "64";
    /**
     * 性别
     */
    public static final Long SEXLONG = 64l;
    /**
     * 身高
     */
    public static final String HEIGHT = "68";
    /**
     * 身高
     */
    public static final Long HEIGHTLONG = 68l;
    /**
     * 证件号码
     */
    public static final Long CARDNOLONG = 184l;
    /**
     * 证件号码
     */
    public static final String CARDNO = "184";
    /**
     * 年龄
     */
    public static final String AGE = "65";
    /**
     * 体型
     */
    public static final String WEIGHT = "67";
    /**
     * 肤色
     */
    public static final String SKINCOLOR = "69";
    /**
     * 体表特征
     */
    public static final String BODYFEATURES = "70";
    /**
     * 证件号码
     */
    public static final Long CARLICENSELONG = 91l;
    /**
     * 证件号码
     */
    public static final String CARLICENSE = "91";

    /**
     * 异常结果描述
     */

    public static final String EXCEPTDESC = "未知";

    /**
     * 是否戴口罩/帽子
     */
    private static final Map<Integer, String> RESPIRATOR_CAP_MAP = new HashMap<>();

    static {
        RESPIRATOR_CAP_MAP.put(-1, "未知");
        RESPIRATOR_CAP_MAP.put(1, "是");
        RESPIRATOR_CAP_MAP.put(0, "否");
    }

    public static Map<Integer, String> getPiratorCap() {
        return RESPIRATOR_CAP_MAP;
    }

    /**
     * 上衣纹理
     */

    private static final Map<Integer, String> COAT_TEXTURE_MAP = new HashMap<>();

    static {
        COAT_TEXTURE_MAP.put(-1, "未知");
        COAT_TEXTURE_MAP.put(1, "净色");
        COAT_TEXTURE_MAP.put(2, "间条");
        COAT_TEXTURE_MAP.put(3, "格子");
        COAT_TEXTURE_MAP.put(4, "图案");
        COAT_TEXTURE_MAP.put(5, "拼接");
    }

    public static Map<Integer, String> getCoatTexture() {
        return COAT_TEXTURE_MAP;
    }

    /**
     * 下衣纹理
     */

    private static final Map<Integer, String> TROUSERS_TEXTURE_MAP = new HashMap<>();

    static {
        TROUSERS_TEXTURE_MAP.put(-1, "未知");
        TROUSERS_TEXTURE_MAP.put(1, "净色");
        TROUSERS_TEXTURE_MAP.put(2, "间条");
        TROUSERS_TEXTURE_MAP.put(3, "图案");
    }

    public static Map<Integer, String> getTrousersTexture() {
        return TROUSERS_TEXTURE_MAP;
    }

    /**
     * 非机动车类型
     */

    private static final Map<Integer, String> BIKE_GENRE_MAP = new HashMap<>();

    static {
        //BIKE_GENRE_MAP.put(1, "女士摩托车");
        BIKE_GENRE_MAP.put(2, "摩托车");
        BIKE_GENRE_MAP.put(3, "自行车");
        // BIKE_GENRE_MAP.put(4, "电动车");
        BIKE_GENRE_MAP.put(5, "三轮车");
    }

    public static Map<Integer, String> getBikeGenre() {
        return BIKE_GENRE_MAP;
    }

    private static Map<Integer, String> SOCIAL_ATTRIBUTE = new HashMap<>();

    static {
        SOCIAL_ATTRIBUTE.put(1, "普通");
        SOCIAL_ATTRIBUTE.put(2, "外卖");
        SOCIAL_ATTRIBUTE.put(3, "快递");
        SOCIAL_ATTRIBUTE.put(-1, "未知");
    }

    public static Map<Integer, String> getSocialAttribute() {
        return SOCIAL_ATTRIBUTE;
    }

    private static Map<Integer, String> ENTERPRISE = new HashMap<>();

    static {
        ENTERPRISE.put(1, "中通");
        ENTERPRISE.put(2, "韵达");
        ENTERPRISE.put(3, "圆通");
        ENTERPRISE.put(4, "申通");
        ENTERPRISE.put(5, "中国邮政");
        ENTERPRISE.put(6, "顺丰");
        ENTERPRISE.put(7, "京东");
        ENTERPRISE.put(8, "百事汇通");
        ENTERPRISE.put(9, "美团");
        ENTERPRISE.put(10, "饿了么");
        ENTERPRISE.put(-1, "未知");
    }

    public static Map<Integer, String> getEnterprise() {
        return ENTERPRISE;
    }
}
