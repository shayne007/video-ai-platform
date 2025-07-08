package com.keensense.admin.service.ext;

import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.common.platform.bo.video.AnalysisResultBo;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 获取目标结果
 * @Date: 2019/7/18.
 */
public interface QueryAnalysisResultService {
    Map<String, Object> doHttpService(Map<String, Object> paramMap, ResultQueryVo paramBo);

    Map<String, Object> dealResultData(String resultJson, String objType, String order);

    String doHttpService(Map<String, Object> paramMap);

    List<ResultQueryVo> transAllResultToBo(String retJson);

    String groupResult(Map<String, Object> paramMap);

    AnalysisResultBo transOneResultToBo(String retJson);
}
