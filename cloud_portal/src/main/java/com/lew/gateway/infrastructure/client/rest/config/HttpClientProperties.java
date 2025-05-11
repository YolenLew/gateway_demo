/*
 * Copyright (c) 2025. This is my custom copyright information.2025-2025. All rights reserved.
 */

package com.lew.gateway.infrastructure.client.rest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author Yolen
 * @date 2025/5/10
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "http.client")
public class HttpClientProperties {
    /**
     * setConnectTimeout：设置连接超时时间，单位毫秒。
     * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
     * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
     * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
     */
    private Duration connectTimeout = Duration.ofSeconds(5);
    private Duration responseTimeout = Duration.ofSeconds(6);
    private Duration socketTimeout = Duration.ofSeconds(60);
    private Duration maxTotal = Duration.ofSeconds(60);

    private ProxyConfig proxy = new ProxyConfig();
    private SSLConfig ssl = new SSLConfig();

    @Data
    public static class ProxyConfig {
        private String host;
        private int port;
        private boolean enabled;
    }

    @Data
    public static class SSLConfig {
        private boolean insecureEnabled;
    }
}