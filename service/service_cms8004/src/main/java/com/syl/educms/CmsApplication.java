package com.syl.educms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.syl")
public class CmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class,args);
    }
}
