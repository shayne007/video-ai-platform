package com.keensense.extension.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author jingege
 * @return:
 */
@Data
@Accessors(chain = true)
@TableName("archives_library_info")
public class LibraryInfo {

    @TableId
    private String id;
    private String libraryName;
    private Integer libraryType;
    private Date presetStartTime;
    private Date presetEndTime;
    private Date createTime;


    public LibraryInfo(){}

    public LibraryInfo(String id,int libraryType, Date presetStartTime, Date presetEndTime, Date createTime){
        this.id = id;
        this.libraryType = libraryType;
        this.presetStartTime = presetStartTime;
        this.presetEndTime = presetEndTime;
        this.createTime = createTime;
    }

}
