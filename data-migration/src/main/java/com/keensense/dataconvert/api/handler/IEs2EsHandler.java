package com.keensense.dataconvert.api.handler;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.handler.es2es
 * @Description： <p> IEs2EsHandler  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 16:34
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
public interface IEs2EsHandler {

    /**
     * 加载Source数据源
     * @param loadConfig
     * @return
     */
    void loadSourceData(JSONObject loadConfig);

    /**
     * 对数据进行业务处理
     * @param sourceList
     * @param convertConfig
     * @return
     */
    void convertData(List<JSONObject> sourceList, JSONObject convertConfig);

    /**
     * 保存到Target数据源
     * @param sourceList
     * @param targetConfig
     */
    void saveIntoTarget(List<JSONObject> sourceList,JSONObject targetConfig);

}
