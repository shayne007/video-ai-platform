package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.keensense.search.tool_interface.ParameterCheck;
import com.keensense.search.utils.ParametercheckUtil;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.Table;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * Created by zhanx xiaohui on 2019-09-10.
 */
@Data
@Table(TableName = "crowddensity_result")
@ToString(callSuper=true, includeFieldNames=true)
public class CrowdDensityResult implements ParameterCheck {
    //主键
    @Id
    @JSONField(name = "Id")
    @Column(ColumnName = "id")
    private String id;

    //任务编号
    @JSONField(name = "Serialnumber")
    @Column(ColumnName = "serialnumber")
    private String serialnumber;

    //总人数
    @JSONField(name = "Count")
    @Column(ColumnName = "count")
    private Integer count;

    //抓拍场景图片访问地址
    @JSONField(name = "PicUrl")
    @Column(ColumnName = "picurl")
    private String picUrl;

    //创建时间
    @JSONField(name = "CreateTime" ,format = "yyyyMMddHHmmss")
    @Column(ColumnName = "createtime")
    private Date createTime;

    //该字段用于存储每个ROI的人群密度信息
    @JSONField(name = "DensityInfo")
    @Column(ColumnName = "densityinfo")
    private String densityInfo;

    //人头位置信息
    @JSONField(name = "HeadPosition")
    @Column(ColumnName = "headposition")
    private String headPosition;

    @Override
    public void checkParameter() {
        ParametercheckUtil.checkEmpty("Id", id);
        ParametercheckUtil.checkEmpty("Count", count);
        ParametercheckUtil.checkEmpty("Serialnumber", serialnumber);
        ParametercheckUtil.checkEmpty("PicUrl", picUrl);
        ParametercheckUtil.checkEmpty("CreateTime", createTime);
        ParametercheckUtil.checkLength("Id", id, 1,48);
        ParametercheckUtil.checkLength("Serialnumber", serialnumber, 1,60);
        ParametercheckUtil.checkLength("PicUrl", picUrl, 0,128);
    }
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-09-10 10:54
 **/