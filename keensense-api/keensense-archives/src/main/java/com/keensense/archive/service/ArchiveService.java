package com.keensense.archive.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by memory_fu on 2019/12/26.
 */
public interface ArchiveService {
    
    /**
     * 档案搜图(根据特征查找符合条件的档案封面)
     * @return
     */
    String archiveTitleSearch(JSONObject jsonObject);
    
    /**
     * 更新档案ID
     * @param jsonObject
     * @return
     */
    String updateArchive(JSONObject jsonObject);
}
