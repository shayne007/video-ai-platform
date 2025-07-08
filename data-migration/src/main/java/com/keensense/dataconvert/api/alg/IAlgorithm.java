package com.keensense.dataconvert.api.alg;

import com.keensense.dataconvert.biz.entity.PictureInfo;
import com.loocme.sys.datastruct.Var;
import net.sf.json.JSONArray;

import java.util.List;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.alg
 * @Description： <p> IAlgorithm - 算法接口  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 16:51
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
public interface IAlgorithm {

    /**
     * 特征值 再次识别算法服务 [6K , 1.5K 接口 ]
     * 1. 调用接口 - [构造请求参数等]
     * 2. 处理封装 - 返回到调用者
     * ============================
     * 调用者将1.5K 数据 推送到 kafka 指定topic
     */
    /**
     * getRequestParam  设置请求需要的参数
     * @param config
     * @param imgArr
     * @return
     */
    String getRequestParam(Var config, JSONArray imgArr);

    /**
     * getRequestUrl - 算法服务url
     * @return
     */
    String getRequestUrl();


    /**
     * getResponseCode - 响应code
     * @param responseVar
     * @return
     */
    String getResponseCode(Var responseVar);

    /**
     * dealResponse 处理响应数据
     * @param list
     * @param responseVar
     */
    void dealResponse(List<PictureInfo> list, Var responseVar);

}
