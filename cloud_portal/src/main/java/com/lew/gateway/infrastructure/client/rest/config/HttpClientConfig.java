/*
 * Copyright (c) 2025. This is my custom copyright information.2025-2025. All rights reserved.
 */

package com.lew.gateway.infrastructure.client.rest.config;// HttpClientConfig.java

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Configuration
public class HttpClientConfig {
    // 连接池限制
    private static final int CONNECTION_POOL_MAX = 200;

    // preRouter限制
    private static final int PER_ROUTE_MAX = 20;

    @Autowired
    private HttpClientProperties properties;

    @Bean(name = "defaultHttpClient")
    public CloseableHttpClient defaultHttpClient() throws Exception {
        return buildHttpClient(null);
    }

    @Bean(name = "proxyHttpClient")
    public CloseableHttpClient proxyHttpClient() throws Exception {
        if (properties.getProxy().isEnabled()) {
            HttpHost proxy = new HttpHost(properties.getProxy().getHost(), properties.getProxy().getPort());
            return buildHttpClient(proxy);
        }
        return defaultHttpClient();
    }

    private CloseableHttpClient buildHttpClient(HttpHost proxy) throws Exception {
        return HttpClients.custom().setConnectionManager(createConnectionManager()).evictExpiredConnections()
            .evictIdleConnections(properties.getMaxTotal().toMillis(), TimeUnit.MICROSECONDS)
            .setDefaultRequestConfig(createRequestConfig()).setProxy(proxy).disableAutomaticRetries().build();
    }

    private HttpClientConnectionManager createConnectionManager() throws Exception {
        SSLConnectionSocketFactory sslConnectionSocketFactory = createSSLConnectionSocketFactory();
        PlainConnectionSocketFactory plainConnectionSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry =
            RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainConnectionSocketFactory)
                .register("https", sslConnectionSocketFactory).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setDefaultSocketConfig(
            SocketConfig.custom().setSoTimeout((int)properties.getSocketTimeout().getSeconds()).build());
        connectionManager.setMaxTotal(CONNECTION_POOL_MAX);
        connectionManager.setDefaultMaxPerRoute(PER_ROUTE_MAX);
        return connectionManager;
    }

    private SSLConnectionSocketFactory createSSLConnectionSocketFactory() throws Exception {
        return new SSLConnectionSocketFactory(createSSLContext(), new String[] {"TLSv1.2", "TLSv1.3"}, null,
            NoopHostnameVerifier.INSTANCE);
    }

    private SSLConnectionSocketFactory sslConnectionSocketFactory() throws Exception {
        return new SSLConnectionSocketFactory(createSSLContext(), new String[] {"TLSv1.2", "TLSv1.3"}, null,
            properties.getSsl().isInsecureEnabled() ? NoopHostnameVerifier.INSTANCE :
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

    private SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        if (properties.getSsl().isInsecureEnabled()) {
            // 信任所有证书
            return SSLContexts.custom().loadTrustMaterial((chain, authType) -> true).build();
        }
        return SSLContexts.createDefault();
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom().setConnectTimeout((int)properties.getConnectTimeout().toMillis())
            .setSocketTimeout((int)properties.getSocketTimeout().toMillis())
            .setConnectionRequestTimeout((int)properties.getResponseTimeout().toMillis()).build();
    }
}