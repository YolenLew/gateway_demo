/*
 * Copyright (c) 2024. This is my custom copyright information.2024-2024. All rights reserved.
 */

package com.lew.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 在 Spring Boot 中把 YAML 属性绑定到 Map
 * <p>
 * 配置属性yaml，接下来尝试将 app 映射为一个简单的 Map<String, String>。
 * 将 config 注入为一个 Map<String, List<String>>，
 * 将 users 映射为一个Map，其中 Key 是 String，Value 是自定义（Credential）对象
 *
 * @author Yolen
 * @date 2024/6/16
 */
@Data
@Component
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {
    private Map<String, String> app;
    private Map<String, List<String>> config;
    private Map<String, Credential> users;

    @Data
    public static class Credential {
        private String username;
        private String password;
        private String alias;
    }
}