package com.keensense.admin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@RefreshScope
public class ComConfig {
    //人脸特征提取对应 0.千视通 1.格林
    @Value("${comConfig.face.feature}")
    private String faceFeature;


}
