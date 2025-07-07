package com.keensense.search.repository;

import com.alibaba.fastjson.JSONObject;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by zhanx xiaohui on 2019-03-13.
 */
public interface ImageRepository {
    String getUrl(JSONObject jsonObject, String imageStr, String type, String pricuteType,
        String serialNumber, String root, String appearTimeName, String disappearTimeName, Date markTime, String id);

    long batchDelete(String serialNumber, String time) throws ParseException;

}
