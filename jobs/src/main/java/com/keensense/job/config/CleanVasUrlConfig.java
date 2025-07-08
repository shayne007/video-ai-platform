package com.keensense.job.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cleanvas-url")
public class CleanVasUrlConfig {

    /**
     * url
     */
    @Setter
    @Getter
    private String url;
}
