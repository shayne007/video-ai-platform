package com.keensense.picturestream.controller;

import com.keensense.common.util.ResultUtils;
import com.keensense.picturestream.config.NacosConfig;
import com.keensense.picturestream.util.kafka.KafkaUtil;
import com.loocme.sys.util.OpencvUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/picturestream")
@Api("图片数据控制器")
public class PicturestreamController {

    @Autowired
    private NacosConfig nacosConfig;

    @ApiOperation(value = "图片数据推送", notes = "图片数据推送")
    @PostMapping(value = "/loadPicture")
    public String loadPicture(@RequestBody String body) {
        log.info("kakou data push kafka");
        KafkaUtil.sendMessage("dag_face_analysis","JdFaceCjQst", body, nacosConfig.getKafkaBootstrap());
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "测试opencv", notes = "测试opencv")
    @PostMapping(value = "/opencv")
    public String opencv() {
        //172.16.1.29:8890/rest/picturestream/opencv
        OpencvUtil util = OpencvUtil.getInstance();
        log.info("opencv succuse");
        return util.toString();
    }
}
