package com.keensense.densecrowd.service.ext;

import java.util.Map;

/**
 * @Author cuiss
 * @Description 视频结构化输入管理接口
 * @Date 2018/10/11
 */
public interface VideoObjextTaskService {

    /**
     * 3.1.1添加任务接口
     *
     * @param paramMap
     * @return
     */
    String addVsdTaskService(Map<String, Object> paramMap, boolean addRelation);

    /**
     * 3.1.7分析任务进度查询
     *
     * @param paramMap
     * @return
     */
    String queryVsdTaskAllService(Map<String, Object> paramMap);


    /**
     * 3.1.5删除任务接口
     *
     * @param paramMap
     * @return
     */
    String deleteVsdTaskService(Map<String, Object> paramMap);

    /**
     * 3.1.3暂停实时任务接口
     *
     * @param paramMap
     * @return
     */
    String pauseVsdTaskService(Map<String, Object> paramMap);

    /**
     * 3.1.4继续实时任务接口
     *
     * @param paramMap
     * @return
     */
    String continueVsdTaskService(Map<String, Object> paramMap);


    /**
     * 3.1.6更新感兴趣区域
     *
     * @param paramMap
     * @return
     */
    String updateVsdTaskService(Map<String, Object> paramMap);

    String saveImageToFdfs(String imageBase64);
}
