package com.keensense.commonlib.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/21 15:49
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@Accessors(chain = true)
@TableName("lib_common_feature_info")
public class CommonFeatureInfo {

    private String id;

    private String libraryId;

    private String imgUrl;

    private Integer featureType;

    private Date createTime;

    private Date updateTime;

    private CommonFeatureInfo(){

    }

    public CommonFeatureInfo(String id, String imgUrl,String libraryId,Integer featureType,Date createTime){
        this.id = id;
        this.imgUrl = imgUrl;
        this.libraryId = libraryId;
        this.featureType = featureType;
        this.createTime = createTime;
        this.updateTime = createTime;
    }

}
