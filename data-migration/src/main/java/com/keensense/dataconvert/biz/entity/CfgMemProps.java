package com.keensense.dataconvert.biz.entity;

import java.util.Date;

/**
 * @ClassName：CfgMemProps
 * @Description： <p> CfgMemProps - 数据库配置文件  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 9:13
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 * @version V1.0
*/
public class CfgMemProps extends CfgMemPropsKey {

    /**
     * 参数name
     */
    private String propName;

    /**
     * 参数value
     */
    private String propValue;

    /**
     * 参数描述
     */
    private String propDesc;

    /**
     * 更新时间
     */
    private Date updateTime;

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName == null ? null : propName.trim();
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue == null ? null : propValue.trim();
    }

    public String getPropDesc() {
        return propDesc;
    }

    public void setPropDesc(String propDesc) {
        this.propDesc = propDesc == null ? null : propDesc.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}