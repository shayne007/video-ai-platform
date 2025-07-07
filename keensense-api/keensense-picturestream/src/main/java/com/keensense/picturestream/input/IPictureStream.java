package com.keensense.picturestream.input;

import com.keensense.picturestream.entity.PictureInfo;

import java.util.List;

public interface IPictureStream {

    boolean init();

    List<PictureInfo> loadPictureRecords();
}
