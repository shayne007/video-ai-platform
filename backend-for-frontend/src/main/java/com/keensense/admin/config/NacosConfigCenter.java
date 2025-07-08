package com.keensense.admin.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


@Configuration
@Data
@ToString
@RefreshScope
public class NacosConfigCenter {

    /*@Value("${result.export.maxcount}")
    private String exportMaxcount;

    @Value("${converse-network-req-config}")
    private String converseUrlConfig;*/


}
