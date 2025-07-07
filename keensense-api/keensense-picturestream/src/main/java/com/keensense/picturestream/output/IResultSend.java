package com.keensense.picturestream.output;

import com.keensense.picturestream.entity.PictureInfo;
import java.util.List;

public interface IResultSend {
    
    /**
     * receive Var data pulic.
     */
    boolean receiveData(List<PictureInfo> pictureInfos);

}
