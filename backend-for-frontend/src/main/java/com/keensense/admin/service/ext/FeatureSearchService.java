package com.keensense.admin.service.ext;

import com.keensense.common.platform.bo.feature.DumpQuery;

import java.util.Map;

/**
 * @Author cuiss
 * @Description 以图搜图
 * @Date 2018/10/16
 */
public interface FeatureSearchService {
    /**
     * 调用JManager服务
     *
     * @param paramMap
     * @return
     */
    String doSearchService(Map<String, Object> paramMap);

    /**
     * 图片特征提取接口
     *
     * @param paramMap
     * @return
     */
    String doExtractFromPictureService(Map<String, Object> paramMap);

    String doExtractFromPictureGLFace(String pictureUrl);

    /**
     * 图片目标检测接口
     *
     * @param paramMap
     * @return
     */
    String doStructPictureService(Map<String, Object> paramMap);

    /**
     * 查询底库特征值
     *
     * @param dumpQuery
     * @return
     */
    String doDumpService(DumpQuery dumpQuery);

    /**
     * 调用视图库dump接口查询低库特征值
     *
     * @param dumpQuery
     * @return
     */
    String doFeatureDumpService(DumpQuery dumpQuery);
}
