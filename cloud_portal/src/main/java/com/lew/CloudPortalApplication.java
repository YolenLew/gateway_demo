package com.lew;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Yolen
 * @date 2022/2/9
 */
@SpringBootApplication
@MapperScan(basePackages="com.lew.dao")
public class CloudPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudPortalApplication.class, args);
    }
}
