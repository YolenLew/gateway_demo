/*
 * Copyright (c) 2025. This is my custom copyright information.2025-2025. All rights reserved.
 */

package com.lew;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class MultiThreadHttpTestApache {

    public static void main(String[] args) throws Exception {
        LogDebugReject.debugReject();
        long startTime = System.currentTimeMillis();
        mainThreadPoolWithHttpClient();
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime) / 1000 + "秒");
    }

    public static void mainThreadPoolWithHttpClient() throws Exception {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            urls.add("https://www.example.com");
        }

        // 创建HTTP连接池
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(500); // 最大连接数
        cm.setDefaultMaxPerRoute(100); // 每个路由最大连接数

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(100);

        try {
            List<Future<Integer>> futures = new ArrayList<>();

            for (String url : urls) {
                Callable<Integer> task = () -> fetchStatusWithHttpClient(httpClient, url);
                futures.add(executor.submit(task));
            }

            List<Integer> statusCodes = new ArrayList<>();
            for (Future<Integer> future : futures) {
                statusCodes.add(future.get());
            }

            System.out.println("Total requests: " + statusCodes.size());
            System.out.println("Unique status codes: " +
                    statusCodes.stream().distinct().collect(Collectors.toList()));

        } finally {
            executor.shutdown();
            httpClient.close();
            cm.close();
        }
    }

    private static int fetchStatusWithHttpClient(CloseableHttpClient httpClient, String url) {
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            return -1;
        } finally {
            request.releaseConnection();
        }
    }
}