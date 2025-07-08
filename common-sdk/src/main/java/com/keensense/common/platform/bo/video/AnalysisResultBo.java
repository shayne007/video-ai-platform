package com.keensense.common.platform.bo.video;

import com.keensense.common.platform.domain.FaceResult;
import com.keensense.common.platform.domain.NonMotorVehiclesResult;
import com.keensense.common.platform.domain.PersonResult;
import com.keensense.common.platform.domain.Result;
import com.keensense.common.platform.domain.VlprResult;
import lombok.Data;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 17:54 2019/7/18
 * @Version v0.1
 */
@Data
public class AnalysisResultBo extends Result {
    FaceResult faceResult;
    PersonResult personResult;
    VlprResult vlprResult;
    NonMotorVehiclesResult nonMotorVehiclesResult;
}
