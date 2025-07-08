package com.keensense.dataconvert.biz.entity;

/**
 * @ClassName：CfgMemPropsKey
 * @Description： <p> CfgMemPropsKey 数据库配置文件 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 9:13
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 * @version V1.0
*/
public class CfgMemPropsKey {

    /**
     * 模块名
     */
    private String moduleName;

    /**
     * 属性key
     */
    private String propKey;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey == null ? null : propKey.trim();
    }
}