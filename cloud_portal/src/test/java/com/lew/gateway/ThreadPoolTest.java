
/*
 * Copyright (c) 2025. This is my custom copyright information.2025-2025. All rights reserved.
 */

package com.lew.gateway;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yolen
 * @date 2025-12-17
 */
public class ThreadPoolTest {
    @Test
    public void testConcurrentRequest() throws InterruptedException {
        // 开始计时
        long startTime = System.currentTimeMillis();
        CloseableHttpClient httpClient = getHttpClient();
        // 创建包含100个线程的线程池，发起1000个并发请求，获取请求结果的状态码，请求地址是https://www.example.com
        ThreadPoolExecutor executor =
            new ThreadPoolExecutor(100, 100, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        // 结果收集到并发列表中去
        LinkedBlockingQueue<Integer> resultList = new LinkedBlockingQueue<>();
        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                // 发起请求，获取请求结果
                int code = get(httpClient, "https://www.example.com");
                resultList.add(code);
                latch.countDown();
            });
        }
        // 等待所有任务执行完毕
        latch.await();
        // 停止计时
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime) / 1000 + "请求结果：" + resultList);
    }

    // 编写HttpClient通用Get方法
    public int get(CloseableHttpClient httpClient, String url) {
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
            // 获取请求结果
            return response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 构造包含通用请求池配置的httpClient
    public CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().build()).build();
    }
}
