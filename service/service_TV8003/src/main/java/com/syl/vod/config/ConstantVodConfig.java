package com.syl.vod.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "aliyun.vod.file"
)
@Data
public class ConstantVodConfig {
    private String keyid;
    private String keysecret;
}
