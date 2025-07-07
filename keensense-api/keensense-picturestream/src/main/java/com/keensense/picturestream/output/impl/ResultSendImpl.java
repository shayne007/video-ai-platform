package com.keensense.picturestream.output.impl;

import com.keensense.common.config.SpringContext;
import com.keensense.picturestream.config.NacosConfig;
import com.keensense.picturestream.entity.PictureInfo;
import com.keensense.picturestream.output.IResultSend;
import java.util.List;

/**
 * Created by memory_fu on 2019/7/17.
 */
public class ResultSendImpl implements IResultSend {
    

    private NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    @Override
    public boolean receiveData(List<PictureInfo> pictureInfos) {
        
        boolean result = true;
        String warehouse = nacosConfig.getPushDataWareHouse();
        if ("1".equals(warehouse)) {
            result = new ResultSendKafka().receiveData(pictureInfos);
        }
        
        if ("2".equals(warehouse)) {
            result = new ResultSendKeensense().receiveData(pictureInfos);
        }
        
        if ("3".equals(warehouse)) {
            boolean rkafka = new ResultSendKafka().receiveData(pictureInfos);
            boolean rKeensense = new ResultSendKeensense().receiveData(pictureInfos);
            if(!rkafka || !rKeensense){
                result = false;
            }
        }

        return result;
    }
}
