package com.keensense.admin.picturestream.output.impl;

import com.alibaba.fastjson.JSON;
import com.keensense.admin.picturestream.entity.PictureInfo;
import com.keensense.admin.util.KafkaUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by memory_fu on 2019/7/4.
 */
@Slf4j
public class ResultSendKafka {
    
    public boolean receiveData(List<PictureInfo> pictureInfos) {
        
        for (PictureInfo pictureInfo : pictureInfos) {
            try {
                //推送kafka
                pushDataTokafka(pictureInfo);
            } catch (Exception e) {
                log.error("====receiveData Exception:", e);
                return false;
            }
        }
        return true;
    }
    
    /**
     * push data to kafka
     */
    private void pushDataTokafka(PictureInfo pictureInfo) {
        KafkaUtil.sendMessage("dag_face_analysis","JdFaceCjQst", JSON.toJSONString(pictureInfo),"172.16.1.68:39092");
    }
    
}
