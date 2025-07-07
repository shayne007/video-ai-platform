package com.keensense.extension2.service;

import com.keensense.extension.entity.dto.ArchivesDTO;
import com.keensense.extension2.dataobject.ArchiveLibraryInfo;
import com.keensense.extension2.linkobject.ArchiveIn;
import com.keensense.extension2.linkobject.ArchiveOut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArchivesManageService {
    
    /**
     * 通过库类型获取当前库信息
     *
     * @param type 1-人脸；2-人形（正面）；3-人形（侧面）；4-人形（背面）
     * @return 档案库的信息
     */
    public ArchiveLibraryInfo getArchiveLibId(int type) {
        return null;
    }
    
    /**
     * 通过输入添加档案
     */
    public ArchiveOut addArchive(List<ArchiveIn> archiveIns) {
        return null;
    }
    
    /**
     * 通过输入，对原有档案进行补充：
     * 人脸：补充侧脸、低头
     * 人形：补充正面、侧面、背面
     */
    public ArchiveOut optArchive(ArchiveOut orgArchive, List<ArchiveIn> archiveIns) {
        return null;
    }
    
    /**
     * 合并两个档案 将两个档案的pid设置为同一个，通过archiveFaceZheng来判断 当archive1.archiveFaceZheng.id==archive1.archiveFaceZheng.pid，则将archive2的所有pid均设置为archive1.archiveFaceZheng.pid
     * 当archive1.archiveFaceZheng.id!=archive1.archiveFaceZheng.pid &&
     * archive2.archiveFaceZheng.id==archive2.archiveFaceZheng.pid，则将archive1的所有pid设置为archive2.archiveFaceZheng.pid
     * 当archive1.archiveFaceZheng.id!=archive1.archiveFaceZheng.pid &&
     * archive2.archiveFaceZheng.id!=archive2.archiveFaceZheng.pid，按第一条规则处理
     *
     * @param archive1 第一个档案信息
     * @param archive2 第二个档案信息
     * @return 合并后的档案信息
     */
    public ArchiveOut combineArchive(ArchiveOut archive1, ArchiveOut archive2) {
        return null;
    }
    
    /**
     * 通过aid列表查询档案列表
     */
    public List<ArchiveOut> selectArchives(List<String> aids) {
        return null;
    }
    
    /**
     * 通过aid获取档案信息
     */
    public ArchiveOut selectArchive(String aid) {
        return null;
    }
    
    /**
     * 通过人脸特征和阈值，获取档案中相似度超过阈值的档案列表
     *
     * @param feature 人脸特征
     * @param thredhold 阈值
     * @return 抓拍库档案列表
     */
    public List<ArchiveOut> selectArchives(String feature, float thredhold) {
        return null;
    }
}
