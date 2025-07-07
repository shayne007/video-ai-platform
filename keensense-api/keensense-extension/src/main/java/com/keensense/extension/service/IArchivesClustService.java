package com.keensense.extension.service;


import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2019-05-06
 */
public interface IArchivesClustService  {

    void updateEsFace(List<String> ids,String archivesId);

    void updateEsBody(List<String> ids,String archivesId,Integer tsType);

    void startFaceClust();

    void startBodyClust();

    void addArchivesBodyInfo(Float bodyQuality, Integer angle,String archivesId,String bodyImgUrl,String bodyFeature);

    void updEsRelationByBody();
}
