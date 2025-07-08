package com.keensense.admin.util;


import com.keensense.admin.constants.FeaturesConstant;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FeatureFormatUtil {
    private FeatureFormatUtil() {
    }

    public static String getFeatureDesc(Integer feauturValue, String tag) {
        String result = FeaturesConstant.EXCEPTDESC;
        if (feauturValue == null) {
            return result;
        }
        if ("respirator".equals(tag) || "cap".equals(tag)) {
            result = FeaturesConstant.getPiratorCap().get(feauturValue);
        } else if ("coat_texture".equals(tag)) {
            result = FeaturesConstant.getCoatTexture().get(feauturValue);
        } else if ("trousers_texture".equals(tag)) {
            result = FeaturesConstant.getTrousersTexture().get(feauturValue);
        } else if ("bike_genre".equals(tag)) {
            result = FeaturesConstant.getBikeGenre().get(feauturValue);
        } else if ("enterprise".equals(tag)) {
            result = FeaturesConstant.getEnterprise().get(feauturValue);
        }else if("social_attribute".equals(tag)) {
            result = FeaturesConstant.getSocialAttribute().get(feauturValue);
        }
        if (result == null) {
            result = FeaturesConstant.EXCEPTDESC;
        }
        return result;
    }

}
