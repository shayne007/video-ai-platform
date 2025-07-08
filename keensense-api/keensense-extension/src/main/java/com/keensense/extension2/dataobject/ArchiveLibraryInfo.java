package com.keensense.extension2.dataobject;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author jingege
 * @description:
 * @return:
 */
@Data
@Accessors(chain = true)
@Table(name = "archives_library_info")
public class ArchiveLibraryInfo {
    
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "library_name")
    private String libraryName;
    @Column(name = "library_type")
    private Integer libraryType;
    @Column(name = "preset_start_time")
    private Date presetStartTime;
    @Column(name = "preset_end_time")
    private Date presetEndTime;
    @Column(name = "create_time")
    private Date createTime;
    
    
    public ArchiveLibraryInfo() {
    }
    
    public ArchiveLibraryInfo(String id, int libraryType, Date presetStartTime, Date presetEndTime,
        Date createTime) {
        this.id = id;
        this.libraryType = libraryType;
        this.presetStartTime = presetStartTime;
        this.presetEndTime = presetEndTime;
        this.createTime = createTime;
    }
    
}
