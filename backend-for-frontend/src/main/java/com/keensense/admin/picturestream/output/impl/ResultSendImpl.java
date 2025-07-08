package com.keensense.admin.picturestream.output.impl;

import com.keensense.admin.picturestream.entity.PictureInfo;
import com.keensense.admin.picturestream.output.IResultSend;

import java.util.List;

/**
 * Created by memory_fu on 2019/7/17.
 */
public class ResultSendImpl implements IResultSend {

    @Override
    public boolean receiveData(List<PictureInfo> pictureInfos) {
        
        boolean result = new ResultSendKafka().receiveData(pictureInfos);
        return result;
    }
}
