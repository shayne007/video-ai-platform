package com.keensense.dataconvert.framework.config.ehcache;

import com.keensense.dataconvert.biz.common.consts.ConfigPathConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @projectName：noSeatBelt
 * @Package：cn.jiuling.config.ehcache
 * @Description： <p> EhCacheConfig  </p>
 * @Author： - Jason
 * @CreatTime：2019/4/3 - 10:24
 * @Modify By：
 * @ModifyTime： 2019/4/3
 * @Modify marker：
 */
@Configuration
@ConditionalOnProperty(name = "enabled", havingValue = "true",prefix="ehcache")
public class EhCacheConfig {

    /**
     * @Description: ehcache 主要的管理器
     * @param bean   Spring管理
     * @return EhCacheCacheManager
     * @Autor: Jason
     */
    @Bean(name = "appEhCacheCacheManager")
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean){
        return new EhCacheCacheManager (bean.getObject ());
    }

    /**
     * @Description: Spring分别通过CacheManager.create()或new CacheManager()方式来创建一个ehcache基地
     * @return EhCacheManagerFactoryBean
     * @Autor: Jason
     */
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean ();
        cacheManagerFactoryBean.setConfigLocation (new ClassPathResource(ConfigPathConstant.CONFIG_EHCACHE_XML));
        cacheManagerFactoryBean.setShared (true);
        return cacheManagerFactoryBean;
    }

}
