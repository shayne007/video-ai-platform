package com.keensense.image.repository;

/**
 * Created by zhanx xiaohui on 2019-03-13.
 */
public interface ImageRepository {
    String saveToFileServer(String imageStr, String fileExtentionName, String id);

    long batchDelete(String serialNumber, String time);
}
