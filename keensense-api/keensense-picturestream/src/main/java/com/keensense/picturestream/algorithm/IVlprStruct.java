package com.keensense.picturestream.algorithm;

import com.keensense.picturestream.entity.PictureInfo;
import com.loocme.sys.datastruct.Var;

import java.util.List;

public interface IVlprStruct {

    void init(Var params);
    void recog(PictureInfo pictureInfo);
    void recog(List<PictureInfo> pictureList);
    int getBatchSize();
}
