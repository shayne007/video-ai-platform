package com.keensense.extension2.linkobject;

import com.keensense.extension2.dataobject.ArchivesInfo;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArchiveOut {
    
    private ArchivesInfo archiveFaceZheng;
    private ArchivesInfo archiveFaceCe;
    private ArchivesInfo archiveFaceDi;
    private ArchivesInfo archiveBodyZheng;
    private ArchivesInfo archiveBodyCe;
    private ArchivesInfo archiveBodyBei;
    
}
