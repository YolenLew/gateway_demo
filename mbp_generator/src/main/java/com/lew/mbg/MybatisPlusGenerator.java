/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.mbg;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.lew.mbg.config.MybatisPlusConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * mpg代码生成器
 *
 * @author Yolen
 * @date 2023/4/5
 */
public class MybatisPlusGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisPlusGenerator.class);

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        Properties properties = new Properties();
        try {
            properties.load(MybatisPlusGenerator.class.getResourceAsStream("/mybatis-plus.properties"));
        } catch (IOException e) {
            LOGGER.error("failed to load `mybatis-plus.properties` file", e);
        }
        MybatisPlusConfig mybatisPlusConfig = new MybatisPlusConfig(properties);
        mpg.setDataSource(mybatisPlusConfig.dataSourceConfig());
        mpg.setGlobalConfig(mybatisPlusConfig.globalConfig());
        mpg.setPackageInfo(mybatisPlusConfig.packageConfig());
        mpg.setStrategy(mybatisPlusConfig.strategyConfig());
        mpg.execute();
    }
}
