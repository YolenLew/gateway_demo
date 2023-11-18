/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.config;

import org.apache.http.client.HttpClient;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Yolen
 * @date 2022/2/17
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().setTcpNoDelay(true).build());
        connectionManager.setDefaultMaxPerRoute(15);
        connectionManager.setMaxTotal(30);
        HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // 超时时间设置
        factory.setReadTimeout(3000);
        factory.setConnectTimeout(3000);
        factory.setConnectionRequestTimeout(3000);
        return new RestTemplate(factory);
    }

    @Bean(value = "proxyRestTemplate")
    public RestTemplate proxyRestTemplate() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().setTcpNoDelay(true).build());
        connectionManager.setDefaultMaxPerRoute(20);
        connectionManager.setMaxTotal(30);
        HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // 超时时间设置
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        factory.setConnectionRequestTimeout(5000);
        return new RestTemplate(factory);
    }

}
