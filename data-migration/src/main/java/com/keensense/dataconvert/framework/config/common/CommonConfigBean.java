package com.keensense.dataconvert.framework.config.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName：CommonConfigBean
 * @Description： <p> CommonConfigBean  - 是否开启的判断配置 - load application.yaml - 通用配置bean </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 11:09
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 * @version V1.0
*/
@Configuration
public class CommonConfigBean {

    /**
     * ehcacheEnabled 缓存是否开启
     */
    @Value(value = "${ehcache.enabled:true}")
    private  Boolean ehcacheEnabled;

    /**
     * swagger
     */
    @Value(value = "${swagger.enabled:true}")
    private  Boolean swaggerEnabled;

    public Boolean getEhcacheEnabled() {
        return ehcacheEnabled;
    }

    public void setEhcacheEnabled(Boolean ehcacheEnabled) {
        this.ehcacheEnabled = ehcacheEnabled;
    }

    public Boolean getSwaggerEnabled() {
        return swaggerEnabled;
    }

    public void setSwaggerEnabled(Boolean swaggerEnabled) {
        this.swaggerEnabled = swaggerEnabled;
    }
}
