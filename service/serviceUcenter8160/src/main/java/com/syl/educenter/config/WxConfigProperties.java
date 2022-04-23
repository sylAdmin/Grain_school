package com.syl.educenter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wx.open")
@Data
public class WxConfigProperties {

    //微信开放平台 appid
    private String app_id;
    //微信开放平台 appsecret
    private String app_secret;
    //微信开放平台 重定向url
    private String redirect_url;
}
