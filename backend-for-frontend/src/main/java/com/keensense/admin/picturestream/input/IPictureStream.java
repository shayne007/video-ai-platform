package com.keensense.admin.picturestream.input;

import com.keensense.admin.picturestream.entity.PictureInfo;

import java.util.List;

public interface IPictureStream {

    boolean init();

    List<PictureInfo> loadPictureRecords();
}
