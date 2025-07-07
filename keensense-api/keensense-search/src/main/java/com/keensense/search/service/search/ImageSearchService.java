package com.keensense.search.service.search;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhanx xiaohui on 2019-05-14.
 */
public interface ImageSearchService {
    String search(JSONObject jsonObject, int objType) throws InterruptedException;
    
    /**
     * 下载特征
     * @param json
     * @return
     */
    String featureDump(JSONObject json);
}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-05-14 09:55
 **/