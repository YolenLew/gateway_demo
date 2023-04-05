/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.mbg.config;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Properties;

/**
 *
 * @author Yolen
 * @date 2023/4/5
 */
public class MybatisPlusConfig {

    private Properties properties;

    public MybatisPlusConfig(Properties properties) {
        this.properties = properties;
    }

    /**
     * 数据源配置
     *
     * @return DataSourceConfig
     */
    public DataSourceConfig dataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(properties.getProperty("url"));
        dataSourceConfig.setDriverName(properties.getProperty("driverClassName"));
        dataSourceConfig.setUsername(properties.getProperty("username"));
        dataSourceConfig.setPassword(properties.getProperty("password"));
        dataSourceConfig.setTypeConvert(new MysqlTypeConvertCustom());
        return dataSourceConfig;
    }

    /**
     * 包配置
     *
     * @return PackageConfig
     */
    public PackageConfig packageConfig() {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(properties.getProperty("package.parent"));
        packageConfig.setModuleName(properties.getProperty("package.moduleName"));
        packageConfig.setEntity(properties.getProperty("package.entity"));
        packageConfig.setController(properties.getProperty("package.controller"));
        packageConfig.setMapper(properties.getProperty("package.mapper"));
        packageConfig.setXml(properties.getProperty("package.xml"));
        return packageConfig;
    }

    /**
     * 策略配置
     *
     * @return StrategyConfig
     */
    public StrategyConfig strategyConfig() {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setLogicDeleteFieldName("is_deleted");

        // 支持指定表
        String tables = properties.getProperty("strategy.include");
        if (tables != null && tables.length() > 0) {
            String[] includeTables = tables.trim().split(",");
            strategyConfig.setInclude(includeTables);
        }
        return strategyConfig;
    }

    /**
     * 全局配置
     *
     * @return GlobalConfig
     */
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + properties.getProperty("globalConfig.outputDir"));
        globalConfig.setFileOverride(true);
        // 是否生成swagger注解
        // globalConfig.setSwagger2(true);
        globalConfig.setOpen(false);
        globalConfig.setServiceName("%sService");
        return globalConfig;
    }
}
