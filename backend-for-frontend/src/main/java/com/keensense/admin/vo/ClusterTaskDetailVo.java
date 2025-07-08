package com.keensense.admin.vo;

import com.keensense.admin.request.PageRequest;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:59 2019/11/26
 * @Version v0.1
 */
@Data
public class ClusterTaskDetailVo {
    long detailId;
    List<ResultQueryVo> results;
}
