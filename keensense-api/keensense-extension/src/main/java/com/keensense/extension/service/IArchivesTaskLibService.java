package com.keensense.extension.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.extension.entity.ArchivesTaskLib;
import com.baomidou.mybatisplus.extension.service.IService;
import com.loocme.sys.datastruct.Var;

/**
 * <p>
 * 任务时间库对应 服务类
 * </p>
 *
 * @author ycl
 * @since 2019-06-08
 */
public interface IArchivesTaskLibService extends IService<ArchivesTaskLib> {

    /**
     * 特征录入
     * @param featureObject feature
     * @return 特征id
     */
    String enrollFeature(JSONObject featureObject);

    /**
     * 特征检索
     * @param searchParam 检索参数
     * @return faces
     */
    JSONArray searchFeature(JSONObject searchParam);

}
