package com.keensense.extension2.dataobject;

import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author jingege
 * @description:
 * @return:
 */
@Data
@Accessors(chain = true)
@Table(TableName = "archives_library_info")
public class ArchiveLibraryInfo {
    
    @Id
    @Column(ColumnName = "id")
    private String id;
    @Column(ColumnName = "library_name")
    private String libraryName;
    @Column(ColumnName = "library_type")
    private Integer libraryType;
    @Column(ColumnName = "preset_start_time")
    private Date presetStartTime;
    @Column(ColumnName = "preset_end_time")
    private Date presetEndTime;
    @Column(ColumnName = "create_time")
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
