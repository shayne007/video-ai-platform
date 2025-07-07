package com.keensense.extension.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.ResponseStatusList;
import com.keensense.extension.entity.ArchivesInfo;
import com.keensense.extension.entity.dto.ArchivesDTO;
import com.keensense.extension.entity.dto.TrailConditionDTO;
import com.keensense.extension.entity.dto.TrailDTO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2019-05-06
 */
public interface IArchivesInfoService extends IService<ArchivesInfo> {

    void saveObjext(ArchivesDTO archivesDTO) throws VideoException;

    void saveConvergenceObjext(ArchivesDTO archivesDTO) throws VideoException;

    List<TrailDTO> getTrailInfos(TrailConditionDTO trailConditionDTO);

    String getArchivesIdInfos(TrailConditionDTO trailConditionDTO);

    String findBodyLibByAngle(Integer angle);
    
    String queryArchives(String jsonStr);
    
    String unbindTrail(String jsonStr);
    
    ResponseStatusList mergeArchives(HttpServletRequest request,String jsonStr);
    
    /**
     * 删除档案信息，
     * @param jsonStr  任务号：serialnumber
     * @return
     */
    String deleteArchives(String jsonStr);
    
}
