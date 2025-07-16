package com.keensense.extension.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.common.exception.VideoException;
import com.keensense.extension.entity.ArchivesInfo;
import com.keensense.extension.entity.CameraRelationInfo;
import com.keensense.extension.entity.dto.ArchivesDTO;
import com.keensense.extension.entity.dto.CameraRelationDTO;
import com.keensense.extension.entity.dto.TrailConditionDTO;
import com.keensense.extension.entity.dto.TrailDTO;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2019-05-06
 */
public interface ICameraRelationInfoService extends IService<CameraRelationInfo> {
    
    String insertCameraRelation(String jsonStr);
    
    String deleteCameraRelation(String jsonStr);
    
    String queryCameraRelation(String jsonStr);

}
