/*
 * Copyright (c) 2025. This is my custom copyright information.2025-2025. All rights reserved.
 */

package com.lew.gateway.infrastructure.client.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yolen
 * @date 2025/5/11
 */
@Slf4j
@Component
public class HttpClientUtil {
    private final CloseableHttpClient defaultClient;
    private final CloseableHttpClient proxyClient;

    public HttpClientUtil(@Qualifier("defaultHttpClient") CloseableHttpClient defaultClient,
        @Qualifier("proxyHttpClient") CloseableHttpClient proxyClient) {
        this.defaultClient = defaultClient;
        this.proxyClient = proxyClient;
    }

    public String doGet(String url, Map<String, String> params, boolean useProxy) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        // 将params及url生成uri
        httpGet.setURI(buildUri(url, params));
        return executeRequest(httpGet, useProxy);
    }

    private URI buildUri(String url, Map<String, String> params) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url, StandardCharsets.UTF_8);
        if (MapUtils.isEmpty(params)) {
            //直接构建uri
            return uriBuilder.build();
        }
        // 组装查询参数params
        List<NameValuePair> nameValuePairList =
            params.entrySet().stream().map(e -> new BasicNameValuePair(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        uriBuilder.setParameters(nameValuePairList);
        return uriBuilder.build();
    }

    public String doPostJson(String url, String json, Map<String, String> headers, boolean useProxy)
        throws IOException {
        HttpPost httpPost = new HttpPost(url);
        addHeaders(httpPost, headers);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        return executeRequest(httpPost, useProxy);
    }

    public boolean downloadFile(String url, Path savePath, boolean useProxy) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = getClient(useProxy).execute(httpGet);
            InputStream inputStream = response.getEntity().getContent()) {
            Files.copy(inputStream, savePath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        }
    }

    public String uploadFile(String url, File file, Map<String, String> formParams, boolean useProxy)
        throws IOException {
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder =
            MultipartEntityBuilder.create().addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName());

        if (formParams != null) {
            formParams.forEach(builder::addTextBody);
        }

        httpPost.setEntity(builder.build());
        return executeRequest(useProxy ? proxyClient : defaultClient, httpPost);
    }

    public boolean downloadFile(String url, String savePath, boolean useProxy) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = (useProxy ? proxyClient : defaultClient).execute(httpGet);
            InputStream is = response.getEntity().getContent()) {
            Files.copy(is, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
            return true;
        }
    }

    private String executeRequest(CloseableHttpClient client, HttpUriRequest request) throws IOException {
        try (CloseableHttpResponse response = client.execute(request)) {
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        }
    }

    private String executeRequest(HttpUriRequest request, boolean useProxy) throws IOException {
        try (CloseableHttpResponse response = getClient(useProxy).execute(request)) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        }
    }

    private static void packageJsonBody(String body, HttpEntityEnclosingRequestBase httpRequest) {
        if (body != null) {
            httpRequest.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        }
    }

    public static void addParam(HttpEntityEnclosingRequestBase httpMethod, Map<String, String> params) {
        // 封装请求参数
        if (MapUtils.isNotEmpty(params)) {
            List<NameValuePair> nameValuePairList =
                params.entrySet().stream().map(e -> new BasicNameValuePair(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
            // 设置到请求的http对象中
            httpMethod.setEntity(new UrlEncodedFormEntity(nameValuePairList, StandardCharsets.UTF_8));
        }
    }

    private void addHeaders(HttpUriRequest request, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach(request::addHeader);
        }
    }

    private CloseableHttpClient getClient(boolean useProxy) {
        return useProxy ? proxyClient : defaultClient;
    }
}