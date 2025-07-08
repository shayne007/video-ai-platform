package com.keensense.admin.picturestream.output;

import com.keensense.admin.picturestream.entity.PictureInfo;

import java.util.List;

public interface IResultSend {
    
    /**
     * receive Var data pulic.
     */
    boolean receiveData(List<PictureInfo> pictureInfos);

}
