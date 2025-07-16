package com.keensense.picturestream.algorithm;

import com.keensense.picturestream.entity.PictureInfo;

import java.util.List;
import java.util.Map;

public interface IFaceStruct {

    void init(Map<String, Object> params);
    void recog(PictureInfo pictureInfo);
    void recog(List<PictureInfo> pictureList);
    int getBatchSize();
}
