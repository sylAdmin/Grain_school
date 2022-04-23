package com.syl.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 读取阿里云存储中的文件
 */
// InitializingBean: spring容器启动时加载它的实现类，为属性赋值，然后执行相应方法
@Component
public class ConstantPropertiesUtils implements InitializingBean {
    //读取配置文件中的内容
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;  //地域名
    @Value("${aliyun.oss.file.keyid}")
    private String keyId; //AccessKey ID
    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret; //AccessKey Secret
    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName; //云存储的名字

    //定义公开静态常量
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}
