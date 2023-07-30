/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 注意：在SpringBoot的启动类上明确的指出Mapper的Java代码所有包路径。不能只是大概范围，要具体到Mapper文件所在的文件夹路径
 *
 * @author Yolen
 * @date 2022/2/9
 */
@SpringBootApplication
@MapperScan(basePackages = "com.lew.gateway.dao")
public class CloudPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudPortalApplication.class, args);
    }
}
